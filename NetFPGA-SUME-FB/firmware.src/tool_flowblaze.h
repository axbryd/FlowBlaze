#ifndef __TOOL_FLOWBLAZE_H_
#define __TOOL_FLOWBLAZE_H_

#include "address_flowblaze.h"
#include "address_debug.h"
#include "code_flowblaze.h"

#include <stdio.h>
#include "platform.h"
#include "xparameters.h"
#include "string.h"
#include "xiic.h"
#include "xintc.h"
#include "xil_types.h"
#include "platform.h"
#include "mb_interface.h"
#include "xuartlite_l.h"



//#include "sume_reg.h"



unsigned int
regread(unsigned int addr)
{
        unsigned int data=Xil_In32(addr);
	return (data);
        data=Xil_In32(REGTEST0_ADDR);


}

int
regwrite(unsigned int addr, unsigned int val)
{
        Xil_Out32(addr, val);
	return (0);
}


void set_tcamram2(int flowblaze_baseaddr, int row, int* key, int* mask, int next_state, int action, int update){
        int i=0,value=0;

        for(i=0; i < 8; i++){
		if(i < 5)
                	value=key[i];
		else
                	value=0xFFFFFFFF;
                regwrite(flowblaze_baseaddr + TCAM2_BASEADDR + 64*row +4*i, value);
        }

        for(i=0; i<8; i++){
		if(i < 5)
                	value=mask[i];
		else
			value=0xFFFFFFFF;
                regwrite(flowblaze_baseaddr + TCAM2_MASK_BASEADDR + 64*row +4*i, value);
        }

        value=next_state;
        regwrite(flowblaze_baseaddr + RAM2_BASEADDR+4*row, value);

        value=action;
        regwrite(flowblaze_baseaddr + RAM3_BASEADDR+4*row, value);

        value=update;
        regwrite(flowblaze_baseaddr + RAM4_BASEADDR+4*row, value);

}


void set_tcamram1(int flowblaze_baseaddr, int row, int* key, int* mask, int present_state){
	int i=0,value=0;

	for(i=0; i<8; i++){
		if(i < 4)
                        value=key[i];
                else
                        value=0xFFFFFFFF;
		regwrite(flowblaze_baseaddr + TCAM1_BASEADDR + 64*row +4*i, value);
	}

	for(i=0; i<8; i++){
		if(i < 4)
                        value=mask[i];
                else
                        value=0xFFFFFFFF;
		regwrite(flowblaze_baseaddr + TCAM1_MASK_BASEADDR + 64*row +4*i, value);
	}

	value=present_state;
	regwrite(flowblaze_baseaddr + RAM1_BASEADDR+4*row, value);

}

void write_pipealu(int flowblaze_baseaddr, int row, int* pipealu){
	int i=0, value=0;

        for(i=0; i<16; i++){
                if(i < 9)
                        value=pipealu[i];
                else
                        value=0;
                regwrite(flowblaze_baseaddr + RAM_PIPEALU_BASEADDR + 64*row +4*i, value);
        }

}


void testAddress(unsigned int address, char* s, int g){
        int valore=0;
        int rand = 0xFFFFFFFF-g;

        printf("========%s(%X)======\n\r",s,address);

        valore=regread(address);
        printf(" %X  \n\r",valore);

        valore=0;
        regwrite(address, rand);
        valore=regread(address);

        printf("|%X \n\r",rand);
        printf(" %X \n\r",valore);


}


void testAddressLine(unsigned int address, unsigned int len, char* s){
        int valore=0;
        int rand=0;
        int i=0;

//leggo
        printf("========%s(%X)======\n\r",s,address);
        for(i=0; i<len; i++){
                valore=regread(address+4*i);
                printf("%08X ",valore);
        }
        printf("\n\r");

        regread(REGTEST0_ADDR);

//scrivo
        for(i=0; i<len; i++){
                rand=(0xFFFFFFFF-i) ;
                printf("%08X ",rand);
                regwrite(address+4*i, rand);
        }
        printf("\n\r");

//rileggo
        for(i=0; i<len; i++){
                valore=regread(address+4*i);
                printf("%08X ",valore);
        }
        printf("\n\r");
        regread(REGTEST0_ADDR);
}


void scan_extractor(int flowblaze_baseaddr){
	printf("base: %08X \n\r",flowblaze_baseaddr);
	int i=0;
	int option=regread(LOOKUP_ADDR + flowblaze_baseaddr);
	printf("LookUp: %08X \n\r",option);

	printf("Lookup Mask:");
	for(i=0; i<4; i++){
		option=regread(LOOKUPMASK_ADDR + flowblaze_baseaddr + i*4);
		printf(" %08X |",option);
	}
	printf("\n\r");


	option=regread(UPDATE_ADDR + flowblaze_baseaddr);
	printf("Update: %08X \n\r",option);

	option=regread(SEL_UPDATE_ADDR + flowblaze_baseaddr);
	printf("Sel_Update: %08X \n\r",option);

	printf("Update Mask:");
	for(i=0; i<4; i++){
		option=regread(UPDATEMASK_ADDR + flowblaze_baseaddr + i*4);
		printf(" %08X |",option);
	}
	printf("\n\r");


	option=regread(FSM_SCOPE_ADDR + flowblaze_baseaddr);
	printf("FSM_scope: %08X \n\r",option);



	printf("=====Scan Off-Len=====\n\r");
	for(i=0; i<3; i++){
		option=regread(flowblaze_baseaddr + OFF_LEN1 + i*16);
		printf("Off-Len%d: %08X \n\r",i+1,option);
	}


	printf("=====GR=====\n\r");
	for(i=0; i<3; i++){
		option=regread(flowblaze_baseaddr + GR00 + i *4);
		printf("GR%02d: %08X \n\r",i,option);
	}

}



/*
Search HT's row different from null row. Searching start from the "address" to "lenght" (number of row) later
*/

int searchHT_hide(unsigned int address, unsigned int lenght){
        int i=0, j=0, ok=0, counter=0;
        for(i=0 ; i<lenght; i++){
                //search line not null
                for(j=0; j<8; j++){
                        int value=regread(address+4*j+32*i);
                        if(value!=0)
                                ok++;
                }
                regread(REGTEST0_ADDR);

                if(ok>0){
			counter++;
/*
                        printf("%X:   ",address+i*32);
                        for(j=0; j<8; j++){
                                int value=regread(address+4*j+32*i);
				printf("%08X ",value);
				if(j==3)
					printf(" | ");
					
                        }
                        printf("\n\r");
 */
		}

                ok=0;
                regread(REGTEST0_ADDR);
        }
	printf("Valid entries: %d  \n\r", counter);
	return counter;
}

/*
Search HT's row different from null row. Searching start from the "address" to "lenght" (number of row) later
*/

int searchHT(unsigned int address, unsigned int lenght){
        int i=0, j=0, ok=0, counter=0;
        for(i=0 ; i<lenght; i++){
                //search line not null
                for(j=0; j<8; j++){
                        int value=regread(address+4*j+32*i);
                        if(value!=0)
                                ok++;
                }
                regread(REGTEST0_ADDR);

                if(ok>0){
			counter++;
                        printf("%X:   ",address+i*32);
                        for(j=0; j<8; j++){
                                int value=regread(address+4*j+32*i);
                                printf("%08X ",value);
				if(j==3)
					printf(" | ");
					
                        }
                        printf("\n\r");
                }

                ok=0;
                regread(REGTEST0_ADDR);
        }
	printf("Valid entries: %d  \n\r", counter);
	return counter;
}

int xil_searchHT(unsigned int address, unsigned int lenght){
        int i=0, j=0, ok=0, counter=0;
        for(i=0 ; i<lenght; i++){
                //search line not null
                for(j=0; j<8; j++){
                        int value=regread(address+4*j+32*i);
                        if(value!=0)
                                ok++;
                }
                regread(REGTEST0_ADDR);

                if(ok>0){
			counter++;
                        xil_printf("%X:   ",address+i*32);
                        for(j=0; j<8; j++){
                                int value=regread(address+4*j+32*i);
                                xil_printf("%08X ",value);
				if(j==3)
					xil_printf(" | ");
					
                        }
                        xil_printf("\n\r");
                }

                ok=0;
                regread(REGTEST0_ADDR);
        }
	xil_printf("Valid entries: %d  \n\r", counter);
	return counter;
}



/**
Delete "length" (number of) row starting from "address" on HT
**/

void resetHT(unsigned int address, unsigned int lenght){
        int i=0;
        int j=0;
        for(i=0 ; i<lenght; i++){
                for(j=0; j<8; j++){
                        regwrite(address+4*j+32*i,0);
                }

                regread(REGTEST0_ADDR);
        }

}

/**
Implement the function that use ControlFlag to delete timeouted flow
**/

void controlHT(unsigned int address){

        int i=0, j=0, ok=0;
        for(i=0 ; i<1024; i++){
                int value[9];
                //check the controlFlag(if it is set to 1, it will remove line)
                for(j=0; j<8; j++){
                        value[j]=regread(address+4*j+32*i);
			if(j==7){
				value[8]= (value[7]>>30) & 3;

				if(value[8] == 1)
					resetHT(address+32*i,1);
				else if(value[8] == 3)
					ok=10;

			}
                }
                regread(REGTEST0_ADDR);

                if(ok>2){
			value[7]&= 0x4FFFFFFF;
			//change controlFlag to 1
                        printf("%X:   ", address+i*32);
                        for(j=0; j<8; j++){
                                regwrite(address+4*j+32*i, value[j]);
                                printf("%08X ", value[j]);
                        }
                        printf("\n\r");

	                regread(REGTEST0_ADDR);

                        printf("%X:   ", address+i*32);
                        for(j=0; j<8; j++){
                                int value=regread(address+4*j+32*i);
                                printf("%08X ",value);
                        }
                        printf("\n\r");
                }

                ok=0;
                regread(REGTEST0_ADDR);
        }

}


#endif
