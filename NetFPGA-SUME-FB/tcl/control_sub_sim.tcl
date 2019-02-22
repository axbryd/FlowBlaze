
################################################################
# This is a generated script based on design: control_sub_sim
#
# Though there are limitations about the generated script,
# the main purpose of this utility is to make learning
# IP Integrator Tcl commands easier.
################################################################

################################################################
# Check if script is running in correct Vivado version.
################################################################
set scripts_vivado_version 2016.4
set current_vivado_version [version -short]

if { [string first $scripts_vivado_version $current_vivado_version] == -1 } {
   puts ""
   puts "ERROR: This script was generated using Vivado <$scripts_vivado_version> and is being run in <$current_vivado_version> of Vivado. Please run the script in Vivado <$scripts_vivado_version> then open the design in Vivado <$current_vivado_version>. Upgrade the design by running \"Tools => Report => Report IP Status...\", then run write_bd_tcl to create an updated script."

   return 1
}

################################################################
# START
################################################################

# To test this script, run the following commands from Vivado Tcl console:
# source control_sub_sim_script.tcl

# If you do not already have a project created,
# you can create a project using the following command:
#    create_project project_1 myproj -part xc7vx690tffg1761-3

# CHECKING IF PROJECT EXISTS
if { [get_projects -quiet] eq "" } {
   puts "ERROR: Please open or create a project!"
   return 1
}



# CHANGE DESIGN NAME HERE
set design_name control_sub_sim

# If you do not already have an existing IP Integrator design open,
# you can create a design using the following command:
#    create_bd_design $design_name

# Creating design if needed
set errMsg ""
set nRet 0

set cur_design [current_bd_design -quiet]
set list_cells [get_bd_cells -quiet]

if { ${design_name} eq "" } {
   # USE CASES:
   #    1) Design_name not set

   set errMsg "ERROR: Please set the variable <design_name> to a non-empty value."
   set nRet 1

} elseif { ${cur_design} ne "" && ${list_cells} eq "" } {
   # USE CASES:
   #    2): Current design opened AND is empty AND names same.
   #    3): Current design opened AND is empty AND names diff; design_name NOT in project.
   #    4): Current design opened AND is empty AND names diff; design_name exists in project.

   if { $cur_design ne $design_name } {
      puts "INFO: Changing value of <design_name> from <$design_name> to <$cur_design> since current design is empty."
      set design_name [get_property NAME $cur_design]
   }
   puts "INFO: Constructing design in IPI design <$cur_design>..."

} elseif { ${cur_design} ne "" && $list_cells ne "" && $cur_design eq $design_name } {
   # USE CASES:
   #    5) Current design opened AND has components AND same names.

   #set errMsg "ERROR: Design <$design_name> already exists in your project, please set the variable <design_name> to another value."
   #set nRet 1
   remove_files [get_files -quiet ${design_name}.bd] 
   
} elseif { [get_files -quiet ${design_name}.bd] ne "" } {
   # USE CASES: 
   #    6) Current opened design, has components, but diff names, design_name exists in project.
   #    7) No opened design, design_name exists in project.

   #set errMsg "ERROR: Design <$design_name> already exists in your project, please set the variable <design_name> to another value."
   #set nRet 2
   remove_files [get_files -quiet ${design_name}.bd] 

} 

#else {
   # USE CASES:
   #    8) No opened design, design_name not in project.
   #    9) Current opened design, has components, but diff names, design_name not in project.

   puts "INFO: Currently there is no design <$design_name> in project, so creating one..."

   create_bd_design $design_name

   puts "INFO: Making design <$design_name> as current_bd_design."
   current_bd_design $design_name

#}

puts "INFO: Currently the variable <design_name> is equal to \"$design_name\"."

if { $nRet != 0 } {
   puts $errMsg
   return $nRet
}

##################################################################
# DESIGN PROCs
##################################################################



# Procedure to create entire design; Provide argument to make
# procedure reusable. If parentCell is "", will use root.
proc create_root_design { parentCell } {

  if { $parentCell eq "" } {
     set parentCell [get_bd_cells /]
  }

  # Get object for parentCell
  set parentObj [get_bd_cells $parentCell]
  if { $parentObj == "" } {
     puts "ERROR: Unable to find parent cell <$parentCell>!"
     return
  }

  # Make sure parentObj is hier blk
  set parentType [get_property TYPE $parentObj]
  if { $parentType ne "hier" } {
     puts "ERROR: Parent <$parentObj> has TYPE = <$parentType>. Expected to be <hier>."
     return
  }

  # Save current instance; Restore later
  set oldCurInst [current_bd_instance .]

  # Set parent object as current
  current_bd_instance $parentObj


  # Create interface ports
  set M00_AXI [ create_bd_intf_port -mode Master -vlnv xilinx.com:interface:aximm_rtl:1.0 M00_AXI ]
  set_property -dict [ list \
CONFIG.ADDR_WIDTH {32} \
CONFIG.DATA_WIDTH {32} \
CONFIG.FREQ_HZ {200000000} \
CONFIG.PROTOCOL {AXI4LITE} \
 ] $M00_AXI
  set M01_AXI [ create_bd_intf_port -mode Master -vlnv xilinx.com:interface:aximm_rtl:1.0 M01_AXI ]
  set_property -dict [ list \
CONFIG.ADDR_WIDTH {32} \
CONFIG.DATA_WIDTH {32} \
CONFIG.FREQ_HZ {200000000} \
CONFIG.PROTOCOL {AXI4LITE} \
 ] $M01_AXI
  set M02_AXI [ create_bd_intf_port -mode Master -vlnv xilinx.com:interface:aximm_rtl:1.0 M02_AXI ]
  set_property -dict [ list \
CONFIG.ADDR_WIDTH {32} \
CONFIG.DATA_WIDTH {32} \
CONFIG.FREQ_HZ {200000000} \
CONFIG.PROTOCOL {AXI4LITE} \
 ] $M02_AXI
  set M03_AXI [ create_bd_intf_port -mode Master -vlnv xilinx.com:interface:aximm_rtl:1.0 M03_AXI ]
  set_property -dict [ list \
CONFIG.ADDR_WIDTH {32} \
CONFIG.DATA_WIDTH {32} \
CONFIG.FREQ_HZ {200000000} \
CONFIG.PROTOCOL {AXI4LITE} \
 ] $M03_AXI
  set M04_AXI [ create_bd_intf_port -mode Master -vlnv xilinx.com:interface:aximm_rtl:1.0 M04_AXI ]
  set_property -dict [ list \
CONFIG.ADDR_WIDTH {32} \
CONFIG.DATA_WIDTH {32} \
CONFIG.FREQ_HZ {200000000} \
CONFIG.PROTOCOL {AXI4LITE} \
 ] $M04_AXI
  set M05_AXI [ create_bd_intf_port -mode Master -vlnv xilinx.com:interface:aximm_rtl:1.0 M05_AXI ]
  set_property -dict [ list \
CONFIG.ADDR_WIDTH {32} \
CONFIG.DATA_WIDTH {32} \
CONFIG.FREQ_HZ {200000000} \
CONFIG.PROTOCOL {AXI4LITE} \
 ] $M05_AXI
  set M06_AXI [ create_bd_intf_port -mode Master -vlnv xilinx.com:interface:aximm_rtl:1.0 M06_AXI ]
  set_property -dict [ list \
CONFIG.ADDR_WIDTH {32} \
CONFIG.DATA_WIDTH {32} \
CONFIG.FREQ_HZ {200000000} \
CONFIG.PROTOCOL {AXI4LITE} \
 ] $M06_AXI
  set M07_AXI [ create_bd_intf_port -mode Master -vlnv xilinx.com:interface:aximm_rtl:1.0 M07_AXI ]
  set_property -dict [ list \
CONFIG.ADDR_WIDTH {32} \
CONFIG.DATA_WIDTH {32} \
CONFIG.FREQ_HZ {200000000} \
CONFIG.PROTOCOL {AXI4LITE} \
 ] $M07_AXI
  set S00_AXI [ create_bd_intf_port -mode Slave -vlnv xilinx.com:interface:aximm_rtl:1.0 S00_AXI ]
  set_property -dict [ list \
CONFIG.ADDR_WIDTH {32} \
CONFIG.ARUSER_WIDTH {0} \
CONFIG.AWUSER_WIDTH {0} \
CONFIG.BUSER_WIDTH {0} \
CONFIG.DATA_WIDTH {32} \
CONFIG.HAS_BRESP {1} \
CONFIG.HAS_BURST {1} \
CONFIG.HAS_CACHE {1} \
CONFIG.HAS_LOCK {1} \
CONFIG.HAS_PROT {1} \
CONFIG.HAS_QOS {1} \
CONFIG.HAS_REGION {1} \
CONFIG.HAS_RRESP {1} \
CONFIG.HAS_WSTRB {1} \
CONFIG.ID_WIDTH {0} \
CONFIG.MAX_BURST_LENGTH {256} \
CONFIG.NUM_READ_OUTSTANDING {2} \
CONFIG.NUM_WRITE_OUTSTANDING {2} \
CONFIG.PROTOCOL {AXI4} \
CONFIG.READ_WRITE_MODE {READ_WRITE} \
CONFIG.RUSER_WIDTH {0} \
CONFIG.SUPPORTS_NARROW_BURST {1} \
CONFIG.WUSER_WIDTH {0} \
 ] $S00_AXI

  # Create ports
  set axi_lite_aclk [ create_bd_port -dir I -type clk axi_lite_aclk ]
  set axi_lite_areset [ create_bd_port -dir I -type rst axi_lite_areset ]
  set core_clk [ create_bd_port -dir I -type clk core_clk ]
  set_property -dict [ list \
CONFIG.FREQ_HZ {200000000} \
 ] $core_clk
  set core_resetn [ create_bd_port -dir I -type rst core_resetn ]

  # Create instance: axi_clock_converter_0, and set properties
  set axi_clock_converter_0 [ create_bd_cell -type ip -vlnv xilinx.com:ip:axi_clock_converter:2.1 axi_clock_converter_0 ]

  # Create instance: axi_interconnect_0, and set properties
  set axi_interconnect_0 [ create_bd_cell -type ip -vlnv xilinx.com:ip:axi_interconnect:2.1 axi_interconnect_0 ]
  set_property -dict [ list \
CONFIG.M00_HAS_DATA_FIFO {1} \
CONFIG.M00_HAS_REGSLICE {3} \
CONFIG.M01_HAS_DATA_FIFO {1} \
CONFIG.M01_HAS_REGSLICE {3} \
CONFIG.M02_HAS_DATA_FIFO {1} \
CONFIG.M02_HAS_REGSLICE {3} \
CONFIG.M03_HAS_DATA_FIFO {1} \
CONFIG.M03_HAS_REGSLICE {3} \
CONFIG.M04_HAS_DATA_FIFO {1} \
CONFIG.M04_HAS_REGSLICE {3} \
CONFIG.M05_HAS_DATA_FIFO {1} \
CONFIG.M05_HAS_REGSLICE {3} \
CONFIG.M06_HAS_DATA_FIFO {1} \
CONFIG.M06_HAS_REGSLICE {3} \
CONFIG.M07_HAS_DATA_FIFO {1} \
CONFIG.M07_HAS_REGSLICE {3} \
CONFIG.NUM_MI {8} \
CONFIG.S00_HAS_DATA_FIFO {1} \
CONFIG.S00_HAS_REGSLICE {3} \
 ] $axi_interconnect_0

  # Create interface connections
  connect_bd_intf_net -intf_net S00_AXI_1 [get_bd_intf_ports S00_AXI] [get_bd_intf_pins axi_clock_converter_0/S_AXI]
  connect_bd_intf_net -intf_net axi_clock_converter_0_M_AXI [get_bd_intf_pins axi_clock_converter_0/M_AXI] [get_bd_intf_pins axi_interconnect_0/S00_AXI]
  connect_bd_intf_net -intf_net axi_interconnect_0_M00_AXI [get_bd_intf_ports M00_AXI] [get_bd_intf_pins axi_interconnect_0/M00_AXI]
  connect_bd_intf_net -intf_net axi_interconnect_0_M01_AXI [get_bd_intf_ports M01_AXI] [get_bd_intf_pins axi_interconnect_0/M01_AXI]
  connect_bd_intf_net -intf_net axi_interconnect_0_M02_AXI [get_bd_intf_ports M02_AXI] [get_bd_intf_pins axi_interconnect_0/M02_AXI]
  connect_bd_intf_net -intf_net axi_interconnect_0_M03_AXI [get_bd_intf_ports M03_AXI] [get_bd_intf_pins axi_interconnect_0/M03_AXI]
  connect_bd_intf_net -intf_net axi_interconnect_0_M04_AXI [get_bd_intf_ports M04_AXI] [get_bd_intf_pins axi_interconnect_0/M04_AXI]
  connect_bd_intf_net -intf_net axi_interconnect_0_M05_AXI [get_bd_intf_ports M05_AXI] [get_bd_intf_pins axi_interconnect_0/M05_AXI]
  connect_bd_intf_net -intf_net axi_interconnect_0_M06_AXI [get_bd_intf_ports M06_AXI] [get_bd_intf_pins axi_interconnect_0/M06_AXI]
  connect_bd_intf_net -intf_net axi_interconnect_0_M07_AXI [get_bd_intf_ports M07_AXI] [get_bd_intf_pins axi_interconnect_0/M07_AXI]

  # Create port connections
  connect_bd_net -net axi_lite_aclk_1 [get_bd_ports axi_lite_aclk] [get_bd_pins axi_clock_converter_0/s_axi_aclk]
  connect_bd_net -net axi_lite_areset_1 [get_bd_ports axi_lite_areset] [get_bd_pins axi_clock_converter_0/s_axi_aresetn]
  connect_bd_net -net core_clk_1 [get_bd_ports core_clk] [get_bd_pins axi_clock_converter_0/m_axi_aclk] [get_bd_pins axi_interconnect_0/ACLK] [get_bd_pins axi_interconnect_0/M00_ACLK] [get_bd_pins axi_interconnect_0/M01_ACLK] [get_bd_pins axi_interconnect_0/M02_ACLK] [get_bd_pins axi_interconnect_0/M03_ACLK] [get_bd_pins axi_interconnect_0/M04_ACLK] [get_bd_pins axi_interconnect_0/M05_ACLK] [get_bd_pins axi_interconnect_0/M06_ACLK] [get_bd_pins axi_interconnect_0/M07_ACLK] [get_bd_pins axi_interconnect_0/S00_ACLK]
  connect_bd_net -net core_resetn_1 [get_bd_ports core_resetn] [get_bd_pins axi_clock_converter_0/m_axi_aresetn] [get_bd_pins axi_interconnect_0/ARESETN] [get_bd_pins axi_interconnect_0/M00_ARESETN] [get_bd_pins axi_interconnect_0/M01_ARESETN] [get_bd_pins axi_interconnect_0/M02_ARESETN] [get_bd_pins axi_interconnect_0/M03_ARESETN] [get_bd_pins axi_interconnect_0/M04_ARESETN] [get_bd_pins axi_interconnect_0/M05_ARESETN] [get_bd_pins axi_interconnect_0/M06_ARESETN] [get_bd_pins axi_interconnect_0/M07_ARESETN] [get_bd_pins axi_interconnect_0/S00_ARESETN]

  # Create address segments
  create_bd_addr_seg -range 0x1000 -offset 0x44000000 [get_bd_addr_spaces S00_AXI] [get_bd_addr_segs M00_AXI/Reg] SEG_M00_AXI_Reg
  create_bd_addr_seg -range 0x1000 -offset 0x44010000 [get_bd_addr_spaces S00_AXI] [get_bd_addr_segs M01_AXI/Reg] SEG_M01_AXI_Reg
  create_bd_addr_seg -range 0x1000 -offset 0x44020000 [get_bd_addr_spaces S00_AXI] [get_bd_addr_segs M02_AXI/Reg] SEG_M02_AXI_Reg
  create_bd_addr_seg -range 0x1000 -offset 0x44030000 [get_bd_addr_spaces S00_AXI] [get_bd_addr_segs M03_AXI/Reg] SEG_M03_AXI_Reg
  create_bd_addr_seg -range 0x10000 -offset 0x44A00000 [get_bd_addr_spaces S00_AXI] [get_bd_addr_segs M04_AXI/Reg] SEG_M04_AXI_Reg
  create_bd_addr_seg -range 0x10000 -offset 0x44A10000 [get_bd_addr_spaces S00_AXI] [get_bd_addr_segs M05_AXI/Reg] SEG_M05_AXI_Reg
  create_bd_addr_seg -range 0x10000 -offset 0x44A20000 [get_bd_addr_spaces S00_AXI] [get_bd_addr_segs M06_AXI/Reg] SEG_M06_AXI_Reg
  create_bd_addr_seg -range 0x10000 -offset 0x44A30000 [get_bd_addr_spaces S00_AXI] [get_bd_addr_segs M07_AXI/Reg] SEG_M07_AXI_Reg

  # Perform GUI Layout
  regenerate_bd_layout -layout_string {
   guistr: "# # String gsaved with Nlview 6.5.5  2015-06-26 bk=1.3371 VDI=38 GEI=35 GUI=JA:1.8
#  -string -flagsOSRD
preplace port M07_AXI -pg 1 -y 280 -defaultsOSRD
preplace port axi_lite_areset -pg 1 -y 180 -defaultsOSRD
preplace port core_clk -pg 1 -y 200 -defaultsOSRD
preplace port S00_AXI -pg 1 -y 140 -defaultsOSRD
preplace port M06_AXI -pg 1 -y 260 -defaultsOSRD
preplace port core_resetn -pg 1 -y 220 -defaultsOSRD
preplace port M01_AXI -pg 1 -y 160 -defaultsOSRD
preplace port M04_AXI -pg 1 -y 220 -defaultsOSRD
preplace port M03_AXI -pg 1 -y 200 -defaultsOSRD
preplace port M05_AXI -pg 1 -y 240 -defaultsOSRD
preplace port M02_AXI -pg 1 -y 180 -defaultsOSRD
preplace port axi_lite_aclk -pg 1 -y 160 -defaultsOSRD
preplace port M00_AXI -pg 1 -y 140 -defaultsOSRD
preplace inst axi_clock_converter_0 -pg 1 -lvl 1 -y 180 -defaultsOSRD
preplace inst axi_interconnect_0 -pg 1 -lvl 1 -y 510 -defaultsOSRD
preplace netloc axi_lite_areset_1 1 0 1 N
preplace netloc axi_interconnect_0_M02_AXI 1 1 2 360 180 NJ
preplace netloc core_clk_1 1 0 1 20
preplace netloc axi_lite_aclk_1 1 0 1 N
preplace netloc axi_interconnect_0_M07_AXI 1 1 2 410 280 NJ
preplace netloc core_resetn_1 1 0 1 10
preplace netloc axi_interconnect_0_M04_AXI 1 1 2 380 220 NJ
preplace netloc S00_AXI_1 1 0 1 N
preplace netloc axi_interconnect_0_M05_AXI 1 1 2 390 240 NJ
preplace netloc axi_interconnect_0_M00_AXI 1 1 2 340 140 NJ
preplace netloc axi_clock_converter_0_M_AXI 1 0 2 30 90 330
preplace netloc axi_interconnect_0_M01_AXI 1 1 2 350 160 NJ
preplace netloc axi_interconnect_0_M06_AXI 1 1 2 400 260 NJ
preplace netloc axi_interconnect_0_M03_AXI 1 1 2 370 200 NJ
levelinfo -pg 1 -10 180 430 470 -top 0 -bot 770
",
}

  # Restore current instance
  current_bd_instance $oldCurInst

  save_bd_design
}
# End of create_root_design()


##################################################################
# MAIN FLOW
##################################################################

create_root_design ""


