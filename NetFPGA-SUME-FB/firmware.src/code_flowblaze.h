#ifndef code_flowblaze_h   
#define code_flowblaze_h


//===HT====
#define HT_1K	1024
#define HT_8K	8192


//ETH
#define IDPORT_01	 0x01
#define IDPORT_02	 0x04
#define IDPORT_03	 0x10
#define IDPORT_04	 0x40
#define IDFLOODING	 0x55

//===TCAM2===
//HT-ACTION
#define HT_INSERT	0xFF00
#define HT_UPDATE	0xFF01
#define HT_REMOVE	0xFFFF

//FORWARDING ACTION
#define PORT_01		 0x01000000
#define PORT_02		 0x04000000
#define PORT_03		 0x10000000
#define PORT_04		 0x40000000
#define FLOODING	 0x55000000
#define DROP		 0x00000000

#define MODIFY_FIELD	 0x000B0000
#define MODIFY_FIELD_R	 0x001B0000


#endif
