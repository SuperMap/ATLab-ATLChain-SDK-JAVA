package com.supermap.blockchain.sdk;

import org.hyperledger.fabric.sdk.Peer;
import org.junit.Test;

import java.io.File;
import java.util.Set;

public class SmChannelImpTest {
    private final String networkConfigFile = this.getClass().getResource("/network-config-testC.yaml").getFile();
    private final SmChain smChain = SmChain.getSmChain("txchannel", new File(networkConfigFile));
    private Peer peer = smChain.getChannel().getPeers().iterator().next();

    @Test
    public void listTest() {
        Set<String> channels = smChain.getSmChannel().listChannelOfPeerJoined(peer);
        for (String str : channels) {
            System.out.println(str);
        }
    }


    @Test
    public void getBlockchainInfoTest() {
        SmBlockchainInfo smBlockchainInfo = smChain.getSmChannel().getBlockchainInfo();
        System.out.println(smBlockchainInfo.getCurrentBlockHash());
        System.out.println(smBlockchainInfo.getPreviousBlockHash());
        System.out.println(smBlockchainInfo.getHeight());
    }

    @Test
    public void get() {
        SmBlockInfo smBlockInfo = smChain.getSmChannel().getBlockInfoByNumber(peer, 10000L);
        System.out.println(smBlockInfo.getCurrentBlockHash());
        System.out.println(smBlockInfo.getPreviousBlockHash());
    }
}