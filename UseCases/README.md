# Examples description

## Load Balancer

A load balancer function that assigns TCP connections to a set of
web servers in a private LAN, in a round-robin fashion.
Directing traffic to a given web server
is as easy as configuring a static NAT rule. Nonetheless, the complexity of the use case is in the
necessity of keeping track of two different states. A global state is used to store a counter value
that is checked for each new flow received by the switch and it is used to enforce the roundrobin
selection. The second one is a per-flow state that binds each established flow to one of the
available destination servers and assure the forwarding consistency. That is, the destination web
server is selected when the first connection’s packet is received, and all the remaining packets
for that flow should be forwarded to that same web server.

## UDP Stateful Firewall

This use case implements a stateful firewall that allows bidirectional communication between
two networks only if initiated from one of the two sides. This is a typical use case for stateful
firewalls like Linux netfilter/iptables for isolating DMZ networks and ”protected” networks. In
this case, we implement a UDP stateful firewall, that only checks for bidirectional connectivity.
A more complex implementation with TCP connection tracking is presented in the paper. In
iptables, assuming that the communication is allowed from a network behind eth1 to a network
behind eth2 only if initiated by a host behind eth2, this would be realized by the following rules:

    iptables -A FORWARD -i eth2 -o eth1 -j ACCEPT
    iptables -A FORWARD -i eth1 -o eth2 -m state --state ESTABLISHED -j ACCEPT

Packets from port 1 to port 2 are allowed only if
associated to a flow initiated from port 2 to port 1.

## Port Knocking Firewall

This use case implements a port knocking firewall, a well-known method for opening a port
on a firewall. An IP host that wants to establish a connection (say an SSH session, i.e., port 22)
delivers a sequence of packets addressed to an ordered list of pre-defined closed ports, say ports
5123, 6234, 7345 and 8456. Once the exact sequence of packets is received, the firewall opens
port 22 for the considered host. Before this stage, all packets (including the knocking ones) are
dropped.
Starting from a DEFAULT state, 0, each correctly knocked port will cause a transition to a
series of three intermediate states, 1, 2 and 3, until a final OPEN state, 4, is reached. Any knock
on a different port will reset the state to DEFAULT. When in the OPEN state, only packets addressed
to port 22 will be forwarded; all remaining packets will be dropped, but without resetting
the state.

## Traffic Policer

This use case implements a single rate token bucket with burst size B and token rate 1/Q, where
Q is the token inter arrival time. Since in the current FlowBlaze architecture the update functions
are performed after the condition verification, we cannot update the number of tokens in the
bucket based on packet arrival time before evaluating the condition (token availability) for packet
forwarding. For this reason, we have implemented an alternative approximated algorithm based
on a sliding time window (Figure 3b). For each flow, a time window W(Tmin − Tmax) of
length BQ is maintained to represent the availability times of the tokens in the bucket. At each
packet arrival, if arrival time T now is within W (Case 1), at least one token is available and the
bucket is not full, so we shift W by Q to the right and forward packet. If the arrival time is after
Tmax (Case 2), the bucket is full, so packet is forwarded and W is moved to the right to reflect
that *B − 1* tokens are now available *(Tmin = T now−(B −1) ∗ Q and Tmax = T now+Q)*.
Finally, if the packet is received before Tmin (Case 3), no token is available, therefore W
is left unchanged and the packet is dropped. Upon receipt of the first flow packet, we make a
state transition in which we initialize the two registers: *Tmin = T now − (B − 1) ∗ Q* and
Tmax = T now + Q (initialization with full bucket).

