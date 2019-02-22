if { [ catch {open_project FlowBlaze.xpr}] } {
    puts ""
        puts "ERROR in opening project. Try to re-create bd files"    
        source tcl/axilite_interconnect.tcl
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
}
