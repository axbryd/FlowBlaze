# run with  vivado -mode batch -source make.tcl
open_project FlowBlaze.xpr
set_property source_mgmt_mode DisplayOnly [current_project] 
reset_run synth_1
launch_runs synth_1
wait_on_run synth_1
launch_runs impl_1 -to_step write_bitstream 
wait_on_run impl_1
exit
