set ws "SDK_Workspace"
set design "flowblaze"

puts "\nCopying FlowBlaze.sysdef\n"
file copy -force FlowBlaze.runs/impl_1/top.sysdef ./$design.hdf

setws ./$ws/$design
createhw -name hw_platform -hwspec ./$design.hdf
createbsp -name bsp -hwproject hw_platform -proc control_sub_i_nf_mbsys_mbsys_microblaze_0 -os standalone
createapp -name app -hwproject hw_platform -proc control_sub_i_nf_mbsys_mbsys_microblaze_0 -os standalone -lang C -bsp bsp
importsources -name app -path ./firmware.src/

puts "\nDone\n"
exit
