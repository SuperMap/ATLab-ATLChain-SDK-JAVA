package com.atlchain.sdk;


import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.NetworkConfig;
import org.hyperledger.fabric.sdk.security.CryptoSuite;

import java.io.File;

/**
 * ATLChain 客户端
 */
public class ATLChain {
    private HFClient hfClient;
    private Channel channel;

    /**
     * 通过 yaml 配置文件实例化
     * @param networkConfigFile
     */
    private ATLChain(String channelName, File networkConfigFile) {
        try {
            NetworkConfig networkConfig = NetworkConfig.fromYamlFile(networkConfigFile);
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

    public static ATLChain getATLChain(String channelName, File networkConfigFile) {
        return new ATLChain(channelName, networkConfigFile);
    }

    public ATLTransaction getAtlTransaction() {
        return new ATLTransactionImp(hfClient, channel);
    }

    public ATLChaincode getATLChaincode() {

        return new ATLChaincodeImp(hfClient, channel);
    }

    public ATLChannel getATLChannel() {
        return new ATLChannelImp();
    }

    public ATLCA getATLCa() {
        return new ATLCAImp();
    }
}
