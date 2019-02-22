/*
 * Copyright (c) 2015 Digilent Inc.
 * Copyright (c) 2015 Tinghui Wang (Steve)
 * All rights reserved.
 *
 * File:
 *     sw/embedded/src/nf_sume_10g_loopback/iic_sfp.c
 *
 * Project:
 *     acceptance_test
 *
 * Author:
 *     Tinghui Wang (Steve)
 *
 * Description:
 *     Iic codes to read SFP+ module information
 *
 * This software was developed by the University of Cambridge Computer Laboratory
 * under EPSRC INTERNET Project EP/H040536/1, National Science Foundation under Grant No. CNS-0855268,
 * and Defense Advanced Research Projects Agency (DARPA) and Air Force Research Laboratory (AFRL),
 * under contract FA8750-11-C-0249.
 * 
 * @NETFPGA_LICENSE_HEADER_START@
 * 
 * Licensed to NetFPGA Open Systems C.I.C. (NetFPGA) under one or more contributor
 * license agreements. See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership. NetFPGA licenses this
 * file to you under the NetFPGA Hardware-Software License, Version 1.0 (the
 * "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at:
 * 
 * http:#www.<future link here>
 * 
 * Unless required by applicable law or agreed to in writing, Work distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * @NETFPGA_LICENSE_HEADER_END@
 *
 */

#include "iic_config.h"
#include "xstatus.h"
#include <stdio.h>

/*****************************************************************************/
/**
* This function reads the information of SFP+ Module.
*
* @param	ModuleID is the index of SFP+ port
*
* @return	XST_SUCCESS if successful else XST_FAILURE.
*
******************************************************************************/

int sfpReadInfo(int ModuleID) {
	int Status;
	u8 WriteBuffer[10];
	u8 reg_addr;
	u8 ReadBuffer[20];
	u8 iicBusSel;
	u8 iicAddress;

	switch(ModuleID) {
	case 1:
		iicBusSel = IIC_BUS_SFP1;
		iicAddress = IIC_SFP1_ADDRESS;
		break;
	case 2:
		iicBusSel = IIC_BUS_SFP2;
		iicAddress = IIC_SFP2_ADDRESS;
		break;
	case 3:
		iicBusSel = IIC_BUS_SFP3;
		iicAddress = IIC_SFP3_ADDRESS;
		break;
	case 4:
		iicBusSel = IIC_BUS_SFP4;
		iicAddress = IIC_SFP4_ADDRESS;
		break;
	default:
		iicBusSel = IIC_BUS_SFP1;
		iicAddress = IIC_SFP1_ADDRESS;
		break;
	}

	/*
	 * Write to the IIC Switch.
	 */
	WriteBuffer[0] = iicBusSel; //Select Bus7 - Si5326
	Status = IicWriteData(IIC_SWITCH_ADDRESS, WriteBuffer, 1);
	if (Status != XST_SUCCESS) {
		xil_printf("PCA9548 FAILED to select Si5324 IIC Bus\r\n");
		return XST_FAILURE;
	}

	/*
	 * Read Vendor ID
	 */
	reg_addr = 20; //Vendor Name ASCII String
	Status = IicReadData(iicAddress, reg_addr, ReadBuffer, 17);
	if (Status != XST_SUCCESS) {
		return XST_FAILURE;
	}
	xil_printf("SFP[%d]: Vendor - %s\r\n", ModuleID, ReadBuffer);

	/*
	 * Read Part Name
	 */
	reg_addr = 40; //Vendor Name ASCII String
	Status = IicReadData(iicAddress, reg_addr, ReadBuffer, 16);
	if (Status != XST_SUCCESS) {
		return XST_FAILURE;
	}
	ReadBuffer[16] = '\0';
	xil_printf("SFP[%d]: Part - %s\r\n", ModuleID, ReadBuffer);

	return XST_SUCCESS;
}

