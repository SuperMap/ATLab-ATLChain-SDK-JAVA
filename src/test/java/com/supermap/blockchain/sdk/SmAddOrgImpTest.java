package com.supermap.blockchain.sdk;

import com.alibaba.fastjson.JSONObject;
import org.hyperledger.fabric.sdk.*;
import org.junit.Test;

import java.io.*;
import java.util.*;

public class SmAddOrgImpTest {

    private final String networkConfigFile = this.getClass().getResource("/blockchain-network-config.yaml").getFile();
    private final SmChain smChain ;
    private Channel channel;
    private HFClient hfClient;
    private SmAddOrgImp smAddOrgImp;

    public SmAddOrgImpTest() {
        String channelName = "txchannel";
        smChain = SmChain.getChain(channelName, new File(networkConfigFile));
        channel = smChain.getHFChannel();
        hfClient = smChain.getHfClient();
        smAddOrgImp = new SmAddOrgImp(hfClient, channel);
    }

    // 新的组织加入通道
    @Test
    public void test() throws Exception {

        //  构建更新信息 UpdateChannelConfiguration
        UpdateChannelConfiguration updateChannelConfiguration = smAddOrgImp.getupdateChannelConfiguration("E:\\org4.json", "Org4MSP");

        // 构造 peerUser 签名
        String userMspId = "OrgA";
        String userName = "peer0.orga.example.com";
        String keyFile = "D:\\crypto-config\\peerOrganizations\\orga.example.com\\users\\Admin@orga.example.com\\msp\\keystore\\315edd7722516c1b2770891587773dbcd52ebd87736ed946bb8ddc7555421788_sk";
        String certFile = "D:\\crypto-config\\peerOrganizations\\orga.example.com\\users\\Admin@orga.example.com\\msp\\signcerts\\Admin@orga.example.com-cert.pem";
        User peeruser = smAddOrgImp.getUser(userMspId, userName, keyFile, certFile);
        byte[] peerSignature = hfClient.getUpdateChannelConfigurationSignature(updateChannelConfiguration, peeruser);

        // 构造 ordererUser 签名
        String ordererMspId = "OrgOrderer";
        String ordererName = "orderer.example.com";
        String ordererKeyFile = "D:\\crypto-config\\ordererOrganizations\\example.com\\users\\Admin@example.com\\msp\\keystore\\3b0663a6cce1ea95a0a1ddbea1f7f1e9ed9f531611b23f214c0cdf0108c37aeb_sk";
        String ordererCertFile = "D:\\crypto-config\\ordererOrganizations\\example.com\\users\\Admin@example.com\\msp\\signcerts\\Admin@example.com-cert.pem";
        User ordereruser = smAddOrgImp.getUser(ordererMspId, ordererName, ordererKeyFile, ordererCertFile);
        byte[] ordererSignature = hfClient.getUpdateChannelConfigurationSignature(updateChannelConfiguration, ordereruser);

        // 提交签名  完成更改配置信息 将新的组织加入到通道里面
        channel.updateChannelConfiguration(updateChannelConfiguration, ordererSignature, peerSignature);
        System.out.println("");

        // 由于此方法没有返回值，所以看信息更新成功与否，直接在获取通道配置信息，然后转为Json查看更新与否即可
        // 第一步  得到通道配置信息 并转为 json 格式
        byte[] channelConfigurationBytes = channel.getChannelConfigurationBytes();
        JSONObject oldChannelConfigJson = smAddOrgImp.getDecodeData(channelConfigurationBytes);
        System.out.println("");
    }
}