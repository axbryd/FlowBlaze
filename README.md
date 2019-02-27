# FlowBlaze

FlowBlaze is an open  abstraction for building stateful packet processing functions in hardware.
The abstraction is based on Extended Finite State Machines and introduces the explicit definition of flow state, allowing FlowBlaze to leverage flow-level parallelism. 
FlowBlaze will be presented at the 16th USENIX Symposium on Networked Systems Design and Implementation ([NSDI'19](https://www.usenix.org/conference/nsdi19)). 
This is the repository for the FlowBlaze's source codes. 

The directories are organized as follows:

* [NetFPGA-SUME-FB](NetFPGA-SUME-FB/): FlowBlaze NetFPGA implementation
* [SW-FB](SW-FB/): FlowBlaze Software implementation
* [UseCases](UseCases/): this directory contains the 4 use cases shown in the NSDI'19 demo
* [XL-toolchain](XL-toolchain/): the directory contains the XL (XFSM Language) compiler and programmer
* [NetFPGA-loader](NetFPGA-loader/): FlowBlaze programmer for the NetFPGA using the compiled output of XL programs

## Quick Start Guidelines

The following steps provide a quick start guide to compile and run an XFSM Language application on the two available targets.

1) Choose one of the two targets:

   a) For NetFPGA refer to the [NetFPGA-SUME-FB](NetFPGA-SUME-FB/) section on how to build the development environment;
   
   b) For the software implementation refer to the [SW-FB](SW-FB/) section to build it.
2) Build the XL compiler referring to [XL-toolchain](XL-toolchain/).
3) Compile an use case from [UseCases](UseCases/) to obtain the json representation of the Network Function. For example:

    ``java -jar xlc.jar -i elephant.xl -o ~/elephant.json``
    
4) Load the application into one of the targets:

   a) For NetFPGA configuration, first load the bitstream to install FlowBlaze into the device:
  
   `$ xsdb
    xsdb$ fpga -f top.bit; dow app.elf;`
    
   Next, to actually configure the FPGA with the selected use case, refer to the [NetFPGA-loader](NetFPGA-loader/) section.    For example, assuming the NetFPGA is connected via usb with serial port in */dev/ttyUSB0*, issue the following command to configure the target:
  
    ``python load_fpga.py -f elephant.json -m 3 -p /dev/ttyUSB0``
    
    b) For the software implementation, run FlowBlaze as described in [SW-FB](SW-FB/) giving as input the compiled `elephant.json` example. For instance:
  
     ``./SW-FB/build/flowblaze -m 64 -l 2 -w 03:00.0 -w 03:00.1 -- -f ~/elephant.json``
5) To test the selected *elephant* application, generate traffic from one of the interfaces connected to the target. You will see that after exceeding a threshold of 150 packets, all subsequent packets will have the ToS IPv4 field marked with a value of 150.
