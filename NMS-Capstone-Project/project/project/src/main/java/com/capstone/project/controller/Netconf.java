//package com.capstone.project.controller;
//import org.opendaylight.netconf.client.NetconfClientSession;
//import org.opendaylight.netconf.client.conf.NetconfClientConfiguration;
//import org.opendaylight.netconf.client.mina.NetconfClientMinaImpl;
//public class Netconf {
// public static void main(String[] args) {
// NetconfClientConfiguration clientConfig = ... // Configure client settings
// NetconfClient client = new NetconfClientMinaImpl();
// NetconfClientSession session = client.connect(clientConfig);
// 
// String getConfigRequest = "<rpc message-id=\"1\"><get-config><source><running/></
//source></get-config></rpc>";
// String response = session.sendRpc(getConfigRequest);
// 
// System.out.println("Response: " + response);
// }
//}