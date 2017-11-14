# Andrew Katsanevas
# 4/22/2017
# CS 4480 PA3
# Load balancing switch
from ryu.base import app_manager
from ryu.controller import ofp_event
from ryu.controller.handler import CONFIG_DISPATCHER, MAIN_DISPATCHER
from ryu.controller.handler import set_ev_cls
from ryu.ofproto import ofproto_v1_3, ether
from ryu.lib.packet import packet
from ryu.lib.packet import ethernet
from ryu.lib.packet import ether_types
from ryu.lib.packet import arp


class MyLoadBalanceSwitch(app_manager.RyuApp):
    OFP_VERSIONS = [ofproto_v1_3.OFP_VERSION]

    # Switches between servers h5 and h6
    switchBoolean = True;
    
    # Initialize
    def __init__(self, *args, **kwargs):
        super(MyLoadBalanceSwitch, self).__init__(*args, **kwargs)
        self.mac_to_port = {}

    # From simple_switch_13.py
    @set_ev_cls(ofp_event.EventOFPSwitchFeatures, CONFIG_DISPATCHER)
    def switch_features_handler(self, ev):
        datapath = ev.msg.datapath
        ofproto = datapath.ofproto
        parser = datapath.ofproto_parser

        # install table-miss flow entry
        match = parser.OFPMatch()
        actions = [parser.OFPActionOutput(ofproto.OFPP_CONTROLLER, ofproto.OFPCML_NO_BUFFER)]
        self.add_flow(datapath, 0, match, actions)

    # Add new flows when an ARP is received 
    # From simple_switch_13.py
    def add_flow(self, datapath, priority, match, actions, buffer_id=None):
        ofproto = datapath.ofproto
        parser = datapath.ofproto_parser
        inst = [parser.OFPInstructionActions(ofproto.OFPIT_APPLY_ACTIONS, actions)]
        if buffer_id:
            mod = parser.OFPFlowMod(datapath=datapath, buffer_id=buffer_id, priority=priority, match=match, instructions=inst)
        else:
            mod = parser.OFPFlowMod(datapath=datapath, priority=priority, match=match, instructions=inst)
        datapath.send_msg(mod)

    # Helper method for creating and sending an ARP packet
    def send_arp(self, datapath, srcMac, srcIp, dstMac, dstIp, outPort):
        # Create the ARP packet
        e = ethernet.ethernet(dstMac, srcMac, ether.ETH_TYPE_ARP)
        a = arp.arp(1, 0x0800, 6, 4, 2, srcMac, srcIp, dstMac, dstIp)
        p = packet.Packet()
        p.add_protocol(e)
        p.add_protocol(a)
        p.serialize()

        # Send the ARP
        actions = [datapath.ofproto_parser.OFPActionOutput(outPort, 0)]
        out = datapath.ofproto_parser.OFPPacketOut(datapath=datapath, buffer_id=0xffffffff, in_port=datapath.ofproto.OFPP_CONTROLLER, actions=actions, data=p.data)
        datapath.send_msg(out)

    # When a packet is received
    # Modified from simple_switch_13.py
    @set_ev_cls(ofp_event.EventOFPPacketIn, MAIN_DISPATCHER)
    def _packet_in_handler(self, ev):
        # If you hit this you might want to increase
        # the "miss_send_length" of your switch
        if ev.msg.msg_len < ev.msg.total_len:
            self.logger.debug("packet truncated: only %s of %s bytes", ev.msg.msg_len, ev.msg.total_len)
        
        # Get attributes of the message
        msg = ev.msg
        datapath = msg.datapath
        ofproto = datapath.ofproto
        parser = datapath.ofproto_parser
        in_port = msg.match['in_port']

        pkt = packet.Packet(msg.data)
        eth = pkt.get_protocols(ethernet.ethernet)[0]

        # Get the arp packet
        arp_pkt = pkt.get_protocol(arp.arp)

        # Make sure it's an ARP packet
        if(arp_pkt is None):
            return

        # ignore lldp packet
        if eth.ethertype == ether_types.ETH_TYPE_LLDP:
            return

        # Get the ethernet destination and source
        dst = eth.dst
        src = eth.src

        # Put the datapath id in the mac_to_port list
        dpid = datapath.id
        self.mac_to_port.setdefault(dpid, {})

        # Print packet info
        self.logger.info("packet in %s %s %s %s", dpid, src, dst, in_port)

        # learn a mac address to avoid FLOOD next time.
        self.mac_to_port[dpid][src] = in_port


        if dst in self.mac_to_port[dpid]:
            out_port = self.mac_to_port[dpid][dst]
        else:
            # Make flooding the default protocol
            out_port = ofproto.OFPP_FLOOD

            # Get the source IP and MAC addresses from the ARP packet
            arp_src_ip = arp_pkt.src_ip
            arp_src_mac = arp_pkt.src_mac

            # Make sure destination is s1
            if(arp_pkt.dst_ip == '10.0.0.10'):
                # Send to h5
                if(self.switchBoolean):
                    out_port = 5
                    arp_dest_ip = '10.0.0.5'
                    arp_dest_mac = '00:00:00:00:00:05'
                    self.switchBoolean = False
                # Send to h6
                else:
                    out_port = 6
                    arp_dest_ip = '10.0.0.6'
                    arp_dest_mac = '00:00:00:00:00:06'
                    self.switchBoolean = True
                    
                # Add a flow in the sent direction
                actions = [parser.OFPActionSetField(ipv4_dst=arp_dest_ip), parser.OFPActionOutput(out_port)]
                match = parser.OFPMatch(in_port=in_port, ipv4_dst='10.0.0.10', eth_type=0x0800)
                self.add_flow(datapath, 2, match, actions, msg.buffer_id)

                # Add a flow in the opposite direction
                actions = [parser.OFPActionSetField(ipv4_src='10.0.0.10'), parser.OFPActionOutput(in_port)]
                match = parser.OFPMatch(in_port=out_port, ipv4_src=arp_dest_ip, ipv4_dst=arp_src_ip, eth_type=0x0800)
                self.add_flow(datapath, 2, match, actions, msg.buffer_id)

                # Send the arp packet
                self.send_arp(datapath, arp_dest_mac, '10.0.0.10', arp_src_mac, arp_src_ip, in_port)
                return



        actions = [parser.OFPActionOutput(out_port)]
        # install a flow to avoid packet_in next time
        if out_port != ofproto.OFPP_FLOOD:
            match = parser.OFPMatch(in_port=in_port, eth_dst=dst)
            # verify if we have a valid buffer_id, if yes avoid to send both
            # flow_mod & packet_out
            if msg.buffer_id != ofproto.OFP_NO_BUFFER:
                self.add_flow(datapath, 1, match, actions, msg.buffer_id)
                return
            else:
                self.add_flow(datapath, 1, match, actions)
        data = None
        if msg.buffer_id == ofproto.OFP_NO_BUFFER:
            data = msg.data

        out = parser.OFPPacketOut(datapath=datapath, buffer_id=msg.buffer_id, in_port=in_port, actions=actions, data=data)
        datapath.send_msg(out)
