package com.supermap.blockchain.sdk;


import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.NetworkConfig;
import org.hyperledger.fabric.sdk.security.CryptoSuite;

import java.io.File;

/**
 * ATLChain 客户端
 */
public class SmChain {
    private HFClient hfClient;
    private Channel channel;
    private NetworkConfig networkConfig;


    /**
     * 通过 yaml 配置文件实例化
     * @param networkConfigFile
     */
    private SmChain(String channelName, File networkConfigFile) {
        try {
            networkConfig = NetworkConfig.fromYamlFile(networkConfigFile);
            hfClient = HFClient.createNewInstance();
            hfClient.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
            hfClient.setUserContext(networkConfig.getPeerAdmin());
            channel = hfClient.loadChannelFromConfig(channelName, networkConfig);
            channel.initialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Channel getChannel() {
        return channel;
    }

    public static SmChain getSmChain(String channelName, File networkConfigFile) {
        return new SmChain(channelName, networkConfigFile);
    }

    public SmTransaction getSmTransaction() {
        return new SmTransactionImp(hfClient, channel);
    }

    public SmChaincode getSmChaincode() {

        return new SmChaincodeImp(hfClient, channel);
    }

    public SmChannel getSmChannel() {
        return new SmChannelImp(hfClient, channel);
    }

    public SmCA getSmCa(String OrgName) {
        return new SmCAImp(networkConfig, OrgName);
    }
}
