//
// Created by giacomo on 24/02/19.
//
#include <printf.h>
#include <rte_log.h>
#include "cJSON.h"
#include "loader.h"
#include "xfsm.h"

#define RTE_LOGTYPE_LOADER RTE_LOGTYPE_USER1

RETURN_STATUS str_to_uint64(char *str, uint64_t *val)
{
    char *endptr;

    errno = 0;
    *val = (uint64_t) strtoul(str, &endptr, 10);

    if ((errno == ERANGE && (*val == (uint64_t) LONG_MAX || *val == (uint64_t) LONG_MIN))
        || (errno != 0 && *val == 0) || endptr == str)
        return RETURN_ERROR;

    return RETURN_SUCCESS;
}

RETURN_STATUS str_to_uint32(char *str, uint32_t *val)
{
    char *endptr;

    errno = 0;
    *val = (uint32_t) strtoul(str, &endptr, 10);

    if ((errno == ERANGE && (*val == (uint32_t) LONG_MAX || *val == (uint32_t) LONG_MIN))
        || (errno != 0 && *val == 0) || endptr == str)
        return RETURN_ERROR;

    return RETURN_SUCCESS;
}

RETURN_STATUS str_to_uint8(char *str, uint8_t *val)
{
    char *endptr;

    errno = 0;
    *val = (uint8_t) strtoul(str, &endptr, 10);

    if ((errno == ERANGE && (*val == (uint8_t) LONG_MAX || *val == (uint8_t) LONG_MIN))
        || (errno != 0 && *val == 0) || endptr == str)
        return RETURN_ERROR;

    return RETURN_SUCCESS;
}

RETURN_STATUS str_to_condition_type(char *str, xfsm_condition_types_t *condition_type)
{
    if (strcmp(str, "XTRA_LESS") == 0)
        *condition_type = XFSM_LESS;
    else if (strcmp(str, "XTRA_LESS_EQ") == 0)
        *condition_type = XFSM_LESS_EQ;
    else if (strcmp(str, "XTRA_GREATER") == 0)
        *condition_type = XFSM_GREATER;
    else if (strcmp(str, "XTRA_GREATER_EQ") == 0)
        *condition_type = XFSM_GREATER_EQ;
    else if (strcmp(str, "XTRA_EQUAL") == 0)
        *condition_type = XFSM_EQUAL;
    else if  (strcmp(str, "XTRA_NOT_EQUAL") == 0)
        *condition_type = XFSM_NOT_EQUAL;
    else
    {
        return RETURN_ERROR;
    }

    return RETURN_SUCCESS;
}

RETURN_STATUS str_to_event_field(char *str, hdr_fields *event_field) {
    if (strcmp(str, "XTRA_PKT_RCVD_PORT") == 0) {
        *event_field = XFSM_IN_PORT;
        return RETURN_SUCCESS;
    }
    else
        return RETURN_ERROR;
}

RETURN_STATUS str_to_header_field(char *str, hdr_fields *value) {
    if (strcmp(str, "eth.src") == 0)
        *value = SRC_MAC;
    else if (strcmp(str, "eth.dst") == 0)
        *value = DST_MAC;
    else if (strcmp(str, "eth.type") == 0)
        *value = ETH_TYPE;
    else if (strcmp(str, "ipv4.src") == 0)
        *value = IPv4_SRC;
    else if (strcmp(str, "ipv4.dst") == 0)
        *value = IPv4_DST;
    else if (strcmp(str, "ipv4.proto") == 0)
        *value = IPv4_PROTO;
    else if (strcmp(str, "ipv4.tos") == 0)
        *value = IPv4_TOS;
    else if (strcmp(str, "tcp.sport") == 0)
        *value = TCP_SPORT;
    else if (strcmp(str, "tcp.dport") == 0)
        *value = TCP_DPORT;
    else if (strcmp(str, "udp.sport") == 0)
        *value = UDP_SPORT;
    else if (strcmp(str, "udp.dport") == 0)
        *value = UDP_DPORT;
    else if (strcmp(str, "XTRA_TIMESTAMP") == 0)
        *value = XFSM_TIMESTAMP;
    else
    {
        return RETURN_ERROR;
    }

    return RETURN_SUCCESS;
}

RETURN_STATUS str_to_result(char *str, xfsm_results_t *result) {
    if (strcmp(str, "XTRA_TRUE") == 0)
        *result = XFSM_TRUE;
    else if (strcmp(str, "XTRA_FALSE") == 0)
        *result = XFSM_FALSE;
    else if (strcmp(str, "XTRA_DONT_CARE") == 0)
        *result = XFSM_DONT_CARE;
    else
    {
        return RETURN_ERROR;
    }

    return RETURN_SUCCESS;
}

RETURN_STATUS str_to_value_type(char *str, xfsm_value_type_t *value) {
    if (strcmp(str, "XTRA_LOCAL_REGISTER") == 0)
        *value = XFSM_REGISTER;
    else if (strcmp(str, "XTRA_GLOBAL_REGISTER") == 0)
        *value = XFSM_GLOBAL_REGISTER;
    else if (strcmp(str, "XTRA_INTEGER_CONST") == 0)
        *value = XFSM_CONSTANT;
    else if (strcmp(str, "XTRA_PKT_FIELD") == 0
             || strcmp(str, "XTRA_EVENT_FIELD") == 0
             || strcmp(str, "XTRA_READ_ONLY_REGISTER") == 0)
        *value = XFSM_HDR_FIELD;
    else {
        return RETURN_ERROR;
    }

    return RETURN_SUCCESS;
}

RETURN_STATUS get_out_operand(cJSON *operand, xfsm_value_type_t *type, uint8_t *val) {
    cJSON *type_json = cJSON_GetObjectItemCaseSensitive(operand, "type");

    if (!cJSON_IsString(type_json) || type_json->valuestring == NULL ||
        str_to_value_type(type_json->valuestring, type) != RETURN_SUCCESS) {
        return RETURN_ERROR;
    }

    if(*type == XFSM_REGISTER || *type == XFSM_GLOBAL_REGISTER) {
        cJSON *name = cJSON_GetObjectItemCaseSensitive(operand, "name");
        if (!cJSON_IsString(name) || name->valuestring == NULL || str_to_uint8(name->valuestring, val) != RETURN_SUCCESS)
            return RETURN_ERROR;
        cJSON_free(name);
    }
    else
        return RETURN_ERROR;
}

RETURN_STATUS get_operand(cJSON *operand, xfsm_value_type_t *type, uint64_t *val) {
    cJSON *type_json = cJSON_GetObjectItemCaseSensitive(operand, "type");

    if (!cJSON_IsString(type_json) || type_json->valuestring == NULL ||
        str_to_value_type(type_json->valuestring, type) != RETURN_SUCCESS) {
        return RETURN_ERROR;
    }

    if(*type == XFSM_REGISTER || *type == XFSM_GLOBAL_REGISTER) {
        cJSON *name = cJSON_GetObjectItemCaseSensitive(operand, "name");
        if (!cJSON_IsString(name) || name->valuestring == NULL || str_to_uint64(name->valuestring, val) != RETURN_SUCCESS)
                return RETURN_ERROR;
        cJSON_free(name);
    }
    else if (*type == XFSM_CONSTANT) {
        cJSON *value = cJSON_GetObjectItemCaseSensitive(operand, "value");
        if (!cJSON_IsString(value) || value->valuestring == NULL ||
            str_to_uint64(value->valuestring, val) != RETURN_SUCCESS) {
            return RETURN_ERROR;
        }

        cJSON_free(value);
    }
    else if (*type == XFSM_HDR_FIELD) {
        // try to parse as event field or header field
        cJSON *value = cJSON_GetObjectItemCaseSensitive(operand, "value");

        if (!cJSON_IsString(value) || value->valuestring == NULL) {
            value = cJSON_GetObjectItemCaseSensitive(operand, "name");

            if ((!cJSON_IsString(value) || value->valuestring == NULL))
                return RETURN_ERROR;
        }

        if (str_to_event_field(value->valuestring, (hdr_fields *) val) != RETURN_SUCCESS)
            if (str_to_header_field(value->valuestring, (hdr_fields *) val) != RETURN_SUCCESS)
                return RETURN_ERROR;

        cJSON_free(value);
    }

    cJSON_free(type_json);

    return RETURN_SUCCESS;
}

xfsm_condition_t ** json_to_conditions(cJSON *conditions, int *num)
{
    cJSON *condition = NULL;
    *num = 0;
    cJSON_ArrayForEach(condition, conditions) {
        (*num)++;
    }
    if(*num >= UINT8_MAX) {
        RTE_LOG(ERR, LOADER, "[LOADER_ERROR] Too many conditions defined! Max number is %d, got %d\n", UINT8_MAX, *num);
        exit(EXIT_FAILURE);
    }

    xfsm_condition_t **condition_array = malloc((*num)*sizeof(xfsm_condition_t *));
    if (condition_array == NULL) {
        perror("[LOADER_ERROR] Error in malloc");
        exit(EXIT_FAILURE);
    }

    cJSON_ArrayForEach(condition, conditions) {
        cJSON *id = cJSON_GetObjectItemCaseSensitive(condition, "id");
        cJSON *opcode = cJSON_GetObjectItemCaseSensitive(condition, "opcode");
        cJSON *op0 = cJSON_GetObjectItemCaseSensitive(condition, "op0");
        cJSON *op1 = cJSON_GetObjectItemCaseSensitive(condition, "op1");

        int id_val;
        if (!cJSON_IsString(id) || id->valuestring == NULL ||
            str_to_uint32(id->valuestring, (uint32_t *) &id_val) != RETURN_SUCCESS) {
            RTE_LOG(ERR, LOADER, "[LOADER_ERROR]: id must be a string representing an integer!\n");
            exit(EXIT_FAILURE);
        }

        condition_array[id_val] = malloc(sizeof(xfsm_condition_t));
        if (condition_array[id_val] == NULL) {
            perror("[LOADER_ERROR] Error in malloc");
            exit(EXIT_FAILURE);
        }

        if (!cJSON_IsString(opcode) || opcode->valuestring == NULL ||
            str_to_condition_type(opcode->valuestring, &(condition_array[id_val]->condition_type)) != RETURN_SUCCESS) {
            RTE_LOG(ERR, LOADER, "[LOADER_ERROR]: opcode must be a string representing a condition_type!\n");
            exit(EXIT_FAILURE);
        }

        if (get_operand(op0, &(condition_array[id_val]->type1), &(condition_array[id_val]->value1)) == RETURN_ERROR) {
            RTE_LOG(ERR, LOADER, "[LOADER_ERROR]: unable to parse op0 of condition with id %d\n", id_val);
            exit(EXIT_FAILURE);
        }

        if (get_operand(op1, &(condition_array[id_val]->type2), &(condition_array[id_val]->value2)) == RETURN_ERROR) {
            RTE_LOG(ERR, LOADER, "[LOADER_ERROR]: unable to parse op1 of condition with id %d\n", id_val);
            exit(EXIT_FAILURE);
        }

        cJSON_free(id);
        cJSON_free(opcode);
        cJSON_free(op0);
        cJSON_free(op1);
    }

    return condition_array;
}

RETURN_STATUS str_to_action_type(char *str, xfsm_action_types_t *action_type, xfsm_opcodes_t *opcode) {
    *opcode = (xfsm_opcodes_t) 0;
    if (strcmp(str, "XTRA_SUM") == 0)
    {
        *action_type = XFSM_UPDATE;
        *opcode = XFSM_SUM;
    }
    else if (strcmp(str, "XTRA_MINUS") == 0)
    {
        *action_type = XFSM_UPDATE;
        *opcode = XFSM_MINUS;
    }
    else if (strcmp(str, "XTRA_MULTIPLY") == 0)
    {
        *action_type = XFSM_UPDATE;
        *opcode = XFSM_MULTIPLY;
    }
    else if (strcmp(str, "XTRA_DIVIDE") == 0)
    {
        *action_type = XFSM_UPDATE;
        *opcode = XFSM_DIVIDE;
    }
    else if (strcmp(str, "XTRA_MODULO") == 0)
    {
        *action_type = XFSM_UPDATE;
        *opcode = XFSM_MODULO;
    }
    else if (strcmp(str, "XTRA_MAX") == 0)
    {
        *action_type = XFSM_UPDATE;
        *opcode = XFSM_MAX;
    }
    else if (strcmp(str, "XTRA_MIN") == 0)
    {
        *action_type = XFSM_UPDATE;
        *opcode = XFSM_MIN;
    }
    else if (strcmp(str, "XTRA_SENDPKT") == 0)
        *action_type = XFSM_SENDPKT;
    else if (strcmp(str, "XTRA_SETFIELD") == 0)
        *action_type = XFSM_SETFIELD;
    else if (strcmp(str, "XTRA_DELETEINST") == 0)
        *action_type = XFSM_DESTROY;
    else
    {
        return RETURN_ERROR;
    }

    return RETURN_SUCCESS;
}

xfsm_action_t **json_to_actions(cJSON *actions, uint16_t *num) {
    cJSON *action = NULL;
    *num = 0;
    cJSON_ArrayForEach(action, actions) {
        (*num)++;
    }

    xfsm_action_t **parsed_actions = malloc((*num) * sizeof(xfsm_action_t *));
    if (parsed_actions == NULL) {
        perror("[LOADER_ERROR] Error in malloc");
        exit(EXIT_FAILURE);
    }

    int i = 0;
    cJSON_ArrayForEach(action, actions) {
        parsed_actions[i] = malloc(sizeof(xfsm_action_t));
        if (parsed_actions[i] == NULL) {
            perror("[LOADER_ERROR] Error in malloc");
            exit(EXIT_FAILURE);
        }

        cJSON *opcode = cJSON_GetObjectItemCaseSensitive(action, "opcode");
        if (!cJSON_IsString(opcode) || opcode->valuestring == NULL ||
            str_to_action_type(opcode->valuestring, &((parsed_actions[i])->type),
                               &((parsed_actions[i])->opcode)) != RETURN_SUCCESS) {
            RTE_LOG(ERR, LOADER, "[LOADER_ERROR]: opcode must be a string representing an operation_type, found %s!\n",
                    opcode->valuestring);
            exit(EXIT_FAILURE);
        }

        cJSON *op0 = cJSON_GetObjectItemCaseSensitive(action, "op0");
        cJSON *op1 = cJSON_GetObjectItemCaseSensitive(action, "op1");
        cJSON *op2 = cJSON_GetObjectItemCaseSensitive(action, "op2");

        switch ((parsed_actions[i])->type) {
            case XFSM_UPDATE: {
                if (get_operand(op0, &((parsed_actions[i])->type1), &((parsed_actions[i])->value1)) == RETURN_ERROR) {
                    RTE_LOG(ERR, LOADER, "[LOADER_ERROR]: XFSM_UPDATE unable to parse op0!\n");
                    exit(EXIT_FAILURE);
                }
                if (get_operand(op1, &((parsed_actions[i])->type2), &((parsed_actions[i])->value2)) == RETURN_ERROR) {
                    RTE_LOG(ERR, LOADER, "[LOADER_ERROR]: XFSM_UPDATE unable to parse op1!\n");
                    exit(EXIT_FAILURE);
                }

                if (get_out_operand(op2, &((parsed_actions[i])->out_type), &((parsed_actions[i])->output)) == RETURN_ERROR) {
                    RTE_LOG(ERR, LOADER, "[LOADER_ERROR]: XFSM_UPDATE Invalid register name, op2 must be a string representing a registers_type!\n");
                    exit(EXIT_FAILURE);
                }
                break;
            }

            case XFSM_SENDPKT: {
                if (get_operand(op0, &((parsed_actions[i])->type1), &((parsed_actions[i])->value1)) == RETURN_ERROR) {
                    RTE_LOG(ERR, LOADER, "[LOADER_ERROR]: XFSM_UPDATE unable to parse op0!\n");
                    exit(EXIT_FAILURE);
                }
                if (get_operand(op1, &((parsed_actions[i])->type2), &((parsed_actions[i])->value2)) == RETURN_ERROR) {
                    RTE_LOG(ERR, LOADER, "[LOADER_ERROR]: XFSM_UPDATE unable to parse op1!\n");
                    exit(EXIT_FAILURE);
                }
                break;
            }

            case XFSM_SETFIELD: {
                if (get_operand(op0, &((parsed_actions[i])->type1), &((parsed_actions[i])->value1)) == RETURN_ERROR) {
                    RTE_LOG(ERR, LOADER, "[LOADER_ERROR]: XFSM_UPDATE unable to parse op0!\n");
                    exit(EXIT_FAILURE);
                }
                if (get_operand(op1, &((parsed_actions[i])->type2), &((parsed_actions[i])->value2)) == RETURN_ERROR) {
                    RTE_LOG(ERR, LOADER, "[LOADER_ERROR]: XFSM_UPDATE unable to parse op1!\n");
                    exit(EXIT_FAILURE);
                }

                if (get_operand(op2, &((parsed_actions[i])->type3), &((parsed_actions[i])->value3)) == RETURN_ERROR) {
                    RTE_LOG(ERR, LOADER, "[LOADER_ERROR]: XFSM_UPDATE Invalid register name %s, "
                                    "op2 must be a string representing a registers_type!\n", op2->valuestring);
                    exit(EXIT_FAILURE);
                }
                break;
            }

            default:
                RTE_LOG(ERR, LOADER, "[LOADER_ERROR]: invalid opcode\n");
                exit(EXIT_FAILURE);
        }

        i++;

        cJSON_free(opcode);
        cJSON_free(op0);
        cJSON_free(op1);
        cJSON_free(op2);
    }

    return parsed_actions;
}

xfsm_table_entry_t *json_to_table_entry(cJSON *json) {
    cJSON *current_state = cJSON_GetObjectItemCaseSensitive(json, "state");
    cJSON *next_state = cJSON_GetObjectItemCaseSensitive(json, "next_state");
    cJSON *next_stage = cJSON_GetObjectItemCaseSensitive(json, "next_stage");
    cJSON *conditions = cJSON_GetObjectItemCaseSensitive(json, "condition_results");
    cJSON *actions = cJSON_GetObjectItemCaseSensitive(json, "actions");

    xfsm_table_entry_t *table_entry = malloc(sizeof(xfsm_table_entry_t));
    if (table_entry == NULL) {
        perror("[LOADER_ERROR] Error in malloc");
        exit(EXIT_FAILURE);
    }

    if (!cJSON_IsString(current_state) || current_state->valuestring == NULL ||
        str_to_uint32(current_state->valuestring, &(table_entry->current_state)) != RETURN_SUCCESS) {
        RTE_LOG(ERR, LOADER, "[LOADER_ERROR]: current_state must be a string representing an unsigned integer!\n");
        exit(EXIT_FAILURE);
    }

    if (!cJSON_IsString(next_state) || next_state->valuestring == NULL ||
        str_to_uint32(next_state->valuestring, &(table_entry->next_state)) != RETURN_SUCCESS) {
        RTE_LOG(ERR, LOADER, "[LOADER_ERROR]: next_state must be a string representing an unsigned integer!\n");
        exit(EXIT_FAILURE);
    }

    if (!cJSON_IsString(next_stage) || next_stage->valuestring == NULL ||
        str_to_uint8(next_stage->valuestring, &(table_entry->next_stage)) != RETURN_SUCCESS) {
        RTE_LOG(ERR, LOADER, "[LOADER_ERROR]: next_stage must be a string representing an 8 bit unsigned integer!\n");
        exit(EXIT_FAILURE);
    }

    for(int j = 0; j < 16; j++)
    {
        table_entry->results[j] = XFSM_DONT_CARE;
    }

    int i = 0;
    cJSON *condition = NULL;
    cJSON_ArrayForEach(condition, conditions) {
        if(i >= 16) {
            RTE_LOG(ERR, LOADER, "[LOADER_ERROR]: Too many conditions, max number is 16!\n");
            exit(EXIT_FAILURE);
        }
        cJSON *id = cJSON_GetObjectItemCaseSensitive(condition, "id");
        cJSON *result = cJSON_GetObjectItemCaseSensitive(condition, "result");
        uint32_t id_val;
        if (!cJSON_IsString(id) || id->valuestring == NULL ||
            str_to_uint32(id->valuestring, &id_val) != RETURN_SUCCESS) {
            RTE_LOG(ERR, LOADER, "[LOADER_ERROR]: id must be a string representing an integer!\n");
            exit(EXIT_FAILURE);
        }
        if (id_val >= 16) {
            RTE_LOG(ERR, LOADER, "[LOADER_ERROR]: id must be between 0 and the number of conditions!\n");
            exit(EXIT_FAILURE);
        }

        if (!cJSON_IsString(result) || result->valuestring == NULL ||
            str_to_result(result->valuestring, &(table_entry->results[id_val])) != RETURN_SUCCESS) {
            RTE_LOG(ERR, LOADER, "[LOADER_ERROR]: result must be a string representing a condition result!\n");
            exit(EXIT_FAILURE);
        }

        i++;
    }

    xfsm_action_t **ptr = json_to_actions(actions, &(table_entry->nb_actions));
    for(int j = 0; j < table_entry->nb_actions; j++)
        memcpy(&(table_entry->actions[j]), ptr[j], sizeof(xfsm_action_t));

    return table_entry;
}

lookup_key_t *json_to_lookup_key(cJSON *fields) {
    // todo: implement biflow

    lookup_key_t *lookup_key = malloc(sizeof(lookup_key_t));

    cJSON *field = NULL;

    lookup_key->size = 0;
    cJSON_ArrayForEach(field, fields) {
        cJSON *value = cJSON_GetObjectItemCaseSensitive(field, "value");

        if (str_to_header_field(value->valuestring, &lookup_key->fld[lookup_key->size]) != RETURN_SUCCESS) {
            RTE_LOG(ERR, LOADER, "[LOADER_ERROR]: invalid header field!\n");
            exit(EXIT_FAILURE);
        }

        lookup_key->size++;
    }

    return lookup_key;
}

xfsm_table_t *json_to_table(cJSON *table) {
    cJSON *conditions_json = cJSON_GetObjectItemCaseSensitive(table, "conditions");
    uint8_t conditions_number;
    xfsm_condition_t **conditions = json_to_conditions(conditions_json, (int *) &conditions_number);

    cJSON *initial_state_json = cJSON_GetObjectItemCaseSensitive(table, "initial_state");
    uint8_t initial_state;
    if (!cJSON_IsString(initial_state_json) || initial_state_json->valuestring == NULL ||
        str_to_uint8(initial_state_json->valuestring, &initial_state) != RETURN_SUCCESS) {
        RTE_LOG(ERR, LOADER, "[LOADER_ERROR]: initial_state must be a string representing an integer!\n");
        exit(EXIT_FAILURE);
    }

    cJSON *table_rows = cJSON_GetObjectItemCaseSensitive(table, "table_rows");
    cJSON *table_row_json;
    uint8_t table_size = 0;
    cJSON_ArrayForEach(table_row_json, table_rows)
    {
        table_size++;
    }

    xfsm_table_entry_t **table_entries = calloc(table_size, sizeof(xfsm_table_entry_t *));
    if (table_entries == NULL) {
        perror("[LOADER_ERROR] Error in malloc");
        exit(EXIT_FAILURE);
    }

    int i = 0;
    cJSON_ArrayForEach(table_row_json, table_rows)
    {
        table_entries[i] = json_to_table_entry(table_row_json);
        i++;
    }

    cJSON *lookup_key_json = cJSON_GetObjectItemCaseSensitive(table, "flow_key");
    lookup_key_t *lookup_key = json_to_lookup_key(lookup_key_json);

    xfsm_condition_t *conds = malloc(sizeof(xfsm_condition_t) * MAX_CONDITIONS);
    for (int j=0; j<conditions_number; j++)
        memcpy(&conds[j], conditions[j], sizeof(xfsm_condition_t));

    xfsm_table_t *xfsm = xfsm_table_create(table_entries, table_size, initial_state,
            conds, conditions_number, lookup_key, 0);

    /*for(int j=0; j < table_size; j++) {
        for(i=0; i < table_entries[j]->nb_actions; i++) {
            printf("i=%d\n", i);
            free(&(table_entries[j]->actions[i]));
        }
        free(table_entries[j]->actions);
        free(table_entries[j]);
    }*/

    free(table_entries);

    for(int j=0; j < conditions_number; j++) {
        free(conditions[j]);
    }
    free(conditions);

    free(lookup_key);
    cJSON_free(lookup_key_json);
    cJSON_free(conditions_json);
    cJSON_free(initial_state_json);
    cJSON_free(table_rows);

    return xfsm;
}
