#include "../xfsm.h"//
// Created by angelo on 23/01/19.
//

#ifndef FLOWBLAZE_DPDK_TABLE_DEF_H
#define FLOWBLAZE_DPDK_TABLE_DEF_H

#include "../xfsm.h"

// conditions
#define NUM_CONDITION 2
#define NUM_ENTRIES 6

xfsm_condition_t conditions[NUM_CONDITION] = {
        {.type1=XFSM_REGISTER, .type2=XFSM_CONSTANT, .value1=0, .value2=5, .condition_type=XFSM_GREATER_EQ},
        {.type1=XFSM_HDR_FIELD, .type2=XFSM_CONSTANT, .value1=XFSM_IN_PORT, .value2=1, .condition_type=XFSM_EQUAL}
                                             };

//entries
xfsm_table_entry_t e0 = {.current_state=0, .nb_actions=2, .next_state=1,
                         .results={XFSM_DONT_CARE, XFSM_FALSE},
                         .actions={
                                        {.type1=XFSM_REGISTER, .type2=XFSM_CONSTANT, .type3=XFSM_REGISTER,  // R0++
                                         .value1=0,            .value2=1,            .value3=0,
                                         .opcode=XFSM_SUM, .output=0},
                                        {.type1=XFSM_REGISTER, .type2=XFSM_CONSTANT, .type3=XFSM_REGISTER,  // R0++
                                            .value1=0,            .value2=1,            .value3=0,
                                            .opcode=XFSM_SUM, .output=0, .type=XFSM_SENDPKT}
                                  }
                        };

xfsm_table_entry_t e00 = {.current_state=0, .nb_actions=2, .next_state=1,
    .results={XFSM_DONT_CARE, XFSM_TRUE},
    .actions={
        {.type1=XFSM_REGISTER, .type2=XFSM_CONSTANT, .type3=XFSM_REGISTER,  // R0++
            .value1=0,            .value2=1,            .value3=0,
            .opcode=XFSM_SUM, .output=0},
        {.type1=XFSM_REGISTER, .type2=XFSM_CONSTANT, .type3=XFSM_REGISTER,  // R0++
            .value1=0,            .value2=2,            .value3=0,
            .opcode=XFSM_SUM, .output=0, .type=XFSM_SENDPKT}
    }
};


// 2

xfsm_table_entry_t e1 = {.current_state=1, .nb_actions=2, .next_state=1,
                         .results={XFSM_FALSE, XFSM_FALSE},
                         .actions={
                                   {.type1=XFSM_REGISTER, .type2=XFSM_CONSTANT, .type3=XFSM_REGISTER,  // R0++
                                    .value1=0,            .value2=1,            .value3=0,
                                    .opcode=XFSM_SUM, .output=0},
                                   {.type1=XFSM_REGISTER, .type2=XFSM_CONSTANT, .type3=XFSM_REGISTER,  // R0++
                                       .value1=0,            .value2=1,            .value3=0,
                                       .opcode=XFSM_SUM, .output=0, .type=XFSM_SENDPKT}
                                  }
                        };

xfsm_table_entry_t e11 = {.current_state=1, .nb_actions=2, .next_state=1,
    .results={XFSM_FALSE, XFSM_TRUE},
    .actions={
        {.type1=XFSM_REGISTER, .type2=XFSM_CONSTANT, .type3=XFSM_REGISTER,  // R0++
            .value1=0,            .value2=1,            .value3=0,
            .opcode=XFSM_SUM, .output=0},
        {.type1=XFSM_REGISTER, .type2=XFSM_CONSTANT, .type3=XFSM_REGISTER,  // R0++
            .value1=0,            .value2=2,            .value3=0,
            .opcode=XFSM_SUM, .output=0, .type=XFSM_SENDPKT}
    }
};

// 3

xfsm_table_entry_t e2 = {.current_state=1, .nb_actions=2, .next_state=1,
                         .results={XFSM_TRUE, XFSM_FALSE},
                         .actions={
                                    {.type1=XFSM_REGISTER, .type2=XFSM_CONSTANT, .type3=XFSM_REGISTER,  //R0 -= 5
                                     .value1=0,            .value2=5,            .value3=0,
                                     .opcode=XFSM_MINUS, .output=0},
                                    {.type1=XFSM_REGISTER, .type2=XFSM_CONSTANT, .type3=XFSM_REGISTER,  // R0++
                                        .value1=0,            .value2=1,            .value3=0,
                                        .opcode=XFSM_SUM, .output=0, .type=XFSM_SENDPKT}
                                  }
                        };
xfsm_table_entry_t e22 = {.current_state=1, .nb_actions=2, .next_state=1,
    .results={XFSM_TRUE, XFSM_TRUE},
    .actions={
        {.type1=XFSM_REGISTER, .type2=XFSM_CONSTANT, .type3=XFSM_REGISTER,  //R0 -= 5
            .value1=0,            .value2=5,            .value3=0,
            .opcode=XFSM_MINUS, .output=0},
        {.type1=XFSM_REGISTER, .type2=XFSM_CONSTANT, .type3=XFSM_REGISTER,  // R0++
            .value1=0,            .value2=2,            .value3=0,
            .opcode=XFSM_SUM, .output=0, .type=XFSM_SENDPKT}
    }
};


xfsm_table_entry_t *entries[NUM_ENTRIES] = {&e0, &e1, &e2, &e00, &e11, &e22};

// flow key

lookup_key_t lookup_key = {.size=2, .biflow_no=0, .fld={IPv4_DST, IPv4_SRC}};

#endif //FLOWBLAZE_DPDK_TABLE_DEF_H
