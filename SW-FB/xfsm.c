//
// Created by angelo on 13/12/18.
//

#include <stdlib.h>
#include <stdio.h>
#include <assert.h>
#include <inttypes.h>
#include <rte_acl_osdep.h>
#include <rte_hash.h>
#include <rte_jhash.h>
#include <rte_mempool.h>
#include <rte_ethdev.h>
#include "xfsm.h"

#define RTE_LOGTYPE_XFSM RTE_LOGTYPE_USER1

static void
init_instance(xfsm_instance_t * inst, xfsm_table_t *table){
    inst->destroy = false;
    memset(inst->results, XFSM_TRUE, sizeof(xfsm_results_t) * MAX_CONDITIONS);
    memset(inst->flow_key.flds, 0, sizeof(flow_key_t));
    inst->table = table;
    memset(inst->dbg_regs, 0, sizeof(dbg_reg_t) * MAX_DEBUG_REGISTERS);
    memset(inst->registers, 0, sizeof(int64_t) * NUM_REGISTERS);
    inst->current_state = table->default_state;
    inst->id = 0;
}

static bool
search_key(xfsm_search_key_t *key1, key_node_t *list) {
    key_node_t *keyNode;
    keyNode = list;
    RTE_LOG(INFO, XFSM, "Searching key\n");

    while (keyNode != NULL) {
        xfsm_search_key_t *key2;
        key2 = &keyNode->key;
        if (memcmp(key1, key2, sizeof(xfsm_search_key_t)) == 0) {
            RTE_LOG(INFO, XFSM, "key found: %u\n", key1->state);
            return true;
        }
        keyNode = keyNode->next;
    }

    return false;
}

static int
insert_key(xfsm_search_key_t *key, key_node_t *list) {
    key_node_t *keyNode;
    keyNode = list;

    // in case it's the first key
    if (key->state == STATE_NULL) {
        memcpy(&keyNode->key, key, sizeof(xfsm_search_key_t));
        RTE_LOG(INFO, XFSM, "inserting first key: %u\n", key->state);
        return 0;
    }
    while (keyNode != NULL) {
        if (keyNode->next == NULL){
            keyNode->next = malloc(sizeof(key_node_t));
            keyNode = keyNode->next;
            memcpy(&keyNode->key, key, sizeof(xfsm_search_key_t));
            keyNode->size = 0;
            RTE_LOG(INFO, XFSM, "inserting key: %u\n", key->state);
            keyNode->next=NULL;
            return 0;
        } else
            keyNode = keyNode->next;
    }
    return 0;
}

static int
insert_entry(int id, key_node_t *list, xfsm_search_key_t *key) {
    key_node_t *keyNode = list;

    while (keyNode != NULL) {
        xfsm_search_key_t *key2;
        key2 = &keyNode->key;
        if (memcmp(key, key2, sizeof(xfsm_search_key_t)) == 0) {
            keyNode->entries[keyNode->size] = id;
            keyNode->size++;
            RTE_LOG(INFO, XFSM, "inserting entry: id=%u\n", id);
            return 0;
        }
        keyNode = keyNode->next;
    }

    return -1;
}

/*
 * Function to create the description of a state table
 */
xfsm_table_t *
xfsm_table_create(xfsm_table_entry_t **entries, uint8_t table_size, uint8_t initial_state,
                  xfsm_condition_t *conditions, uint8_t conditions_number, lookup_key_t *lookup_key, uint8_t table_id)
{
    key_node_t *list = malloc(sizeof(key_node_t));
    key_node_t *head = list;
    xfsm_search_key_t *key = malloc(sizeof(xfsm_search_key_t));
    uint8_t key_num = 0;
    struct rte_hash_parameters entries_hash_params = {0};
    struct rte_hash_parameters instances_hash_params = {0};
#ifdef  CACHE_ENABLE
    struct rte_hash_parameters cache_hash_params = {0};
    char cache_hash_name[32];
    snprintf(cache_hash_name, sizeof(cache_hash_name), "cache_hash_%02d-%02d", table_id, rte_lcore_id());
#endif
    xfsm_table_t *xfsm;
    char xfsm_name[32];
    char entries_db_name[32];
    char entries_hash_name[32];
    char instances_hash_name[32];
    char instances_pool_name[32];
    char pkttmp_pool_name[32];

    snprintf(xfsm_name, sizeof(xfsm_name), "xfsm_table_%02d-%02d", table_id, rte_lcore_id());
    snprintf(entries_db_name, sizeof(entries_db_name), "entries_db_%02d-%02d", table_id, rte_lcore_id());
    snprintf(entries_hash_name, sizeof(entries_hash_name), "entries_hash_%02d-%02d", table_id, rte_lcore_id());
    snprintf(instances_hash_name, sizeof(instances_hash_name), "instances_hash_%02d-%02d", table_id, rte_lcore_id());
    snprintf(instances_pool_name, sizeof(instances_pool_name), "instances_pool_%02d-%02d", table_id, rte_lcore_id());
    snprintf(pkttmp_pool_name, sizeof(pkttmp_pool_name), "pkttmp_pool_%02d-%02d", table_id, rte_lcore_id());

    memset(list->entries, 0, sizeof(int)*MAX_ENTRIES_FOREACH_STATE);
    list->key.state = 0;
    list->size = 0;
    list->next = NULL;

    // Populate keys_list
    for (int i=0; i<table_size; i++) {
        // here I have to setup the right tuple value
            // setup_key()
        key->state = entries[i]->current_state;

        if (!search_key(key, list)){
            insert_key(key, list);
            insert_entry(i, list, key);
            key_num++;
        } else {
            insert_entry(i, list, key);
        }
    }

    key_node_t *ptr = head;

    // Xfsm initialization
    xfsm = rte_zmalloc(xfsm_name,
                       sizeof(xfsm_table_t), CACHE_LINE_SIZE);

    // Initialize entries hashmap
    entries_hash_params.name = entries_hash_name;
    entries_hash_params.entries = MAX_ENTRIES_FOREACH_STATE;
    entries_hash_params.key_len = sizeof(xfsm_search_key_t);
    entries_hash_params.hash_func = rte_jhash;
    entries_hash_params.hash_func_init_val = 0;
    entries_hash_params.socket_id = rte_socket_id();
    entries_hash_params.reserved = 0;
    entries_hash_params.extra_flag = 0;

    xfsm->entries_hmap = rte_hash_create(&entries_hash_params);

    // Allocate the entries db
    xfsm->state_entries_pool = rte_zmalloc(entries_db_name,
                                           table_size * sizeof(hmap_entry_t), CACHE_LINE_SIZE);

    int k = 0, ret = 0;
    while (ptr != NULL){
        hmap_entry_t state_entry = {0};

        // for each node on the list we create an array of entries
        for (uint32_t i=0; i<ptr->size; i++) {
            RTE_LOG(INFO, XFSM, "Size = %d, first ptr is %p, second is %p\n", ptr->size, &state_entry.table_entries[i],
                   entries[ptr->entries[i]]);
            memcpy(&state_entry.table_entries[i], entries[ptr->entries[i]], sizeof(xfsm_table_entry_t));
        }

        memcpy(&state_entry.key, &ptr->key, sizeof(xfsm_search_key_t));
        state_entry.size = ptr->size;

        ret = rte_hash_add_key(xfsm->entries_hmap, &state_entry.key);
        if (ret >= 0) {
            RTE_LOG(INFO, XFSM, "Created new state table entry: state=%d\n", state_entry.key.state);
            rte_memcpy(&xfsm->state_entries_pool[ret], &state_entry, sizeof(hmap_entry_t));

            ptr = ptr->next;
            k++;
        } else {
            RTE_LOG(INFO, XFSM, "Cannot create state table entry: state=%d\n", state_entry.key.state);
            break;
        }
    }

    // Initialize instances hashmap
    instances_hash_params.name = instances_hash_name;
    instances_hash_params.entries = MAX_INSTANCES;          // number of maximum instances
    instances_hash_params.key_len = sizeof(flow_key_t);     // size of the key (24 bytes)
    instances_hash_params.hash_func = rte_jhash;
    instances_hash_params.hash_func_init_val = 0;
    instances_hash_params.socket_id = rte_socket_id();
    instances_hash_params.reserved = 0;
    instances_hash_params.extra_flag = 0;

    xfsm->instances_hmap = rte_hash_create(&instances_hash_params);

    // allocate all the instances
    xfsm->instances_pool = rte_zmalloc(instances_pool_name,
                                       MAX_INSTANCES * sizeof(xfsm_instance_t), CACHE_LINE_SIZE);

    for (int i=0; i<MAX_INSTANCES; i++)
        init_instance(&xfsm->instances_pool[i], xfsm);

#ifdef CACHE_ENABLE
    /*
     * Create the flow cache
     */
    cache_hash_params.name = cache_hash_name;
    cache_hash_params.entries = (uint32_t) (MAX_INSTANCES * conditions_number);
    cache_hash_params.key_len = sizeof(cache_key_t);
    cache_hash_params.hash_func = rte_jhash;
    cache_hash_params.hash_func_init_val = 0;
    cache_hash_params.socket_id = rte_socket_id();
    cache_hash_params.reserved = 0;
    cache_hash_params.extra_flag = 0;

    xfsm->cache = rte_hash_create(&cache_hash_params);

#endif

    // create packet template pool
    xfsm->pkttmp_pool = rte_pktmbuf_pool_create(pkttmp_pool_name, PKTTMP_POOL_SIZE, CACHE_LINE_SIZE/16, 0,
                            RTE_MBUF_DEFAULT_BUF_SIZE, rte_socket_id());

    if (xfsm->pkttmp_pool == NULL)
        rte_exit(EXIT_FAILURE, "Cannot create packet template pool\n");

    // final configurations
    memcpy(xfsm->conditions, conditions, sizeof(xfsm_condition_t) * MAX_CONDITIONS);
    xfsm->nb_conditions = conditions_number;
    memcpy(&xfsm->lookup_key, lookup_key, sizeof(lookup_key_t));
    xfsm->default_state = initial_state;
    memset(xfsm->global_registers, 0, sizeof(int64_t) * NUM_GLOBAL_REGISTERS);
    xfsm->in_port = INVALID_PORT;
    xfsm->nb_instances = 0;
    xfsm->table_id = table_id;
    xfsm->stateful = true;

    for (int i=0; i<NUM_HDR_FIELDS; i++){
        xfsm->pkt_fields[i].field = &xfsm_header_fields[i];
        xfsm->pkt_fields[i].value = NULL;
    }

    RTE_LOG(INFO, XFSM, "XFSM Table successfully created\n");

    return xfsm;
}

struct rte_mbuf*
create_pkttmp(xfsm_table_t *xfsm, uint8_t *buf, uint16_t len, uint8_t id) {
    struct rte_mbuf *pkttmp;
    char *data;

    pkttmp = rte_pktmbuf_alloc(xfsm->pkttmp_pool);
    if (pkttmp == NULL)
        rte_exit(EXIT_FAILURE, "Cannot allocate a packet to the mempool %s\n", xfsm->pkttmp_pool->name);

    if (id < PKTTMP_POOL_SIZE)
        xfsm->pkttmps[id] = pkttmp;
    else {
        RTE_LOG(ERR, XFSM, "Id overflowed packet template pool size: %d\n", PKTTMP_POOL_SIZE);
        return NULL;
    }

    data = rte_pktmbuf_append(pkttmp, len);

    rte_memcpy_aligned(data, buf, len);

    return pkttmp;
}

int
destroy_instance(xfsm_instance_t *inst) {
    int32_t ret = 0;

    ret = rte_hash_del_key(inst->table->instances_hmap, &inst->flow_key);

    if (ret < 0) {
        RTE_LOG(ERR, XFSM, "Error: cannot delete key from hasmap\n");
    } else {
        init_instance(inst, inst->table);
    }
    return ret;
}

xfsm_instance_t *
insert_instance(xfsm_table_t *xfsm, flow_key_t *fk) {
    int32_t ret = 0;
    xfsm_instance_t *instance = NULL;
    if (xfsm->nb_instances != MAX_INSTANCES-1) {
        ret = rte_hash_add_key(xfsm->instances_hmap, fk);
        if (ret >= 0) {
            RTE_LOG(INFO, XFSM, "\t\tInstance inserted: %d\n", ret);
            instance = &xfsm->instances_pool[ret];
            rte_memcpy_aligned(&instance->flow_key, fk, sizeof(flow_key_t));
            xfsm->nb_instances++;
            return instance;
        } else {
            RTE_LOG(INFO, XFSM, "\t\terrors in insering instance: %d\n", ret);
        }
    } else {
        RTE_LOG(ERR, XFSM, "\t\tInstances overflowed\n");
    }
    return NULL;
}

xfsm_instance_t *
lookup_instance(xfsm_table_t *xfsm, flow_key_t *flow_key){
    xfsm_instance_t *instance = NULL;
    int32_t ret = 0;

    ret = rte_hash_lookup(xfsm->instances_hmap, flow_key);

    if (ret >= 0) {
        RTE_LOG(INFO, XFSM, "Instance found!\n");
        instance = &xfsm->instances_pool[ret];
    } else {
        RTE_LOG(INFO, XFSM, "\n\t\tInstance not found\n");
        instance = insert_instance(xfsm, flow_key);
    }
    return instance;
}

xfsm_table_entry_t *
cache_lookup(xfsm_table_t *xfsm, cache_key_t *key) {
    int32_t ret = 0;
    xfsm_table_entry_t *match = NULL;

    ret = rte_hash_lookup_data(xfsm->cache, key, (void **) &match);

    if (ret == -ENOENT) {
        return NULL;
    } else {
        return match;
    }
}

int
cache_insert(xfsm_table_t *xfsm, cache_key_t *key, xfsm_table_entry_t *entry) {
    int32_t ret = 0;

    //ret = rte_hash_add_key(xfsm->cache, key);
    ret = rte_hash_add_key_data(xfsm->cache, key, (void *) entry);

    if (ret==0)
        return 0;
    else {
        RTE_LOG(ERR, XFSM, "Cannot insert cache entry. Error code: %d\n", ret);
        return ret;
    }
}

xfsm_table_entry_t*
xfsm_table_lookup(xfsm_instance_t *instance) {
    xfsm_search_key_t key;
    hmap_entry_t *state_entry;
    int32_t ret = 0;
    xfsm_table_entry_t *match = NULL;

    key.state = instance->current_state;

    RTE_LOG(INFO, XFSM, "\n\nLookup. KEY: state=%d\n\n", key.state);

    ret = rte_hash_lookup(instance->table->entries_hmap, &key);

    if (ret >= 0){
        state_entry = &instance->table->state_entries_pool[ret];

        // while loop scanning all hash table entries
        for (int index=0; index < state_entry->size; index++) {
            match = &state_entry->table_entries[index];

            // found entry; now look for matching conditions
            for (int i=0; i<instance->table->nb_conditions; i++) {
                // don't care: go on
                if (match->results[i] == XFSM_DONT_CARE) {
                    if (i == instance->table->nb_conditions - 1)
                        return match;
                    else
                        continue;
                }
                    // there is something
                else {
                    // if I match a condition, continue
                    if (match->results[i] == instance->results[i]) {
                        // if even the last condition matches, return the result
                        if (i == instance->table->nb_conditions - 1)
                            return match;
                        else
                            continue;
                    } else
                        break;
                }
            }

        }

        RTE_LOG(INFO, XFSM, "Match not found\n");
        return NULL;
    } else {
        RTE_LOG(INFO, XFSM, "No matching state entry found\n");
        return NULL;
    }
}

/**
 * Evaluate a single condition
 * @param cond          structure describing the condition
 * @param reg[in,out]   array of xfsm registers passed by reference
 * @return
 */
static xfsm_results_t
evaluate_condition(xfsm_condition_t cond, xfsm_instance_t *inst) {
    xfsm_condition_types_t opcode = cond.condition_type;
    bool result = false;
    int64_t param1 = 0, param2 = 0;
    int64_t *reg = inst->registers;
    int64_t *gr = inst->table->global_registers;

    switch (cond.type1){
        case XFSM_REGISTER:
            param1 = reg[cond.value1];
            break;
        case XFSM_GLOBAL_REGISTER:
            param1 = gr[cond.value1];
            break;
        case XFSM_CONSTANT:
            param1 = cond.value1;
            break;
        case XFSM_HDR_FIELD:
            if (get_hdr_value(inst->table->pkt_fields[cond.value1], (uint64_t *) &param1) < 0)
                RTE_LOG(ERR, XFSM, "Packet field not found\n");
            break;
        default:
            //RTE_LOG(INFO, XFSM, )("Invalid register 1 type\n");
            break;
    }

    switch (cond.type2){
        case XFSM_REGISTER:
            param2 = reg[cond.value2];
            break;
        case XFSM_GLOBAL_REGISTER:
            param2 = gr[cond.value2];
            break;
        case XFSM_CONSTANT:
            param2 = cond.value2;
            break;
        case XFSM_HDR_FIELD:
            if (get_hdr_value(inst->table->pkt_fields[cond.value2], (uint64_t *) &param2) < 0)
                RTE_LOG(ERR, XFSM, "Packet field not found\n");
            break;
        default:
            //RTE_LOG(INFO, XFSM, )("Invalid register 2 type\n");
            break;
    }


    switch (opcode) {
        case XFSM_LESS:
        {
            result = param1 < param2;
            break;
        }
        case XFSM_LESS_EQ:
        {
            result = param1 <= param2;
            break;
        }
        case XFSM_GREATER:
        {
            result = param1 > param2;
            break;
        }
        case XFSM_GREATER_EQ:
        {
            result = param1 >= param2;
            break;
        }
        case XFSM_EQUAL:
        {
            result = param1 == param2;
            break;
        }
        case XFSM_NOT_EQUAL:
        {
            result = param1 != param2;
            break;
        }
    }

    if (result)
        return XFSM_TRUE;
    else
        return XFSM_FALSE;
}

void
xfsm_evaluate_conditions(xfsm_instance_t *xfsm) {
    for (int i=0; i<xfsm->table->nb_conditions; i++)
        xfsm->results[i] = evaluate_condition(xfsm->table->conditions[i], xfsm);
}

/**
 * Action update: calculate operation and update registers
 * @param reg1              first register of the operation
 * @param reg2              first register of the operation. Could be XFSM_NULL
 * @param ext_param         if reg2 = XFSM_NULL param2 would be the second parameter
 * @param opcode            operation type
 * @param output            output register
 * @param xfsm_regs[out,in] reference to xfsm registers array
 */
static inline void
update(int64_t reg1, int64_t reg2, xfsm_opcodes_t opcode,
       uint8_t output, xfsm_value_type_t out_type, int64_t *xfsm_regs, int64_t *global_regs) {
    int64_t result = 0;

    switch(opcode)
    {
        case(XFSM_SUM):
            result = reg1 + reg2;
            break;
        case(XFSM_MINUS):
            result = reg1 - reg2;
            break;
        case(XFSM_MULTIPLY):
            result = reg1 * reg2;
            break;
        case(XFSM_DIVIDE):
        {
            if (reg2==0)
                result = reg1;
            else
                result = reg1 / reg2;
        }
            break;
        case (XFSM_MODULO):
            result = reg1 % reg2;
            break;
        case(XFSM_MAX):
            result = reg1 > reg2 ? reg1 : reg2;
            break;
        case(XFSM_MIN):
            result = reg1 < reg2 ? reg1 : reg2;
            break;
        default:
            break; // Some warning log
    }
    if (out_type == XFSM_REGISTER)
        xfsm_regs[output] = result;
    else
        global_regs[output] = result;
}

int
xfsm_execute_actions(xfsm_table_entry_t *match, xfsm_instance_t *xfsm, struct rte_eth_dev_tx_buffer *tx_buffer) {
    int64_t reg1 = 0, reg2 = 0, reg3 = 0;

    for (int i=0; i<match->nb_actions; i++){
        xfsm_action_t *act = &match->actions[i];

        switch (act->type1){
            case XFSM_REGISTER:
                reg1 = xfsm->registers[act->value1];
                break;
            case XFSM_GLOBAL_REGISTER:
                reg1 = xfsm->table->global_registers[act->value1];
                break;
            case XFSM_CONSTANT:
                reg1 = act->value1;
                break;
            case XFSM_HDR_FIELD:
                if (act->type == XFSM_SETFIELD)
                    reg1 = act->value1;
                else {
                    if (get_hdr_value(xfsm->table->pkt_fields[act->value1], (uint64_t *) &reg1) < 0)
                        RTE_LOG(ERR, XFSM, "Packet field not found\n");
                }
                break;
            default:
                if (act->type == XFSM_DESTROY)
                    break;
                RTE_LOG(INFO, XFSM, "Invalid field 1 type\n");
                break;
        }

        switch (act->type2){
            case XFSM_REGISTER:
                reg2 = xfsm->registers[act->value2];
                break;
            case XFSM_GLOBAL_REGISTER:
                reg2 = xfsm->table->global_registers[act->value2];
                break;
            case XFSM_CONSTANT:
                reg2 = act->value2;
                break;
            case XFSM_HDR_FIELD:
                if (get_hdr_value(xfsm->table->pkt_fields[act->value2], (uint64_t *) &reg2) < 0)
                    RTE_LOG(ERR, XFSM, "Packet field not found\n");
                break;
            default:
                if (act->type == XFSM_DESTROY)
                    break;
                RTE_LOG(INFO, XFSM, "Invalid field 2 type\n");
                break;
        }

        switch (act->type3){
            case XFSM_REGISTER:
                reg3 = xfsm->registers[act->value3];
                break;
            case XFSM_GLOBAL_REGISTER:
                reg3 = xfsm->table->global_registers[act->value3];
                break;
            case XFSM_CONSTANT:
                reg3 = act->value3;
                break;
            case XFSM_HDR_FIELD:
                if (get_hdr_value(xfsm->table->pkt_fields[act->value3], (uint64_t *) &reg3) < 0)
                    RTE_LOG(ERR, XFSM, "Packet field not found\n");
                break;
            default:
                if (act->type == XFSM_DESTROY)
                    break;
                RTE_LOG(INFO, XFSM, "Invalid field 3 type\n");
                break;
        }

        //RTE_LOG(INFO, XFSM, "\nreg1: %lu, reg2: %lu, reg3: %lu\n", reg1, reg2, reg3);

        switch(act->type) {
            case XFSM_UPDATE:
            {
                update(reg1, reg2, act->opcode, act->output, act->out_type,
                       xfsm->registers, xfsm->table->global_registers);
                break;
            }
                //                reg1    const|reg2
                // send_packet(packet_ref, out_port);
            case XFSM_SENDPKT:
            {
                reg2 = act->value2;

                int ret = 0;
                if (reg2 == 0) {
                    break;
                }
                ret = rte_eth_tx_buffer((uint16_t) (reg2-1), 0, &tx_buffer[reg2-1], xfsm->table->curr_pkt);
                RTE_LOG(INFO, XFSM, "out_port: %d, return code: %d\n", (int) reg2-1, ret);
                break;
            }
                // set_field(field_name, pkt_ref, value)
                //              reg1      const    reg3
            case XFSM_SETFIELD:
            {
                uint64_t field_name, value;

                field_name = (uint64_t) reg1;
                value = (uint64_t) reg3;

                switch (xfsm->table->pkt_fields[field_name].field->len){
                    case 6:
                        value <<= 16;
                        value = rte_cpu_to_be_64(value);
                        break;
                    case 4:
                        value = rte_cpu_to_be_32( (uint32_t) value);
                        break;
                    case 2:
                        value = rte_cpu_to_be_16( (uint16_t) value);
                        break;
                    default:
                        break;
                }

                if (xfsm->table->pkt_fields[field_name].value != NULL) {
                    // value of packet fields are pointers to the packet field
                    // this memcpy copies the value directly in the packet header

                    rte_memcpy(xfsm->table->pkt_fields[field_name].value, &value,
                           xfsm->table->pkt_fields[field_name].field->len);

                    RTE_LOG(INFO, XFSM, "Executing set field: field: %s, value: %lu\n",
                           xfsm->table->pkt_fields[field_name].field->name, value);
                } else
                    RTE_LOG(INFO, XFSM, "Field not found in packet header... doing nothing\n");

                break;
            }
            case XFSM_DESTROY:
            {
                // good bye my darling
                xfsm->destroy = true;
                break;
            }
        }
    }
    return 0;
}

static inline void
biflow(uint32_t *key, lookup_key_t *lookup_flds) {
    uint32_t tmp;

    RTE_LOG(INFO, XFSM, "Biflow handling...\n");

    for (int i=0; i<lookup_flds->biflow_no * 2; i+=2){
        if (key[i] > key[i+1]) {
            tmp = key[i];
            key[i] = key[i+1];
            key[i+1] = tmp;
            RTE_LOG(INFO, XFSM, "Swapped fields\n");
        }
    }
}


bool
key_extract(xfsm_table_t *xfsm, flow_key_t *key) {
    hdr_fields fld;

    for (int i=0; i<xfsm->lookup_key.size; i++){
        fld = xfsm->lookup_key.fld[i];
        if (get_hdr_value(xfsm->pkt_fields[fld], (uint64_t *) &key->flds[i]) < 0)
            RTE_LOG(ERR, XFSM, "Packet field not found\n");
        else
            RTE_LOG(INFO, XFSM, "\tfield %d: %u\n", i, key->flds[i]);

    }

    if (xfsm->lookup_key.biflow_no > 0)
        biflow(key->flds, &xfsm->lookup_key);

    //RTE_LOG(INFO, XFSM, "Key extracted\n");
    return true;
}
