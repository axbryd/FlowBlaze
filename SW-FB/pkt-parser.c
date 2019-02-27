
#include <rte_ether.h>
#include <netinet/in.h>
#include "xfsm.h"

#define RTE_LOGTYPE_PARSER RTE_LOGTYPE_USER1
#define GTP_PORT 2152
#define GTP_HEADER_SIZE 8
#define IPV4_ICMP_PROTO 1

const struct xfsm_hdr_fields xfsm_header_fields[NUM_HDR_FIELDS] = {
        {SRC_MAC,               "SRC_MAC" ,                 6},
        {DST_MAC,               "DST_MAC",                  6},
        {ETH_TYPE,              "ETH_TYPE",                 2},
        {IPv4_SRC,              "IPv4_SRC",                 4},
        {IPv4_DST,              "IPv4_DST",                 4},
        {IPv4_PROTO,            "IPv4_PROTO",               1},
        {IPv4_TOS,              "IPv4_TOS",                 1},
        {ICMP_CODE,             "ICMP_CODE",                1},
        {ICMP_TYPE,             "ICMP_TYPE",                1},
        {ICMP_ID,               "ICMP_ID",                  2},
        {ICMP_SEQ,              "ICMP_SEQ",                 2},
        {ICMP_PAYLOAD,          "ICMP_PAYLOAD",             4},
        {TCP_SPORT,             "TCP_SPORT",                2},
        {TCP_DPORT,             "TCP_DPORT",                2},
        {UDP_SPORT,             "UDP_SPORT",                2},
        {UDP_DPORT,             "UDP_DPORT",                2},
        {GTP_TEID,              "GTP-U_TEID",               4},
        {INNER_IPv4_SRC,        "INNER_IPv4_SRC",           4},
        {INNER_IPv4_DST,        "INNER_IPv4_DST",           4},
        {INNER_IPv4_PROTO,      "INNER_IPv4_PROTO",         1},
        {INNER_TCP_SPORT,       "INNER_TCP_SPORT",          2},
        {INNER_TCP_DPORT,       "INNER_TCP_DPORT",          2},
        {INNER_UDP_SPORT,       "INNER_UDP_SPORT",          2},
        {INNER_UDP_DPORT,       "INNER_UDP_DPORT",          2},
        {XFSM_IN_PORT,          "XFSM_IN_PORT",             1},
        {XFSM_METADATA0,        "METADATA0",                4},
        {XFSM_METADATA1,        "METADATA1",                4},
        {XFSM_TIMESTAMP,        "XFSM_TIMESTAMP",           8},
};


static inline uint64_t
be64ptr2h(const uint8_t *byte) {
    uint64_t value = 0;

    for (int i = 0; i < 6; i++)
        value |= (uint64_t) (byte[i] << (5 - i) * 8);
    return value;
}

static inline uint32_t
be32ptr2h(const uint8_t *byte){
    return byte[3] | byte[2] << 8 | byte[1] << 16 | byte[0] << 24;
}

static inline uint16_t
be16ptr2h(const uint8_t *byte){
    return byte[1] | byte[0] << 8;
}

int parse_pkt_header(xfsm_table_t *xfsm, struct rte_mbuf *packet){
    uint8_t *eth, *ipv4, *tp, *gtp_u, *in_ipv4, *in_tp;
    uint64_t dport = 0;

    if (packet == NULL)
        return -1;

    xfsm->pkt_fields[XFSM_IN_PORT].value = &xfsm->in_port;

    if (packet->userdata != NULL){
        xfsm->pkt_fields[XFSM_METADATA0].value = (uint8_t *) &packet->userdata;
        xfsm->pkt_fields[XFSM_METADATA1].value = (uint8_t *) &packet->userdata + 4;
    } else {
        xfsm->pkt_fields[XFSM_METADATA0].value = NULL;
        xfsm->pkt_fields[XFSM_METADATA1].value = NULL;
    }

    eth = rte_pktmbuf_mtod(packet, void *);

    // Ethernet
    xfsm->pkt_fields[SRC_MAC].value  = &eth[6];
    xfsm->pkt_fields[DST_MAC].value  = &eth[0];
    xfsm->pkt_fields[ETH_TYPE].value = &eth[12];

    // IPv4
    ipv4 = eth + ETHER_HDR_LEN;
    xfsm->pkt_fields[IPv4_DST].value   = &ipv4[16];
    xfsm->pkt_fields[IPv4_SRC].value   = &ipv4[12];
    xfsm->pkt_fields[IPv4_PROTO].value = &ipv4[9];
    xfsm->pkt_fields[IPv4_TOS].value   = &ipv4[1];

    tp = ipv4 + 20;

    if (ipv4[9] == IPPROTO_TCP){
        // TODO: FLAGS
        xfsm->pkt_fields[TCP_SPORT].value = &tp[0];
        xfsm->pkt_fields[TCP_DPORT].value = &tp[2];

        xfsm->pkt_fields[UDP_SPORT].value = NULL;
        xfsm->pkt_fields[UDP_DPORT].value = NULL;
    } else if (ipv4[9] == IPV4_ICMP_PROTO) {
        xfsm->pkt_fields[ICMP_TYPE].value    = &tp[0];
        xfsm->pkt_fields[ICMP_CODE].value    = &tp[1];
        xfsm->pkt_fields[ICMP_ID].value      = &tp[4];
        xfsm->pkt_fields[ICMP_SEQ].value     = &tp[6];
        xfsm->pkt_fields[ICMP_PAYLOAD].value = &tp[8];

        // set to null other transport protocols
        xfsm->pkt_fields[UDP_SPORT].value = NULL;
        xfsm->pkt_fields[UDP_DPORT].value = NULL;
        xfsm->pkt_fields[TCP_SPORT].value = NULL;
        xfsm->pkt_fields[TCP_DPORT].value = NULL;
    } else if (ipv4[9] == IPPROTO_UDP) {
        xfsm->pkt_fields[UDP_SPORT].value = &tp[0];
        xfsm->pkt_fields[UDP_DPORT].value = &tp[2];

        xfsm->pkt_fields[TCP_SPORT].value = NULL;
        xfsm->pkt_fields[TCP_DPORT].value = NULL;

        get_hdr_value(xfsm->pkt_fields[UDP_DPORT], &dport);

        if (dport == GTP_PORT) {
            gtp_u = &tp[8];
            xfsm->pkt_fields[GTP_TEID].value = &gtp_u[4];

            // Inner IPv4
            in_ipv4 = gtp_u + GTP_HEADER_SIZE;
            xfsm->pkt_fields[INNER_IPv4_DST].value   = &ipv4[16];
            xfsm->pkt_fields[INNER_IPv4_SRC].value   = &ipv4[12];
            xfsm->pkt_fields[INNER_IPv4_PROTO].value = &ipv4[9];

            in_tp = in_ipv4 + 20;

            if (in_ipv4[9] == IPPROTO_TCP){
                // TODO: FLAGS
                xfsm->pkt_fields[INNER_TCP_SPORT].value = &in_tp[0];
                xfsm->pkt_fields[INNER_TCP_DPORT].value = &in_tp[2];

                xfsm->pkt_fields[INNER_UDP_SPORT].value = NULL;
                xfsm->pkt_fields[INNER_UDP_DPORT].value = NULL;
            } else if (in_ipv4[9] == IPPROTO_UDP) {
                xfsm->pkt_fields[INNER_UDP_SPORT].value = &in_tp[0];
                xfsm->pkt_fields[INNER_UDP_DPORT].value = &in_tp[2];

                xfsm->pkt_fields[INNER_TCP_SPORT].value = NULL;
                xfsm->pkt_fields[INNER_TCP_DPORT].value = NULL;
            }
        } else {
            xfsm->pkt_fields[GTP_TEID].value            = NULL;
            xfsm->pkt_fields[INNER_IPv4_DST].value      = NULL;
            xfsm->pkt_fields[INNER_IPv4_SRC].value      = NULL;
            xfsm->pkt_fields[INNER_IPv4_PROTO].value    = NULL;
            xfsm->pkt_fields[INNER_TCP_SPORT].value     = NULL;
            xfsm->pkt_fields[INNER_TCP_DPORT].value     = NULL;
            xfsm->pkt_fields[INNER_UDP_SPORT].value     = NULL;
            xfsm->pkt_fields[INNER_UDP_DPORT].value     = NULL;
        }
    }

    xfsm->pkt_fields[XFSM_TIMESTAMP].value = (uint8_t *) &packet->timestamp;

    /*printf("Parsing the packet\n");
    for (int i=0; i<NUM_HDR_FIELDS; i++) {
        uint64_t val = 0;

        get_hdr_value(xfsm->pkt_fields[i], &val);
        printf("\t%s: 0x%"PRIX64"\n", xfsm->pkt_fields[i].field->name, val);
    }*/

    return ipv4[9];
}

int get_hdr_value(xfsm_fields_tlv_t field, uint64_t *value) {

    if (field.value == NULL)
        return -1;

    uint8_t *byte = field.value;
    switch (field.field->len) {
        case 8:
            if (field.field->type == XFSM_TIMESTAMP)
                *value = *((uint64_t *) byte);
            break;
        case 6:
            *value = be64ptr2h(byte);
            break;
        case 4:
            *value = be32ptr2h(byte);
            break;
        case 2:
            *value = be16ptr2h(byte);
            break;
        case 1:
            *value = byte[0];
            break;
        default:
            break;
    }

    return 0;

}
