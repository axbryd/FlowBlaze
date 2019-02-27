ifeq ($(RTE_SDK),)
$(error "Please define RTE_SDK environment variable")
endif

APP=flowblaze

RTE_TARGET ?= x86_64-native-linuxapp-gcc

SRCS-y := loader.c xfsm.c pkt-parser.c cJSON.c flowblaze.c

include $(RTE_SDK)/mk/rte.vars.mk

CFLAGS += -g
CFLAGS += $(WERROR_FLAGS)

include $(RTE_SDK)/mk/rte.extapp.mk

