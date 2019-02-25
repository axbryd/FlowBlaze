# Getting started

### Building the compiler
In order to build the xlc compiler, you need to install [Maven](https://maven.apache.org/) first. Visit [this page](https://maven.apache.org/install.html) for the Maven installation instructions.

After this, you can build the compiler jar file (*xlc.jar*), just with the `make` command.

### Compiling an *XFSM Language* file
For compiling an *XFSM Language* file, to obtain the JSON representation of the XFSM, you need to use the command

`java -jar xlc.jar -i input_file.xl -o output_file.json`
