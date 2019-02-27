//
// Created by giacomo on 24/02/19.
//

#ifndef FLOWBLAZE_DPDK_LOADER_H
#define FLOWBLAZE_DPDK_LOADER_H

#include "cJSON.h"
#include "xfsm.h"
#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include <limits.h>
#include <string.h>

#define RETURN_ERROR -1
#define RETURN_SUCCESS 0

typedef int RETURN_STATUS;

RETURN_STATUS str_to_uint64(char *str, uint64_t *val);

RETURN_STATUS str_to_uint32(char *str, uint32_t *val);

RETURN_STATUS str_to_uint8(char *str, uint8_t *val);

RETURN_STATUS str_to_condition_type(char *str, xfsm_condition_types_t *condition_type);

RETURN_STATUS str_to_event_field(char *str, hdr_fields *event_field);

RETURN_STATUS str_to_header_field(char *str, hdr_fields *value);

RETURN_STATUS str_to_result(char *str, xfsm_results_t *result);

RETURN_STATUS str_to_value_type(char *str, xfsm_value_type_t *value);

RETURN_STATUS get_operand(cJSON *operand, xfsm_value_type_t *type, uint64_t *val);

RETURN_STATUS get_out_operand(cJSON *operand, xfsm_value_type_t *type, uint8_t *val);

xfsm_condition_t ** json_to_conditions(cJSON *conditions, int *num);

RETURN_STATUS str_to_action_type(char *str, xfsm_action_types_t *action_type, xfsm_opcodes_t *opcode);

xfsm_action_t **json_to_actions(cJSON *actions, uint16_t *num);

xfsm_table_entry_t *json_to_table_entry(cJSON *json);

lookup_key_t *json_to_lookup_key(cJSON *fields);

xfsm_table_t *json_to_table(cJSON *table);

#endif //FLOWBLAZE_DPDK_LOADER_H
