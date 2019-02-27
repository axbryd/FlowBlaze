//
// Created by angelo on 23/01/19.
//
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdint.h>
#include <inttypes.h>
#include <sys/types.h>
#include <sys/queue.h>
#include <netinet/in.h>
#include <setjmp.h>
#include <stdarg.h>
#include <ctype.h>
#include <errno.h>
#include <getopt.h>
#include <signal.h>
#include <stdbool.h>

#include <rte_common.h>
#include <rte_log.h>
#include <rte_malloc.h>
#include <rte_memory.h>
#include <rte_memcpy.h>
#include <rte_eal.h>
#include <rte_launch.h>
#include <rte_atomic.h>
#include <rte_cycles.h>
#include <rte_prefetch.h>
#include <rte_lcore.h>
#include <rte_per_lcore.h>
#include <rte_branch_prediction.h>
#include <rte_interrupts.h>
#include <rte_random.h>
#include <rte_debug.h>
#include <rte_ether.h>
#include <rte_ethdev.h>
#include <rte_mempool.h>
#include <rte_mbuf.h>

#include "../xfsm.h"
#include "table_def.h"

static volatile bool force_quit;

#define RTE_LOGTYPE_CREATE_XFSM RTE_LOGTYPE_USER1

#define MAX_PKT_BURST 32
#define BURST_TX_DRAIN_US 1000000 /* TX drain every ~100us */

/*
 * Configurable number of RX/TX ring descriptors
 */
#define RTE_TEST_RX_DESC_DEFAULT 4096
#define RTE_TEST_TX_DESC_DEFAULT 4096
#define TX_BUF_SIZE     4096
static uint16_t nb_rxd = RTE_TEST_RX_DESC_DEFAULT;
static uint16_t nb_txd = RTE_TEST_TX_DESC_DEFAULT;

struct lcore_queue_conf {
    unsigned n_rx_port;
    unsigned queue_id;
} __rte_cache_aligned;
struct lcore_queue_conf lcore_queue_conf[RTE_MAX_LCORE];

static struct rte_eth_dev_tx_buffer *tx_buffer[RTE_MAX_ETHPORTS];

static struct rte_eth_conf port_conf = {
    .rxmode = {
        .mq_mode = ETH_MQ_RX_NONE,
        .split_hdr_size = 0,
    },
    .txmode = {
        .mq_mode = ETH_MQ_TX_NONE,
    },
};

struct rte_mempool * pktmbuf_pool = NULL;

/* A tsc-based timer responsible for triggering statistics printout */
static uint64_t timer_period = 10; /* default period is 10 seconds */

/* main processing loop */
static void
fb_worker_main_loop(void)
{
    struct rte_mbuf *pkts_burst[MAX_PKT_BURST];
    struct rte_mbuf *m;
    unsigned lcore_id;
    uint64_t prev_tsc, diff_tsc, cur_tsc, timer_tsc;
    unsigned i, j, nb_rx, nb_rx_ports;
    const uint64_t drain_tsc = (rte_get_tsc_hz() + US_PER_S - 1) / US_PER_S * BURST_TX_DRAIN_US;
    struct rte_eth_dev_tx_buffer *buffer;
    flow_key_t *fk;
    xfsm_table_t *xfsm;
    xfsm_instance_t *match_inst = NULL;
    xfsm_table_entry_t *match = NULL;
    unsigned long cache_matches = 0;
#ifdef CACHE_ENABLE
    cache_key_t *ck;
    ck = rte_zmalloc("ck", sizeof(cache_key_t), CACHE_LINE_SIZE);
#endif

    prev_tsc = 0;
    timer_tsc = 0;

    lcore_id = rte_lcore_id();
    nb_rx_ports = rte_eth_dev_count_avail();

    RTE_LOG(INFO, CREATE_XFSM, "entering main loop on lcore %u\n", lcore_id);

    xfsm = xfsm_table_create(entries, NUM_ENTRIES, 0, conditions, NUM_CONDITION, &lookup_key, 0);
    fk = rte_zmalloc("fk", sizeof(flow_key_t), CACHE_LINE_SIZE);

    while (!force_quit) {
        // Transmit packets
        cur_tsc = rte_rdtsc();

        diff_tsc = cur_tsc - prev_tsc;
        if (unlikely(diff_tsc > drain_tsc)) {

            printf("nb_instances: %"PRIu32"\tcache_matches: %lu\n", xfsm->nb_instances, cache_matches);

            for (i = 0; i < nb_rx_ports; i++) {
                buffer = tx_buffer[i];

                rte_eth_tx_buffer_flush((uint16_t) i, 0, buffer);
            }

            // if timer is enabled
            if (timer_period > 0) {

                // advance the timer
                timer_tsc += diff_tsc;

                // if timer has reached its timeout
                if (unlikely(timer_tsc >= timer_period)) {

                    // do this only on master core
                    if (lcore_id == rte_get_master_lcore()) {
                        // reset the timer
                        timer_tsc = 0;
                    }
                }
            }

            prev_tsc = cur_tsc;
        }

        /*
         * Read packet from RX queues
         */
        for (i = 0; i < nb_rx_ports; i++) {
            nb_rx = rte_eth_rx_burst((uint16_t) i, 0, pkts_burst, MAX_PKT_BURST);

            for (j = 0; j < nb_rx; j++) {

                m = pkts_burst[j];

                rte_prefetch0(rte_pktmbuf_mtod(m, void *));

                xfsm->curr_pkt = m;
                xfsm->in_port = (uint8_t) (i+1);
                parse_pkt_header(xfsm, m);

                if (key_extract(xfsm, fk)) {
                    RTE_LOG(DEBUG, CREATE_XFSM, "FK: %"PRIX32",%"PRIX32"\n", fk->flds[0], fk->flds[1]);

                    match_inst = lookup_instance(xfsm, fk);

                    xfsm_evaluate_conditions(match_inst);

#ifdef CACHE_ENABLE
                    // pack cache entry key
                    rte_memcpy_aligned(ck->results, match_inst->results, sizeof(xfsm_results_t)*NUM_CONDITION);
                    ck->state = match_inst->current_state;

                    match = cache_lookup(xfsm, ck);
                    if (!match) {
#endif
                        match = xfsm_table_lookup(match_inst);

                        if (unlikely(!match))
                            goto free;
#ifdef CACHE_ENABLE
                        else
                            cache_insert(xfsm, ck, match);
                    } else {
                        cache_matches++;
                    }
#endif
                    xfsm_execute_actions(match, match_inst, (struct rte_eth_dev_tx_buffer *) tx_buffer);

                    if (unlikely(match_inst->destroy)) {
                        destroy_instance(match_inst);
                        goto free;
                    }
                    match_inst->current_state = match->next_state;
                }
free:
                rte_pktmbuf_free(m);
            }
        }
    }
}

static int
fb_worker_launch_one_lcore(__attribute__((unused)) void *dummy)
{
    fb_worker_main_loop();
    return 0;
}

static void
signal_handler(int signum)
{
    if (signum == SIGINT || signum == SIGTERM) {
        RTE_LOG(INFO, CREATE_XFSM, "\n\nSignal %d received, preparing to exit...\n",
               signum);
        force_quit = true;
    }
}

int
main(int argc, char **argv)
{
    struct lcore_queue_conf *qconf;
    int ret;
    uint16_t nb_ports;
    uint16_t nb_ports_available = 0;
    uint16_t portid;
    unsigned lcore_id;
    unsigned int nb_lcores = 0;
    unsigned int nb_mbufs;

    /* init EAL */
    ret = rte_eal_init(argc, argv);
    if (ret < 0)
        rte_exit(EXIT_FAILURE, "Invalid EAL arguments\n");

    argc -= ret;
    argv += ret;

    force_quit = false;
    signal(SIGINT, signal_handler);
    signal(SIGTERM, signal_handler);

    //rte_log_set_global_level(RTE_LOG_EMERG);

    // parse arguments

    /* convert to number of cycles */
    timer_period *= rte_get_timer_hz();

    nb_ports = rte_eth_dev_count_avail();
    nb_lcores = rte_lcore_count();

    if (nb_ports == 0)
        rte_exit(EXIT_FAILURE, "No Ethernet ports - bye\n");

    for (unsigned i=0; i<nb_lcores; i++) {
        qconf = &lcore_queue_conf[i];

        qconf->n_rx_port = nb_ports;
        qconf->queue_id = i;
    }

    nb_mbufs = RTE_MAX(nb_ports * (nb_rxd + nb_txd + MAX_PKT_BURST +
                                   nb_lcores * CACHE_LINE_SIZE), 8192U);

    /* create the mbuf pool */
    pktmbuf_pool = rte_pktmbuf_pool_create("mbuf_pool", nb_mbufs, CACHE_LINE_SIZE, 0,
                                           RTE_MBUF_DEFAULT_BUF_SIZE, rte_socket_id());
    if (pktmbuf_pool == NULL)
        rte_exit(EXIT_FAILURE, "Cannot init mbuf pool\n");

    /* Initialise each port */
    RTE_ETH_FOREACH_DEV(portid) {
        struct rte_eth_rxconf rxq_conf;
        struct rte_eth_txconf txq_conf;
        struct rte_eth_conf local_port_conf = port_conf;
        struct rte_eth_dev_info dev_info;

        nb_ports_available++;

        /* init port */
        RTE_LOG(INFO, CREATE_XFSM, "Initializing port %u... ", portid);
        //fflush(stdout);
        rte_eth_dev_info_get(portid, &dev_info);
        if (dev_info.tx_offload_capa & DEV_TX_OFFLOAD_MBUF_FAST_FREE)
            local_port_conf.txmode.offloads |= DEV_TX_OFFLOAD_MBUF_FAST_FREE;

        ret = rte_eth_dev_configure(portid, (uint16_t) nb_lcores, (uint16_t) nb_lcores, &local_port_conf);
        if (ret < 0)
            rte_exit(EXIT_FAILURE, "Cannot configure device: err=%d, port=%u\n",
                     ret, portid);

        ret = rte_eth_dev_adjust_nb_rx_tx_desc(portid, &nb_rxd, &nb_txd);

        if (ret < 0)
            rte_exit(EXIT_FAILURE,
                     "Cannot adjust number of descriptors: err=%d, port=%u\n",
                     ret, portid);

        /* init one RX queue */
        //fflush(stdout);
        rxq_conf = dev_info.default_rxconf;
        rxq_conf.offloads = local_port_conf.rxmode.offloads;

        for (uint16_t i=0; i<nb_lcores; i++) {
            ret = rte_eth_rx_queue_setup(portid, i, nb_rxd,
                                         (unsigned int) rte_eth_dev_socket_id(portid),
                                         &rxq_conf,
                                         pktmbuf_pool);
            if (ret < 0)
                rte_exit(EXIT_FAILURE, "rte_eth_rx_queue_setup:err=%d, port=%u\n",
                         ret, portid);
        }

        /* init one TX queue on each port */
        //fflush(stdout);
        txq_conf = dev_info.default_txconf;
        txq_conf.offloads = local_port_conf.txmode.offloads;

        for (uint16_t i=0; i<nb_lcores; i++) {
            ret = rte_eth_tx_queue_setup(portid, i, nb_txd,
                                         (unsigned int) rte_eth_dev_socket_id(portid),
                                         &txq_conf);

            if (ret < 0)
                rte_exit(EXIT_FAILURE, "rte_eth_tx_queue_setup:err=%d, port=%u\n",
                         ret, portid);
        }

        tx_buffer[portid] = rte_zmalloc_socket("tx_buffer",
                                               RTE_ETH_TX_BUFFER_SIZE(TX_BUF_SIZE), 0,
                                               rte_eth_dev_socket_id(portid));
        if (tx_buffer[portid] == NULL)
            rte_exit(EXIT_FAILURE, "Cannot allocate buffer for tx on port %u\n",
                     portid);

        rte_eth_tx_buffer_init(tx_buffer[portid], TX_BUF_SIZE);

        if (ret < 0)
            rte_exit(EXIT_FAILURE,
                     "Cannot set error callback for tx buffer on port %u\n",
                     portid);

        /* Start device */
        ret = rte_eth_dev_start(portid);
        if (ret < 0)
            rte_exit(EXIT_FAILURE, "rte_eth_dev_start:err=%d, port=%u\n",
                     ret, portid);

        RTE_LOG(INFO, CREATE_XFSM, "done: \n");

        rte_eth_promiscuous_enable(portid);
    }

    if (!nb_ports_available) {
        rte_exit(EXIT_FAILURE,
                 "All available ports are disabled. Please set portmask.\n");
    }


    ret = 0;
    /* launch per-lcore init on every lcore */
    rte_eal_mp_remote_launch(fb_worker_launch_one_lcore, NULL, CALL_MASTER);
    RTE_LCORE_FOREACH_SLAVE(lcore_id) {
        if (rte_eal_wait_lcore(lcore_id) < 0) {
            ret = -1;
            break;
        }
    }

    RTE_ETH_FOREACH_DEV(portid) {
        RTE_LOG(INFO, CREATE_XFSM, "Closing port %d...", portid);
        rte_eth_dev_stop(portid);
        rte_eth_dev_close(portid);
        RTE_LOG(INFO, CREATE_XFSM, " Done\n");
    }
    RTE_LOG(INFO, CREATE_XFSM, "Bye...\n");

    return ret;
}
