package com.supermap.blockchain.sdk;


import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.Peer;
import org.junit.Test;

import java.io.File;

/**
 * @author liqs
 * @version 1.0
 * @date 2020/5/13 12:36
 * 节点加入通道测试
 */
public class SmAddPeerImpTest {

    private final String networkConfigFile = this.getClass().getResource("/blockchain-network-config.yaml").getFile();
    private final SmChain smChain ;
    private Channel channel;
    private HFClient hfClient;
    private SmAddPeerImp smAddPeerImp;

    public SmAddPeerImpTest()  {
        smChain = SmChain.getChain("channel3", new File(networkConfigFile));
        channel = smChain.getHFChannel();
        hfClient = smChain.getHfClient();
        smAddPeerImp = new SmAddPeerImp(hfClient, channel);
    }

    @Test
    public void channelAddPeer() throws Exception {


        // 根据 TLS 证书秘钥创建一个 peer
        String peerName = "peer0.orga.example.com";
        String peerGrpcURL = "grpcs://peer0.orga.example.com:7051";
        String pemFile = "D:\\crypto-config\\peerOrganizations\\orga.example.com\\peers\\peer0.orga.example.com\\tls\\server.crt";
        String clientKeyFile = "D:\\crypto-config\\peerOrganizations\\orga.example.com\\users\\Admin@orga.example.com\\tls\\server.key";
        String clientCertFile = "D:\\crypto-config\\peerOrganizations\\orga.example.com\\users\\Admin@orga.example.com\\tls\\server.crt";
        Peer peer = smAddPeerImp.creatPeerByTls(peerName, peerGrpcURL, pemFile, clientKeyFile, clientCertFile);

//        String peerName = "peer1.org4.example.com";
//        String peerGrpcURL = "grpcs://peer1.org4.example.com:14051";
//        String pemFile = "D:\\crypto-config-org4\\peerOrganizations\\org4.example.com\\peers\\peer1.org4.example.com\\tls\\server.crt";
//        String clientKeyFile = "D:\\crypto-config-org4\\peerOrganizations\\org4.example.com\\users\\Admin@org4.example.com\\tls\\server.key";
//        String clientCertFile = "D:\\crypto-config-org4\\peerOrganizations\\org4.example.com\\users\\Admin@org4.example.com\\tls\\server.crt";
//        Peer peer = smAddPeerImp.creatPeerByTls(peerName, peerGrpcURL, pemFile, clientKeyFile, clientCertFile);

        // peer 加入到通道里面
        channel.joinPeer(peer);

        System.out.println("");

    }
}