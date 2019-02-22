################################################################################
# Vivado Launch Script
################################################################################

.PHONY: clean all

all:
	@echo ""
	@echo "NetFPGA-SUME FlowBlaze project makefile"
	@echo ""
	@echo "usage:"
	@echo "------------------------------------------------------"
	@echo "make project:    Create HW project"
	@echo "make hw:         Compile HW "
	@echo "make firmware:   Create firmware project"
	@echo "make compile:    Compile firmware"
	@echo "make clean:      Remove generated files"
	@echo "make wipe:       Needed to recreate the HW project"

hw: 
	vivado -mode tcl -source tcl/make.tcl
	@cp ./FlowBlaze.runs/impl_1/top.bit flowblaze.bit

firmware:
	@if [ -d ./SDK_Workspace ]; then rm -rf ./SDK_Workspace ; fi
	mkdir ./SDK_Workspace
	xsdk -batch -source ./tcl/flowblaze_xsdk.tcl


# Compile Command needs to run twice to get sw compiled correctly in DEBUG mode
compile:
	@if [ -d ./SDK_Workspace ]; then \
		xsdk -batch -source ./tcl/compile_xsdk.tcl \
	else \
                echo "Firmware Workspace not found"; \
        fi
	@cp ./SDK_Workspace/flowblaze/app/Debug/app.elf firmware.elf

project: 
	vivado -mode tcl -source tcl/create_project.tcl

wipe:   clean	
	rm -rfv  firmware/
	rm -rfv  FlowBlaze.xpr
	rm -rfv  flowblaze.hdf 
	rm -rfv  FlowBlaze.srcs/
	rm -rfv  FlowBlaze.ip_user_files/
	rm -rfv  FlowBlaze.sim/ 
clean:	
	rm -rfv vivado*
	rm -rfv *.log
	rm -rfv .Xil
	rm -rfv webtalk*
	rm -rfv *.*~
	rm -rfv ip_repo
	rm -rfv ip_proj
	rm -rfv *.cache 
	rm -rfv *.hw 
	rm -rfv *.runs 
	rm -rfv SDK_Workspace
	rm -rfv  flowblaze.bit
	rm -rfv  firmware.elf
