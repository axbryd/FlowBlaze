# FlowBlaze

FlowBlaze is an open  abstraction for building stateful packet processing functions in hardware.
The abstraction is based on Extended Finite State Machines and introduces the explicit definition of flow state, allowing FlowBlaze to leverage flow-level parallelism. 
FlowBlaze will be presented at the 16th USENIX Symposium on Networked Systems Design and Implementation ([NSDI'19](https://www.usenix.org/conference/nsdi19)). 
This is the repository for the FlowBlaze's source codes. 

The directories are organized as follows:

* NetFPGA-SUME-FB: FlowBlaze NetFPGA implementation
* SW-FB: FlowBlaze Software implementation
* UseCases: this directory contains the 4 use cases shown in the NSDI'19 demo
* XL-toolchain: the directory contains the XL (XFSM Language) compiler and programmer
* NetFPGA-loader: FlowBlaze programmer for the NetFPGA using the compiled output of XL programs
