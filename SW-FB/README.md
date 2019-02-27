
# DPDK software implementation of FlowBlaze

This directory contains a simple software implementation of FlowBlaze 
based on the Intel DPDK framework.

## Installation

### Requirements 

Before compiling and running FlowBlaze the DPDK platform must be 
installed in the system. Refer to [DPDK build guide](https://doc.dpdk.org/guides/linux_gsg/build_dpdk.html)
to correctly build DPDK on your system. We tested the program with DPDK
v18.11 LTS. 

### Compile FlowBlaze

The Makefile requires two environment variables to be exported. The
*RTE_SDK* must contain the path in which dpdk is installed and *RTE_TARGET*
must declare the DPDK target for which the application has to be built. 
For example:

    export RTE_SDK="/opt/dpdk-18.11"
    export RTE_TARGET="x86_64-native-linuxapp-gcc"
    
Then, just issue *make* to compile the application.
    
### Run FlowBlaze

The above variables must be exported also for running the application. 
Moreover, before running the applications make sure that all the DPDK
resource requirements are fulfilled:
           
* Hugepages setup is done.
* Any kernel driver being used is loaded.
* In case needed, ports being used by the application should be bound 
to the corresponding kernel driver.

Refer to [DPDK linux user guide](https://doc.dpdk.org/guides/linux_gsg/)
for more information on running requirements for DPDK.

#### Usage:
FlowBlaze takes two different sets of input paramaters divided by the
*--* symbol:

    ./flowblaze [RTE_EAL parameters] -- [FlowBlaze parameters]
    
* RTE_EAL parameters are the ones related to the DPDK Environment 
Abstraction Layer and take the same options of DPDK sample applications.
Refer to [EAL parameters](https://doc.dpdk.org/guides/linux_gsg/linux_eal_parameters.html)
for more information.
* FlowBlaze parameters are the FlowBlaze specific parameters:
    * *-f* takes the XLc produced JSON file describing the application
     to be loaded in the dataplane.

Follows a basic running example:

    ./flowblaze -m 64 -l 2 -w 03:00.0 -w 03:00.1 -- -f program.json
    
The command will run FlowBlaze on core 2 *(-l 2)* allocating 64 
MBytes of hugepage memory *(-m 64)* and provides two PCI devices that 
will be used in the application *(-w ...)*. Then, loads the program from
the file *program.json* *(-f file)*. 

### Limitations

By now, the application runs on a single core, so more cores provided 
through the *-l* argument will be ignored. 
