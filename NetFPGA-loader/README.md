# NetFPGA loader for XL compiled programs

This directory stores the NetFPGA python programmer for XL programs 
compiled in json. Refer to the XL directory for instructions 
on how to write and compile an XL application.

## Packages required:

    pip install numpy 
    pip install serial

## Usage:

    python load_fpga.py -f [program.json] -m MODE -p [out_file/serial_port]
    
There are 3 different modes of operation to produce various outputs from the
json compiled program:

* *PCI driver mode* (1). This mode configures the NetFPGA 
through the PCI driver. The *-p* parameter must have the path
to the *rwaxi* binary (a tool from NetFPGA framework for 
writing FPGA registers through PCI).
* *Simulation mode* (2). Produces an *.axi* file used as 
configuration file for *Vivado* based simulations. Specify the
output file with the *-p* parameter.
* *USB Serial mode* (3). This mode permits to configure the 
NetFPGA sending commands through the serial interface provided 
by the FPGA. Refer to the NetFPGA-SUME-FB directory to configure
the Serial program that must run on the SmartNIC to use this mode.
Use *-p* to provide the serial port (e.g. /dev/ttyUSB0).
