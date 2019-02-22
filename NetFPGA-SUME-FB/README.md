# README #

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE SOFTWARE CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

This repository contains the FlowBlaze prototype for NetFPGA SUME, as described in the NSDI'19 paper.
It should be understood that this prototype is not production ready and
therefore it is provided AS IS, with the user taking full responsibility
for its use. Furthermore, it should be understood that this prototype is provided 
mainly to foster scientific collaboration and ensure the reproducibility
of the research results.

As reported in the paper, this prototype uses statecally defined parser, action and match tables.
This also means that the number of pipeline elements is fixed.
We already provide sources for pipelines comprising 1, 2 and 5 elements. It should be relatively easy to extend such sources to an arbitrary 
number of elements.

The prototype is composed by two parts:

1. The hardware project providing the stateful programmable dataplane
2. The firmware project providing the agent able to configure the stateful programmable with the network functions 
compiled using the XL toolchain. 

However, it should be noted that our prototype supports a relatively small number of packet header actions. Common use cases should not be affected by that.

The prototype has been developed with the Vivado 2016.4 version. We strongly suggest to use the same version.

# How to build FlowBlaze #

1) Install the NetFPGA SUME development environment following the information available at https://github.com/NetFPGA/NetFPGA-SUME-public/wiki

2) Clone the FlowBlaze repo and go into the NetFPGA-SUME-FB folder 

3) source the relevant environmet variables:

```shell
$ source $SUME_FOLDER/tools/settings.sh
$ source $XILINX_PATH/settings64.sh
$ source $XILINX_SDK/settings64.sh
```

4) recreate the vivado project with the command:

```shell
make project
```

5) create a bitstream with the command:

```shell
make hw
```

This command will create the bitfile flowblaze.bit

6) create the firmware project with the command:

```shell
make firmware
```


7) create the firmware executable with the command:

```shell
make compile
```

This command will create an executable named flowblaze.elf

# Install FlowBlaze #

After building the FlowBlaze bitstream and the firmware executable, run the following command to install it on the NetFPGA:


```shell
$ xsdb
```
```shell
xsdb% fpga -f top.bit; dow app.elf; 
```

If your host is connected to the NetFPGA also with the serial port, you can get access to the debug interface of 
FlowBlaze connecting to the serial port over USB, using the following command:


```shell
$ sudo minicom -D /dev/ttyUSB1 
```

To download a network function into the FlowBlaze prototype follows the instructions in the XL-toolchain folder.

# CONTACTS #

This repository is mantained by [axbryd](http://axbryd.com/). 
If you have any question or you need support for using and extending the prototype please send an
email to [info@axbryd.com](mailto:info@axbryd.com).
