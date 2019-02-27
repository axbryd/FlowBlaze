APP = rss-test
RTE_TARGET ?= x86_64-native-linuxapp-gcc

SRCS-y := rss-test.c ../xfsm.c ../pkt-parser.c

include $(RTE_SDK)/mk/rte.vars.mk

CFLAGS += -O3
CFLAGS += $(WERROR_FLAGS)

include $(RTE_SDK)/mk/rte.extapp.mk
