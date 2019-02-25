# Examples description

To compile the use case examples, use the `make` command.

    make load-balancer
    make elephant
    make firewall
    make policer


## Load Balancer
In this use case, we implement a load balancer function that assigns TCP connections to a set of web servers in a private LAN, in a round-robin fashion. Directing traffic to a given web server is as easy as configuring a static NAT rule. Nonetheless, the complexity of the use case is in the necessity of keeping track of two different states. A global state is used to store a counter value that is checked for each new flow received by the switch and it is used to enforce the round robin selection. The second one is a per-flow state that binds each established flow to one of the available destination servers and assure the forwarding consistency. That is, the destination web server is selected when the first connection’s packet is received, and all the remaining packets for that flow should be forwarded to that same web server. 

## Elephant Flow Detection

This application marks packets belonging to a given flow if the flow has generated more than threshold packets.

## Port Knocking Firewall

This use case implements a port knocking firewall, a well-known method for opening a port on a firewall. An IP host that wants to establish a connection to a remote server (say an SSH session, i.e., port 22), delivers a sequence of packets addressed to an ordered list of pre-defined closed ports, say ports 2570, 2827 and 3084. Once the exact sequence of packets is received, the firewall allow the connections for the considered host. Before this stage, all packets (including the knocking ones) are dropped.
Starting from a INITIAL state, each correctly knocked port will cause a transition to a series of two intermediate states, PORT1 and PORT2, until a final ALLOWED state, 4, is reached.

## Traffic Policer

This use case implements a single rate token bucket with burst size B and token rate 1/Q, where Q is the token inter arrival time. Since in the current FlowBlaze architecture the update functions are performed after the condition verification, we cannot update the number of tokens in the bucket based on packet arrival time before evaluating the condition (token availability) for packet forwarding. For this reason, we have implemented an alternative approximated algorithm based on a sliding time window. For each flow, a time window *W(Tmin − Tmax)** of length BQ is maintained to represent the availability times of the tokens in the bucket. 
At each packet arrival, if arrival time Tnow is within W (Case 1), at least one token is available and the bucket is not full, so we shift W by Q to the right and forward packet. If the arrival time is after Tmax (Case 2), the bucket is full, so packet is forwarded and W is moved to the right to reflect that *B−1* tokens are now available *(Tmin = Tnow − (B−1) ∗ Q* and *Tmax = Tnow+Q)*. Finally, if the packet is received before Tmin (Case 3), no token is available, therefore W is left unchanged and the packet is dropped. Upon receipt of the first flow packet, we make a state transition in which we initialize the two registers: *Tmin = Tnow − (B − 1) ∗ Q* and *Tmax = Tnow + Q* (initialization with full bucket).


