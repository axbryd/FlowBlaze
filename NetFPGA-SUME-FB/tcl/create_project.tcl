# Vivado Launch Script
#### Change design settings here #######
set design FlowBlaze
set top top
set device xc7vx690t-3-ffg1761
set public_repo_dir $::env(SUME_FOLDER)/lib/hw/
set xilinx_repo_dir $::env(XILINX_PATH)/data/ip/xilinx/

#####################################
# Project Settings
#####################################
create_project -name ${design} -part ${device}
set_property source_mgmt_mode DisplayOnly [current_project]
set_property top ${top} [current_fileset]
set_property ip_repo_paths ${public_repo_dir} [current_fileset]
puts "Creating User Datapath reference project"



#####################################
# Project
#####################################
update_ip_catalog

create_ip -name input_arbiter -vendor NetFPGA -library NetFPGA -module_name input_arbiter_ip
set_property generate_synth_checkpoint false [get_files input_arbiter_ip.xci]
reset_target all [get_ips input_arbiter_ip]
generate_target all [get_ips input_arbiter_ip]

create_ip -name output_queues -vendor NetFPGA -library NetFPGA -module_name output_queues_ip
set_property generate_synth_checkpoint false [get_files output_queues_ip.xci]
reset_target all [get_ips output_queues_ip]
generate_target all [get_ips output_queues_ip]

source tcl/axilite_interconnect.tcl

source tcl/axilite_interconnect_5.tcl

source tcl/control_sub.tcl

source tcl/control_sub_sim.tcl

source tcl/nf_10ge_interface.tcl
create_ip -name nf_10ge_interface -vendor NetFPGA -library NetFPGA -module_name nf_10g_interface_ip
set_property generate_synth_checkpoint false [get_files nf_10g_interface_ip.xci]
reset_target all [get_ips nf_10g_interface_ip]
generate_target all [get_ips nf_10g_interface_ip]

source tcl/nf_10ge_interface_shared.tcl
create_ip -name nf_10ge_interface_shared -vendor NetFPGA -library NetFPGA -module_name nf_10g_interface_shared_ip
set_property generate_synth_checkpoint false [get_files nf_10g_interface_shared_ip.xci]
reset_target all [get_ips nf_10g_interface_shared_ip]
generate_target all [get_ips nf_10g_interface_shared_ip]

#Add a clock wizard
create_ip -name clk_wiz -vendor xilinx.com -library ip -version 5.3 -module_name clk_wiz_ip
set_property -dict [list   CONFIG.NUM_OUT_CLKS {3} CONFIG.CLKOUT2_USED {true} CONFIG.CLKOUT3_USED {true} CONFIG.PRIM_IN_FREQ {200.00} CONFIG.CLKOUT1_REQUESTED_OUT_FREQ {156.250} CONFIG.CLKOUT2_REQUESTED_OUT_FREQ {100.000} CONFIG.CLKOUT3_REQUESTED_OUT_FREQ {100.000} CONFIG.USE_SAFE_CLOCK_STARTUP {true} CONFIG.RESET_TYPE {ACTIVE_LOW} CONFIG.CLKIN1_JITTER_PS {50.0} CONFIG.CLKOUT1_DRIVES {BUFGCE} CONFIG.CLKOUT2_DRIVES {BUFGCE} CONFIG.CLKOUT3_DRIVES {BUFGCE} CONFIG.CLKOUT4_DRIVES {BUFGCE} CONFIG.CLKOUT5_DRIVES {BUFGCE} CONFIG.CLKOUT6_DRIVES {BUFGCE} CONFIG.CLKOUT7_DRIVES {BUFGCE} CONFIG.MMCM_CLKIN1_PERIOD {5.0} CONFIG.RESET_PORT {resetn} ] [get_ips clk_wiz_ip]


set_property generate_synth_checkpoint false [get_files clk_wiz_ip.xci]
reset_target all [get_ips clk_wiz_ip]
generate_target all [get_ips clk_wiz_ip]

create_ip -name proc_sys_reset -vendor xilinx.com -library ip -version 5.0 -module_name proc_sys_reset_ip
set_property -dict [list CONFIG.C_EXT_RESET_HIGH {0} CONFIG.C_AUX_RESET_HIGH {0}] [get_ips proc_sys_reset_ip]
set_property -dict [list CONFIG.C_NUM_PERP_RST {1} CONFIG.C_NUM_PERP_ARESETN {1}] [get_ips proc_sys_reset_ip]
set_property generate_synth_checkpoint false [get_files proc_sys_reset_ip.xci]
reset_target all [get_ips proc_sys_reset_ip]
generate_target all [get_ips proc_sys_reset_ip]

#Add ID block
create_ip -name blk_mem_gen -vendor xilinx.com -library ip -module_name identifier_ip
set_property -dict [list CONFIG.Interface_Type {AXI4} CONFIG.AXI_Type {AXI4_Lite} CONFIG.AXI_Slave_Type {Memory_Slave} CONFIG.Use_AXI_ID {false} CONFIG.Load_Init_File {true} CONFIG.Coe_File {/../../../../../FlowBlaze.src/id_rom16x32.coe} CONFIG.Fill_Remaining_Memory_Locations {true} CONFIG.Remaining_Memory_Locations {DEADDEAD} CONFIG.Memory_Type {Simple_Dual_Port_RAM} CONFIG.Use_Byte_Write_Enable {true} CONFIG.Byte_Size {8} CONFIG.Assume_Synchronous_Clk {true} CONFIG.Write_Width_A {32} CONFIG.Write_Depth_A {4096} CONFIG.Read_Width_A {32} CONFIG.Operating_Mode_A {READ_FIRST} CONFIG.Write_Width_B {32} CONFIG.Read_Width_B {32} CONFIG.Operating_Mode_B {READ_FIRST} CONFIG.Enable_B {Use_ENB_Pin} CONFIG.Register_PortA_Output_of_Memory_Primitives {false} CONFIG.Register_PortB_Output_of_Memory_Primitives {false} CONFIG.Use_RSTB_Pin {true} CONFIG.Reset_Type {ASYNC} CONFIG.Port_A_Write_Rate {50} CONFIG.Port_B_Clock {100} CONFIG.Port_B_Enable_Rate {100}] [get_ips identifier_ip]
set_property generate_synth_checkpoint false [get_files identifier_ip.xci]
reset_target all [get_ips identifier_ip]
generate_target all [get_ips identifier_ip]



create_ip -name axi_clock_converter -module_name axi_clock_converter_0
set_property -dict [list CONFIG.PROTOCOL {AXI4LITE} CONFIG.DATA_WIDTH {32} CONFIG.ID_WIDTH {0} CONFIG.AWUSER_WIDTH {0} CONFIG.ARUSER_WIDTH {0} CONFIG.RUSER_WIDTH {0} CONFIG.WUSER_WIDTH {0} CONFIG.BUSER_WIDTH {0}] [get_ips axi_clock_converter_0]

create_ip -name axis_clock_converter -module_name axis_clock_converter_0
set_property -dict [list CONFIG.TDATA_NUM_BYTES {32} CONFIG.TUSER_WIDTH {128} CONFIG.HAS_TKEEP {1} CONFIG.HAS_TLAST {1}] [get_ips axis_clock_converter_0]


create_ip -name axis_data_fifo -module_name axis_data_fifo_0
set_property -dict [list CONFIG.TDATA_NUM_BYTES {32} CONFIG.TUSER_WIDTH {128} CONFIG.HAS_TKEEP {1} CONFIG.HAS_TLAST {1}] [get_ips axis_data_fifo_0]

read_vhdl -library cam  FlowBlaze.src/dmem.vhd
read_vhdl -library cam  [glob FlowBlaze.src/cam*.vhd]


#create ip for simulation

create_ip -name barrier -vendor NetFPGA -library NetFPGA -module_name barrier_ip
reset_target all [get_ips barrier_ip]
generate_target all [get_ips barrier_ip]

create_ip -name axis_sim_record -vendor NetFPGA -library NetFPGA -module_name axis_sim_record_ip0
set_property -dict [list CONFIG.OUTPUT_FILE record0.axi] [get_ips axis_sim_record_ip0]
reset_target all [get_ips axis_sim_record_ip0]
generate_target all [get_ips axis_sim_record_ip0]

create_ip -name axis_sim_record -vendor NetFPGA -library NetFPGA -module_name axis_sim_record_ip1
set_property -dict [list CONFIG.OUTPUT_FILE record1.axi] [get_ips axis_sim_record_ip1]
reset_target all [get_ips axis_sim_record_ip1]
generate_target all [get_ips axis_sim_record_ip1]

create_ip -name axis_sim_record -vendor NetFPGA -library NetFPGA -module_name axis_sim_record_ip2
set_property -dict [list CONFIG.OUTPUT_FILE record2.axi] [get_ips axis_sim_record_ip2]
reset_target all [get_ips axis_sim_record_ip2]
generate_target all [get_ips axis_sim_record_ip2]

create_ip -name axis_sim_record -vendor NetFPGA -library NetFPGA -module_name axis_sim_record_ip3
set_property -dict [list CONFIG.OUTPUT_FILE record3.axi] [get_ips axis_sim_record_ip3]
reset_target all [get_ips axis_sim_record_ip3]
generate_target all [get_ips axis_sim_record_ip3]

create_ip -name axis_sim_record -vendor NetFPGA -library NetFPGA -module_name axis_sim_record_ip4
set_property -dict [list CONFIG.OUTPUT_FILE record4.axi] [get_ips axis_sim_record_ip4]
reset_target all [get_ips axis_sim_record_ip4]
generate_target all [get_ips axis_sim_record_ip4]

create_ip -name axis_sim_stim -vendor NetFPGA -library NetFPGA -module_name axis_sim_stim_ip0
set_property -dict [list CONFIG.input_file test0.axi] [get_ips axis_sim_stim_ip0]
generate_target all [get_ips axis_sim_stim_ip0]

create_ip -name axis_sim_stim -vendor NetFPGA -library NetFPGA -module_name axis_sim_stim_ip1
set_property -dict [list CONFIG.input_file test1.axi] [get_ips axis_sim_stim_ip1]
generate_target all [get_ips axis_sim_stim_ip1]

create_ip -name axis_sim_stim -vendor NetFPGA -library NetFPGA -module_name axis_sim_stim_ip2
set_property -dict [list CONFIG.input_file test2.axi] [get_ips axis_sim_stim_ip2]
generate_target all [get_ips axis_sim_stim_ip2]

create_ip -name axis_sim_stim -vendor NetFPGA -library NetFPGA -module_name axis_sim_stim_ip3
set_property -dict [list CONFIG.input_file test3.axi] [get_ips axis_sim_stim_ip3]
generate_target all [get_ips axis_sim_stim_ip3]

create_ip -name axis_sim_stim -vendor NetFPGA -library NetFPGA -module_name axis_sim_stim_ip4
set_property -dict [list CONFIG.input_file test4.axi] [get_ips axis_sim_stim_ip4]
generate_target all [get_ips axis_sim_stim_ip4]

create_ip -name axi_sim_transactor -vendor NetFPGA -library NetFPGA -module_name axi_sim_transactor_ip
set_property -dict [list CONFIG.STIM_FILE reg_stim.axi CONFIG.EXPECT_FILE reg_exp.axi CONFIG.LOG_FILE reg_stim.log] [get_ips axi_sim_transactor_ip]
reset_target all [get_ips axi_sim_transactor_ip]
generate_target all [get_ips axi_sim_transactor_ip]

update_ip_catalog

add_files [glob FlowBlaze.src/*.v]
add_files [glob FlowBlaze.src/*.vhd]
add_files [glob FlowBlaze.src/*.coe]
add_files [glob FlowBlaze.src/*.mif]
add_files [glob FlowBlaze.src/input_files/*.axi]
add_files -fileset constrs_1 [glob FlowBlaze.src/*.xdc]

set_property used_in_synthesis false [get_files top_tb.v]
set_property used_in_implementation false [get_files top_tb.v]

set_property used_in_synthesis false [get_files top_sim.v]
set_property used_in_implementation false [get_files top_sim.v]

set_property used_in_synthesis false [get_files barrier_ip.xci]
set_property used_in_implementation false [get_files barrier_ip.xci]
set_property generate_synth_checkpoint false [get_files barrier_ip.xci]

set_property used_in_synthesis false [get_files axi_sim_transactor_ip.xci]
set_property used_in_implementation false [get_files axi_sim_transactor_ip.xci]
set_property generate_synth_checkpoint false [get_files axi_sim_transactor_ip.xci]

set_property used_in_synthesis false [get_files axis_sim_stim_ip0.xci]
set_property used_in_implementation false [get_files axis_sim_stim_ip0.xci]
set_property generate_synth_checkpoint false [get_files axis_sim_stim_ip0.xci]

set_property used_in_synthesis false [get_files axis_sim_stim_ip1.xci]
set_property used_in_implementation false [get_files axis_sim_stim_ip1.xci]
set_property generate_synth_checkpoint false [get_files axis_sim_stim_ip1.xci]

set_property used_in_synthesis false [get_files axis_sim_stim_ip2.xci]
set_property used_in_implementation false [get_files axis_sim_stim_ip2.xci]
set_property generate_synth_checkpoint false [get_files axis_sim_stim_ip2.xci]

set_property used_in_synthesis false [get_files axis_sim_stim_ip3.xci]
set_property used_in_implementation false [get_files axis_sim_stim_ip3.xci]
set_property generate_synth_checkpoint false [get_files axis_sim_stim_ip3.xci]

set_property used_in_synthesis false [get_files axis_sim_stim_ip4.xci]
set_property used_in_implementation false [get_files axis_sim_stim_ip4.xci]
set_property generate_synth_checkpoint false [get_files axis_sim_stim_ip4.xci]

set_property used_in_synthesis false [get_files axis_sim_record_ip0.xci]
set_property used_in_implementation false [get_files axis_sim_record_ip0.xci]
set_property generate_synth_checkpoint false [get_files axis_sim_record_ip0.xci]

set_property used_in_synthesis false [get_files axis_sim_record_ip1.xci]
set_property used_in_implementation false [get_files axis_sim_record_ip1.xci]
set_property generate_synth_checkpoint false [get_files axis_sim_record_ip1.xci]

set_property used_in_synthesis false [get_files axis_sim_record_ip2.xci]
set_property used_in_implementation false [get_files axis_sim_record_ip2.xci]
set_property generate_synth_checkpoint false [get_files axis_sim_record_ip2.xci]

set_property used_in_synthesis false [get_files axis_sim_record_ip3.xci]
set_property used_in_implementation false [get_files axis_sim_record_ip3.xci]
set_property generate_synth_checkpoint false [get_files axis_sim_record_ip3.xci]

set_property used_in_synthesis false [get_files axis_sim_record_ip4.xci]
set_property used_in_implementation false [get_files axis_sim_record_ip4.xci]
set_property generate_synth_checkpoint false [get_files axis_sim_record_ip4.xci]

#Setting Simulation options
set_property top top_tb [get_filesets sim_1]
set_property top_lib xil_defaultlib [get_filesets sim_1]

#set file order for cam library
reorder_files -before [get_files dmem.vhd ] [get_files cam_init_file_pack_xst.vhd]
reorder_files -before [get_files dmem.vhd ] [get_files cam_pkg.vhd]
reorder_files -after  [get_files cam_mem_srl16_ternwrcomp.vhd] [get_files cam_mem_srl16.vhd]
reorder_files -before [get_files cam_mem_srl16.vhd] [get_files cam_mem_srl16_wrcomp.vhd]
reorder_files -before [get_files cam_top.vhd] [get_files cam_rtl.vhd]
reorder_files -after  [get_files cam_mem_blk.vhd] [get_files cam_top.vhd]
reorder_files -before [get_files cam_top.vhd] [get_files cam_rtl.vhd]
reorder_files -after  [get_files cam_input_ternary.vhd] [get_files cam_input.vhd]
reorder_files -after  [get_files cam_mem_blk.vhd] [get_files cam_mem.vhd]


#set file order for main library
reorder_files -before [get_files cuckoo.vhd] [get_files hash_pkg.vhd]
reorder_files -before [get_files test_flowblaze.vhd] [get_files delayer_axi.vhd]
reorder_files -before [get_files pipealu.vhd] [get_files rams.vhd]
reorder_files -after [get_files FlowBlaze_core.vhd] [get_files FlowBlaze156.vhd]
reorder_files -after [get_files FlowBlaze156.vhd] [get_files FlowBlaze156_2.vhd]
reorder_files -after [get_files FlowBlaze156.vhd] [get_files FlowBlaze156_5.vhd]
reorder_files -before [get_files rams.vhd] [get_files salutil.vhd]
reorder_files -after [get_files salutil.vhd] [get_files hash_pkg.vhd]
reorder_files -after [get_files rams.vhd] [get_files sam.vhd]
reorder_files -after [get_files sam.vhd] [get_files alu.vhd]
reorder_files -before [get_files cam_mem_srl16_block_word.vhd] [get_files cam_decoder.vhd]

set_property USED_IN {simulation} [get_files test0.axi]
set_property USED_IN_SIMULATION 0 [get_files control_sub_nf_riffa_dma_1_0.xci]
set_property file_type {Memory File} [get_files  {reg_exp.axi reg_stim.axi test0.axi test1.axi test2.axi test3.axi test4.axi}]


#Setting Synthesis options

create_run -flow {Vivado Synthesis 2016} synth
#Setting Implementation options
create_run impl -parent_run synth -flow {Vivado Implementation 2016}
set_property strategy Performance_Explore [get_runs impl_1]
set_property steps.phys_opt_design.is_enabled true [get_runs impl_1]
#set_property STEPS.PHYS_OPT_DESIGN.ARGS.DIRECTIVE Explore [get_runs impl_1]
#set_property STEPS.PHYS_OPT_DESIGN.ARGS.DIRECTIVE AggressiveExplore [get_runs impl_1]
#set_property STEPS.PHYS_OPT_DESIGN.ARGS.DIRECTIVE AlternateFlowWithRetiming [get_runs impl_1]
set_property STEPS.PHYS_OPT_DESIGN.ARGS.DIRECTIVE ExploreWithHoldFix [get_runs impl_1]
set_property STEPS.PLACE_DESIGN.ARGS.DIRECTIVE Explore [get_runs impl_1]
set_property STEPS.POST_ROUTE_PHYS_OPT_DESIGN.is_enabled true [get_runs impl_1]
#set_property STEPS.POST_ROUTE_PHYS_OPT_DESIGN.ARGS.DIRECTIVE Explore [get_runs impl_1]
set_property STEPS.POST_ROUTE_PHYS_OPT_DESIGN.ARGS.DIRECTIVE AggressiveExplore [get_runs impl_1]
# The following implementation options will increase runtime, but get the best timing results
#set_property strategy Performance_Explore [get_runs impl_1]
### Solves synthesis crash in 2013.2
##set_param synth.filterSetMaxDelayWithDataPathOnly true
set_property SEVERITY {Warning} [get_drc_checks UCIO-1]

exit
