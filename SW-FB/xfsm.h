//
// Created by angelo on 13/12/18.
//

#ifndef FLOWBLAZE_DPDK_XFSM_H
#define FLOWBLAZE_DPDK_XFSM_H

#include <stdint.h>
#include <stdbool.h>
#include <unistd.h>

#define NUM_REGISTERS 32

#define MAX_STAGES  8
#define NULL_STAGE 255
#define XFSM_TABLE_MAX_ENTRIES 64
#define MAX_CONDITIONS 16
#define MAX_ACTIONS 32
#define FK_MAX_LEN 32
#define STATE_NULL 255
#define MAX_EXACT_MATCH_BYTES 16
#define MAX_TUPLES 16
#define MAX_TUPLE_LEN 8
#define MAX_INSTANCES 1000000
#define CACHE_LINE_SIZE 64
#define PKTTMP_POOL_SIZE    8

#define MAX_ENTRIES_FOREACH_STATE 10

#define LOOKUP_KEY_SIZE         8

#define MAX_DEBUG_REGISTERS     4
#define NUM_GLOBAL_REGISTERS 	32

#define INVALID_PORT    255


typedef enum {
    SRC_MAC = 0,
    DST_MAC,
    ETH_TYPE,

    IPv4_SRC,
    IPv4_DST,
    IPv4_PROTO,
    IPv4_TOS,

    ICMP_TYPE,
    ICMP_CODE,
    ICMP_ID,
    ICMP_SEQ,
    ICMP_PAYLOAD,

    TCP_SPORT,
    TCP_DPORT,

    UDP_SPORT,
    UDP_DPORT,

    GTP_TEID,

    INNER_IPv4_SRC,
    INNER_IPv4_DST,
    INNER_IPv4_PROTO,

    INNER_TCP_SPORT,
    INNER_TCP_DPORT,

    INNER_UDP_SPORT,
    INNER_UDP_DPORT,

    XFSM_IN_PORT,
    XFSM_METADATA0,
    XFSM_METADATA1,
    XFSM_TIMESTAMP,
} hdr_fields;

#define NUM_HDR_FIELDS 28


extern const struct xfsm_hdr_fields {
    hdr_fields type;
    const char *name;
    size_t len;
} xfsm_header_fields[NUM_HDR_FIELDS];

typedef struct xfsm_fields_tlv {
    const struct xfsm_hdr_fields *field;
    uint8_t *value;
} xfsm_fields_tlv_t;

/*
 * XFSM enums and structures
 */

typedef enum {
    XFSM_REGISTER = 0,
    XFSM_CONSTANT,
    XFSM_HDR_FIELD,
    XFSM_GLOBAL_REGISTER
} xfsm_value_type_t;

typedef enum {
    XFSM_SUM = 0,
    XFSM_MINUS,
    XFSM_MULTIPLY,
    XFSM_DIVIDE,
    XFSM_MODULO,
    XFSM_MAX,
    XFSM_MIN
} xfsm_opcodes_t;

typedef enum {
    XFSM_LESS = 0,
    XFSM_LESS_EQ,
    XFSM_GREATER,
    XFSM_GREATER_EQ,
    XFSM_EQUAL,
    XFSM_NOT_EQUAL
} xfsm_condition_types_t;

typedef enum {
    XFSM_UPDATE=0,
    XFSM_SENDPKT,
    XFSM_SETFIELD,
    XFSM_DESTROY
} xfsm_action_types_t;

typedef enum {
    XFSM_FALSE = 0,
    XFSM_TRUE,
    XFSM_DONT_CARE
} xfsm_results_t;

typedef struct xfsm_condition {
    xfsm_value_type_t 		type1;
    xfsm_value_type_t 		type2;
    uint64_t				value1;
    uint64_t				value2;
    xfsm_condition_types_t 	condition_type;
} xfsm_condition_t ;

typedef struct lookup_key {
    uint8_t size;
    hdr_fields fld[LOOKUP_KEY_SIZE];
    uint8_t biflow_no;
} lookup_key_t ;

typedef struct dbg_reg {
    uint8_t reg_num;
    uint64_t sample_time;
    uint64_t last_sample_time;
    bool valid;
} dbg_reg_t ;

typedef struct hmap_entry hmap_entry_t;
typedef struct xfsm_instance xfsm_instance_t;

typedef struct cache_key {
    xfsm_results_t      results[MAX_CONDITIONS];
    uint32_t            state;
} cache_key_t ;

typedef struct flow_key {
    uint32_t flds[8];
} flow_key_t;

typedef struct xfsm_table {
    /* Static parameters, initialized at configuration time */
    xfsm_condition_t			conditions[MAX_CONDITIONS];		// array of conditions
    uint8_t 					nb_conditions;				    // number of configured conditions
    int64_t 					global_registers[NUM_GLOBAL_REGISTERS];
    lookup_key_t                lookup_key;
    uint32_t                    default_state;
    uint8_t                     in_port;
    struct rte_hash             *entries_hmap;
    struct rte_hash             *instances_hmap;
    xfsm_instance_t             *instances_pool;
    hmap_entry_t                *state_entries_pool;
    uint32_t                    nb_instances;
    dbg_reg_t                   dbg_regs[MAX_DEBUG_REGISTERS];
    bool                        stateful;
    struct rte_hash             *cache;
    uint8_t                     table_id;
    struct rte_mbuf             *curr_pkt;
    xfsm_fields_tlv_t           pkt_fields[NUM_HDR_FIELDS];
    struct rte_mempool          *pkttmp_pool;
    struct rte_mbuf             *pkttmps[PKTTMP_POOL_SIZE];
} xfsm_table_t ;

struct xfsm_instance {
    xfsm_table_t                *table;
    int64_t     			    registers[NUM_REGISTERS];		// array of registers
    uint32_t 					current_state;					// current state of xfsm
    xfsm_results_t 				results[MAX_CONDITIONS];		// results of current condition eval
    flow_key_t                  flow_key;                       // flow key of this xfsm
    dbg_reg_t                   dbg_regs[MAX_DEBUG_REGISTERS];
    bool						destroy;
    uint32_t                    id;
};

typedef struct xfsm_action {
    xfsm_action_types_t 	type;
    xfsm_opcodes_t 			opcode;
    xfsm_value_type_t 		type1;
    uint64_t                value1;
    xfsm_value_type_t 		type2;
    uint64_t 				value2;
    xfsm_value_type_t 		type3;
    uint64_t                value3;
    uint8_t         		output;
    xfsm_value_type_t       out_type;
} xfsm_action_t;

typedef struct xfsm_table_entry {
    uint32_t  				current_state;
    uint32_t  				next_state;
    uint8_t                 next_stage;
    xfsm_results_t			results[MAX_CONDITIONS];
    xfsm_action_t 			actions[MAX_ACTIONS];
    uint16_t 				nb_actions;
    uint16_t 				priority;
    uint32_t                idle_timeout;
    uint32_t                hard_timeout;
} xfsm_table_entry_t;


typedef struct xfsm_pipeline {
    xfsm_table_t 	**tables;
    uint8_t 		num_stages;
} xfsm_pipeline_t;

typedef struct tuple {
    uint8_t                 nb_fields;
    hdr_fields              fields[MAX_TUPLE_LEN];
} tuple_t;

typedef struct tuple_table {
    uint8_t             nb_tuples;
    tuple_t             tuples[MAX_TUPLES];
    struct rte_hash     *tables[MAX_TUPLES];
} tuple_table_t;

typedef struct xfsm_search_key {
    uint32_t state;
} xfsm_search_key_t;

/*
 * Structs and functions to populate table entries
 */

typedef struct key_node {
    xfsm_search_key_t key;
    int entries[MAX_ENTRIES_FOREACH_STATE];
    uint8_t size;
    struct key_node *next;
} key_node_t;

struct hmap_entry {
    xfsm_search_key_t key;
    xfsm_table_entry_t table_entries[MAX_ENTRIES_FOREACH_STATE];
    int size;
};

/* XFSM Functions */

/**
 * Creates an xfsm instance
 * @param entries               array of xfsm_table_entry_t
 * @param table_size            number of entries
 * @param initial_state         state in which xfsm starts
 * @param conditions            array of xfsm_condition_t
 * @param conditions_number     number of conditions
 * @return xfsm instance
 */
xfsm_table_t *
xfsm_table_create(xfsm_table_entry_t **entries, uint8_t table_size, uint8_t initial_state,
                  xfsm_condition_t *conditions, uint8_t conditions_number, lookup_key_t *lookup_key, uint8_t table_id);

/**
 * Lookup for matching table entry
 * @param instance Xfsm instance
 * @retval match Matching table entry
 */
xfsm_table_entry_t *
xfsm_table_lookup(xfsm_instance_t *instance);

/**
 * Evaluate conditions and save the results
 * @param xfsm  the xfsm instance
 * @return void
 */
void
xfsm_evaluate_conditions(xfsm_instance_t *xfsmi);

// declare the struct to avoid warning
struct rte_eth_dev_tx_buffer;

int
xfsm_execute_actions(xfsm_table_entry_t *match, xfsm_instance_t *xfsm, struct rte_eth_dev_tx_buffer *tx_buffer);

/**
 * Parses packet header and inserts resulting values in the fields array of xfsm table.
 * Fields are saved as pointer to the packet buffer, so still in big endian.
 *
 * @param xfsm Xfsm instance
 * @param packet Packet
 * @retval -1 if packet is invalid
 * @retval -2 if packet does not have eth header
 * @retval -3 if packet does not have ipv4
 * @retval -4 if packet does not have either udp or tcp header
 * @retval PROTO_TCP if packet has tcp
 * @retval PROTO_UDP if packet has udp
 */
int
parse_pkt_header(xfsm_table_t *xfsm, struct rte_mbuf *packet);

struct rte_mbuf*
create_pkttmp(xfsm_table_t *xfsm, uint8_t *buf, uint16_t len, uint8_t id);

bool
key_extract(xfsm_table_t *xfsm, flow_key_t *fk);

xfsm_instance_t *
lookup_instance(xfsm_table_t *xfsm, flow_key_t *flow_key);

int
destroy_instance(xfsm_instance_t *inst);

xfsm_instance_t *
insert_instance(xfsm_table_t *xfsm, flow_key_t *fk);

xfsm_table_entry_t *
cache_lookup(xfsm_table_t *xfsm, cache_key_t *key);

int
cache_insert(xfsm_table_t *xfsm, cache_key_t *key, xfsm_table_entry_t *entry);

/**
 * Copies the value of the provided header field in little endian.
 *
 * @param field xfsm_fields_tlv packet field
 * @return value
 */
int
get_hdr_value(xfsm_fields_tlv_t field, uint64_t *value);

#endif //FLOWBLAZE_DPDK_XFSM_H
