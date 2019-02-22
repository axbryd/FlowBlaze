#include <stdio.h>
#include <stdbool.h>
#include "platform.h"
#include "xparameters.h"
#include "string.h"
#include "xiic.h"
#include "xintc.h"
#include "xil_types.h"
#include "platform.h"
#include "mb_interface.h"
#include "xuartlite_l.h"

#include <stddef.h>
#include <string.h>


#define IIC_DEVICE_ID               XPAR_IIC_0_DEVICE_ID
#define INTC_DEVICE_ID              XPAR_INTC_0_DEVICE_ID
#define IIC_INTR_ID                 XPAR_INTC_0_IIC_0_VEC_ID

#define MAX_ARR_FIELD           10

XIic IicInstance;               /* The instance of the IIC device. */
XIntc InterruptController;      /* The instance of the Interrupt Controller. */


//test
#define REGTEST0_ADDR    0x80000008
#define TIMER_ADDR       0x8000000C

void xge_test_enable(void);
void sfpReadInfo(u8 i);
int config_SI5324(void);

int IicInit(XIic*);
int SetupInterruptSystem(XIic*);
int IicInitPost(XIic*);
 

// FlowBlaze addresses and codes
#include <unistd.h>
#include "address_flowblaze.h"
#include "address_debug.h"
#include "code_flowblaze.h"

// methods to configure flowblaze-fpga components
#include "tool_flowblaze.h"
//#include "elfgen.h" used to provide a pre-compiled application




//#define DEBUG


#ifdef DEBUG
#define debug_print(fmt, args...)    xil_printf(fmt, ## args)
#else
#define debug_print(fmt, args...)    /* Don't do anything in release builds */
#endif 

void getline(char * msg) {
        char ch;
        while (((ch = getchar()) != 0x0D) && (ch != EOF))
	   		{
			*msg++=ch;	
			debug_print("--> %c %d \n\r",ch,ch);
			}		
        msg=0;
}

// Reduced version of scanf (%d, %x, %c, %n are supported)
// %d dec integer (E.g.: 12)
// %x hex integer (E.g.: 0xa0)
// %b bin integer (E.g.: b1010100010)
// %n hex, de or bin integer (e.g: 12, 0xa0, b1010100010)
// %c any character
//
/*
int rsscanf(const char* str, const char* format, ...)
{
        va_list ap;
        int value, tmp;
        int count;
        int pos;
        char neg, fmt_code;

        va_start(ap, format);

        for (count = 0; *format != 0 && *str != 0; format++, str++)
        {
                while (*format == ' ' && *format != 0)
                        format++;
                if (*format == 0)
                        break;

                while (*str == ' ' && *str != 0)
                        str++;
                if (*str == 0)
                        break;

                if (*format == '%')
                {
                        format++;
                        if (*format == 'n')
                        {
                if (str[0] == '0' && (str[1] == 'x' || str[1] == 'X'))
                {
                    fmt_code = 'x';
                    str += 2;
                }
                else
                if (str[0] == 'b')
                {
                    fmt_code = 'b';
                    str++;
                }
                else
                    fmt_code = 'd';
                        }
                        else
                                fmt_code = *format;

                        switch (fmt_code)
                        {

                        case 'd':
                                if (*str == '-')
                                {
                                        neg = 1;
                                        str++;
                                }
                                else
                                        neg = 0;
                                for (value = 0, pos = 0; *str != 0; str++, pos++)
                                {
                                        if ('0' <= *str && *str <= '9')
                                                value = value*10 + (int)(*str - '0');
                                        else
                                                break;
                                }
                                if (pos == 0)
                                        return count;
                                *(va_arg(ap, int*)) = neg ? -value : value;
                                count++;
                                break;

                        case 'c':
                                *(va_arg(ap, char*)) = *str;
                                count++;
                                break;

                        default:
                                return count;
                        }
                }
                else
                {
                        if (*format != *str)
                                break;
                }
        }

        va_end(ap);

        return count;
}
*/
int rsscanf(const char* str, const char* format, ...)
{
        va_list ap;
        int value, tmp;
        int count;
        int pos;
        char neg, fmt_code;

        va_start(ap, format);

        for (count = 0; *format != 0 && *str != 0; format++, str++)
        {
                while (*format == ' ' && *format != 0)
                        format++;
                if (*format == 0)
                        break;

                while (*str == ' ' && *str != 0)
                        str++;
                if (*str == 0)
                        break;

                if (*format == '%')
                {
                        format++;
                        if (*format == 'n')
                        {
                if (str[0] == '0' && (str[1] == 'x' || str[1] == 'X'))
                {
                    fmt_code = 'x';
                    str += 2;
                }
                else
                if (str[0] == 'b')
                {
                    fmt_code = 'b';
                    str++;
                }
                else
                    fmt_code = 'd';
                        }
                        else
                                fmt_code = *format;

                        switch (fmt_code)
                        {
                        case 'x':
                        case 'X':
                                for (value = 0, pos = 0; *str != 0; str++, pos++)
                                {
                                        if ('0' <= *str && *str <= '9')
                                                tmp = *str - '0';
                                        else
                                        if ('a' <= *str && *str <= 'f')
                                                tmp = *str - 'a' + 10;
                                        else
                                        if ('A' <= *str && *str <= 'F')
                                                tmp = *str - 'A' + 10;
                                        else
                                                break;

                                        value *= 16;
                                        value += tmp;
                                }
                                if (pos == 0)
                                        return count;
                                *(va_arg(ap, int*)) = value;
                                count++;
                                break;

            case 'b':
                                for (value = 0, pos = 0; *str != 0; str++, pos++)
                                {
                                        if (*str != '0' && *str != '1')
                        break;
                                        value *= 2;
                                        value += *str - '0';
                                }
                                if (pos == 0)
                                        return count;
                                *(va_arg(ap, int*)) = value;
                                count++;
                                break;

                        case 'd':
                                if (*str == '-')
                                {
                                        neg = 1;
                                        str++;
                                }
                                else
                                        neg = 0;
                                for (value = 0, pos = 0; *str != 0; str++, pos++)
                                {
                                        if ('0' <= *str && *str <= '9')
                                                value = value*10 + (int)(*str - '0');
                                        else
                                                break;
                                }
                                if (pos == 0)
                                        return count;
                                *(va_arg(ap, int*)) = neg ? -value : value;
                                count++;
                                break;

                        case 'c':
                                *(va_arg(ap, char*)) = *str;
                                count++;
                                break;

                        default:
                                return count;
                        }
                }
                else
                {
                        if (*format != *str)
                                break;
                }
        }

        va_end(ap);

        return count;
}


int main(){

        xil_printf("============ BITSTREAM  ============\n\r");
	// display bitstream version
        unsigned int data=0;
        data=regread(0x80000004);
        int day=(data & 4160749568)>>27;
        int month=(data & 125829120)>>23;
        int year=2000;
        year +=(data & 8257536)>>17;

        int hour=(data & 126976)>>12;
        int min=(data & 4032)>>6;
        int sec=(data & 63);
        xil_printf("%02d/%02d/%d (%02d:%02d:%02d)\n\r",day,month,year,hour,min,sec);

	// 'base' is an offset needs to be added to component baseaddresses to configure different flowblaze stages if more than one are present in the bitstream
	int stage=1, base=FLOWBLAZE1_BASEADDR-FLOWBLAZE2_BASEADDR;
	base=0;stage=0;
	int key_tcam[5]={0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF};
	int mask_tcam[5]={0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF};

	//int pipealu[9]={0,0,0,0,0,0,0,0,0};


	// reset REGTEST0 - it is a register in which you can force action to all packets 
	regwrite(REGTEST0_ADDR, 0);
//      regwrite(REGTEST0_ADDR, 0x55);

	bool silent=false;
	while(1) {
		if (!silent) {
			xil_printf("=========================================\n\r");
			xil_printf("================   DONE   ===============\n\r");
			xil_printf("========= FlowBlaze stage: %d ======== \n\r",stage);
			xil_printf("============= Select: \n\r");
			xil_printf("     0: exit\n\r");
			xil_printf("Configuration: \n\r");
			xil_printf("     1: change FlowBlaze stage\n\r");
			xil_printf("     2: send to - REGTEST0 \n\r");
			xil_printf("Debug: \n\r");
			xil_printf("     3: count HT-entries\n\r");
			xil_printf("     4: search on hash table\n\r");
			xil_printf("     5: scan RAM\n\r");
			xil_printf("     6: scan ETH-port\n\r");
			xil_printf("Application: \n\r");
			xil_printf("     7: transparent mode\n\r");
			//xil_printf("     10: Compiled application \n\r");
			xil_printf("     97: Silent Write command\n\r");
			xil_printf("     98: Read command\n\r");
			xil_printf("     99: Write command\n\r");
		}
                silent = false;
		int option=0,i=0,tmp=0,ht_counter=0;
		int opcode=0;
		char msg[256];
		getline(msg);
		unsigned int value=0, addr=0;
		//rsscanf(msg,"%d\n",&opcode);
		rsscanf(msg,"%d 0x%x 0x%x\n",&opcode,&addr,&value);
		//xil_printf("msg is: %d 0x%x 0x%x \n\r",opcode,addr,value);

                //svuotare buffer scanf
		//while ( getchar() != '\n' );
		
		//xil_printf("=========================================\n\r");

		//PAUSE is a signal used to disable pipeline
		//pipeline needs to be stopped if you are modifying internal flowblaze componets!

		//disabling pipeline
		regwrite(PAUSE, 1);
		regwrite(FLOWBLAZE1_BASEADDR-FLOWBLAZE2_BASEADDR+PAUSE, 1);
		usleep(20000);

                switch(opcode){
/**
===============================Configuration:
**/
			case 0:
				//enabling pipeline
				regwrite(PAUSE, 0);
				regwrite(FLOWBLAZE1_BASEADDR-FLOWBLAZE2_BASEADDR+PAUSE, 0);

				return 0;

			case 1:
				stage = (stage==1) ? 2:1; 

				base = (stage==1) ? (FLOWBLAZE1_BASEADDR-FLOWBLAZE2_BASEADDR) : 0;

				break;

			case 2:
				xil_printf("=========================\n\r");
				xil_printf("=========SEND TO=========\n\r");
				xil_printf("Select destination port:\n\r");
				xil_printf("0: DROP\n\r");
				xil_printf("1: Port_01\n\r");
				xil_printf("2: Port_04\n\r");
				xil_printf("3: Port_10\n\r");
				xil_printf("4: Port_40\n\r");
				xil_printf("5: Flooding\n\r");
				xil_printf("6: Reset\n\r");
				getline(msg);
				rsscanf(msg,"%d",&option);

				switch(option){
					case 0:
						regwrite(REGTEST0_ADDR, 0x100);
						break;

					case 1:
						regwrite(REGTEST0_ADDR, IDPORT_01);
						break;

					case 2:
						regwrite(REGTEST0_ADDR, IDPORT_02);
						break;

					case 3:
						regwrite(REGTEST0_ADDR, IDPORT_03);
						break;
	
					case 4:
						regwrite(REGTEST0_ADDR, IDPORT_04);
						break;
	
					case 5:
						regwrite(REGTEST0_ADDR, IDFLOODING);
						break;

					case 6:
						regwrite(REGTEST0_ADDR, 0);
						break;
				};




				break;


/**
===============================Debug:
**/

                        case  3:
                                xil_printf("searching on HT - H \n\r");
 				
				ht_counter=0;
                                xil_printf("-----------------------------------------------HT0-----------------------------------------------\n\r");
                                ht_counter += searchHT_hide(HT0_1K_BASEADDR + base, HT_1K);
                                xil_printf("-----------------------------------------------HT1-----------------------------------------------\n\r");
                                ht_counter += searchHT_hide(HT1_1K_BASEADDR + base,HT_1K);
                                xil_printf("-----------------------------------------------HT2-----------------------------------------------\n\r");
                                ht_counter += searchHT_hide(HT2_1K_BASEADDR + base,HT_1K);
                                xil_printf("-----------------------------------------------HT3-----------------------------------------------\n\r");
                                ht_counter += searchHT_hide(HT3_1K_BASEADDR + base,HT_1K);
                                xil_printf(" \n\r");
				
				xil_printf("Total count: %d  \n\r", ht_counter);

				break;


                        case  4:
                                xil_printf("searching on HT\n\r");
 				
				ht_counter=0;
                                xil_printf("-----------------------------------------------HT0-----------------------------------------------\n\r");
                                ht_counter += xil_searchHT(HT0_1K_BASEADDR + base, HT_1K);
                                xil_printf("-----------------------------------------------HT1-----------------------------------------------\n\r");
                                ht_counter += xil_searchHT(HT1_1K_BASEADDR + base,HT_1K);
                                xil_printf("-----------------------------------------------HT2-----------------------------------------------\n\r");
                                ht_counter += xil_searchHT(HT2_1K_BASEADDR + base,HT_1K);
                                xil_printf("-----------------------------------------------HT3-----------------------------------------------\n\r");
                                ht_counter += xil_searchHT(HT3_1K_BASEADDR + base,HT_1K);
                                xil_printf(" \n\r");
				
				xil_printf("Total count: %d  \n\r", ht_counter);

                                break;



                        case 5:
				xil_printf("====|===RAM1===||===RAM2===||===RAM3===||===RAM4===|| \n\r");
				for(i=0; i<32;   i++){
					xil_printf("%02d: |",i);
					option=regread(RAM1_BASEADDR + base +4*i);
					xil_printf(" %08X ||",option);

					option=regread(RAM2_BASEADDR + base +4*i);
					xil_printf(" %08X ||",option);

					option=regread(RAM3_BASEADDR + base +4*i);
					xil_printf(" %08X ||",option);

					option=regread(RAM4_BASEADDR + base +4*i);
					xil_printf(" %08X ||",option);
					
					xil_printf("\n\r");
				}

				break;

			case 6:
				//This option monitors packets arriving and exiting NetFpga physical interfaces
				xil_printf("=======ETH========\n\r");

				xil_printf("INTERFACEID |     PKTIN    |     PKTOUT  \n\r");
				
				int pktin=0,pktout=0;

				tmp=SUME_NF_10G_INTERFACE0_BASEADDR;
				for(i=0; i<4 ;i++){
					option=regread(tmp + SUME_NF_10G_INTERFACE_SHARED_0_INTERFACEID_OFFSET);
					xil_printf("        %02x  |", option);
					option=regread(tmp + SUME_NF_10G_INTERFACE_SHARED_0_PKTIN_OFFSET);
					xil_printf("  %10d  |", option);
					pktin+=option;
					option=regread(tmp + SUME_NF_10G_INTERFACE_SHARED_0_PKTOUT_OFFSET);
					xil_printf("  %10d  |     ", option);
					pktout+=option;
					xil_printf("\n\r");

					tmp+=0x00010000;
				}
				
				xil_printf("---------------------------------------------\n\r");
				xil_printf("       TOT  |");
				xil_printf("  %10d  |", pktin);
				xil_printf("  %10d  |     ", pktout);
				xil_printf("\n\r");



				break;


/**
===============================Application:
**/



			case 7:

				xil_printf("============================== \n\r");
				xil_printf("=======Trasparent Mode======== \n\r");
				xil_printf("============================== \n\r");


				//setting extractor
				regwrite(LOOKUP_ADDR + base ,0);
				regwrite(UPDATE_ADDR + base , 0);
				regwrite(SEL_UPDATE_ADDR + base , 0);
				regwrite(FSM_SCOPE_ADDR + base , 0x2D);

				//setting key extractor mask
				regwrite(LOOKUPMASK_ADDR + base , 0xFFFFFFFF);
				regwrite(LOOKUPMASK_ADDR + base +4, 0xFFFFFFFF);
				regwrite(LOOKUPMASK_ADDR + base +8, 0xFFFFFFFF);
				regwrite(LOOKUPMASK_ADDR + base +12, 0xFFFFFFFF);
				regwrite(UPDATEMASK_ADDR + base , 0xFFFFFFFF);
				regwrite(UPDATEMASK_ADDR + base +4, 0xFFFFFFFF);
				regwrite(UPDATEMASK_ADDR + base +8, 0xFFFFFFFF);
				regwrite(UPDATEMASK_ADDR + base +12, 0xFFFFFFFF);

				//setting off-len
				regwrite(OFF_LEN1 + base, 0xE8); //timestamp(0x28)
				regwrite(OFF_LEN2 + base, 0xE8); //timestamp(0x28)
				regwrite(OFF_LEN3 + base, 0x50); //off=16|len=2

				//Global register
				regwrite(GR00 + base, 0x1); 
				regwrite(GR01 + base, 0x2); 

				
				scan_extractor(base);


				for(i=0; i<5; i++){
					key_tcam[i]=0xFFFFFFFF;
					mask_tcam[i]=0xFFFFFFFF;
				}

				//setting first tcam1 row as missing-flow
				set_tcamram1(base, 0, key_tcam, mask_tcam, 0x000000FF);

				//setting tcam2 mask(it is the same on all entries)
				mask_tcam[0]=0xFFFFFFFF;
				mask_tcam[1]=0xFFFFFF00;
				
				//setting tcam2
				key_tcam[0]=0x000000FF;	
				key_tcam[1]=IDPORT_01 ;	
				set_tcamram2(base, 0, key_tcam, mask_tcam, 0x0000000A, (PORT_01 + HT_REMOVE), 0);

				key_tcam[1]=IDPORT_02 ;	
				set_tcamram2(base, 1, key_tcam, mask_tcam, 0x0000000A, (PORT_02 + HT_REMOVE), 0);

				key_tcam[1]=IDPORT_03;	
				set_tcamram2(base, 2, key_tcam, mask_tcam, 0x0000000A, (PORT_03 + HT_REMOVE), 0);

				key_tcam[1]=IDPORT_04;	
				set_tcamram2(base, 3, key_tcam, mask_tcam, 0x0000000A, (PORT_04 + HT_REMOVE), 0);

				key_tcam[1]=(IDPORT_01 + IDPORT_02);	
				set_tcamram2(base, 4, key_tcam, mask_tcam, 0x0000000A, ((PORT_01 + PORT_02) + HT_REMOVE), 0);

				key_tcam[1]=(IDPORT_01 + IDPORT_03);	
				set_tcamram2(base, 5, key_tcam, mask_tcam, 0x0000000A, ((PORT_01 + PORT_03) + HT_REMOVE), 0);

				key_tcam[1]=(IDPORT_02 + IDPORT_04);	
				set_tcamram2(base, 6, key_tcam, mask_tcam, 0x0000000A, ((PORT_02 + PORT_04) + HT_REMOVE), 0);

				key_tcam[1]=(IDPORT_03 + IDPORT_04);	
				set_tcamram2(base, 7, key_tcam, mask_tcam, 0x0000000A, ((PORT_03 + PORT_04) + HT_REMOVE), 0);

				key_tcam[1]=IDFLOODING;	
				set_tcamram2(base, 8, key_tcam, mask_tcam, 0x0000000A, (FLOODING + HT_REMOVE), 0);


				break;

			//case 10:
			//	compiled_app();
			//	break;
			case 97:
				regwrite(addr,value);
				silent=true;
                                break;
			case 98:
				data=regread(addr);
				xil_printf("addr 0x%x : 0x%x %d ---\n\r",addr,data,data);
				break;
			case 99:
				regwrite(addr,value);
				break;
			default:
				xil_printf("Error! Option not available\n\r");

		}

		//enabling pipeline
		regwrite(PAUSE, 0);
		regwrite(FLOWBLAZE1_BASEADDR-FLOWBLAZE2_BASEADDR+PAUSE, 0);


	}

    return 0;
}
