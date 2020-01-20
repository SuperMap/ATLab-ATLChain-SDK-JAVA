package com.supermap.blockchain.sdk;

import org.hyperledger.fabric.protos.peer.Query;
import org.hyperledger.fabric.sdk.BlockEvent;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.TransactionRequest;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.supermap.blockchain.sdk.Utils.getHexString;

public class SmChaincodeImpTest {
    private final String networkConfigFile = this.getClass().getResource("/network-config-testC.yaml").getFile();
    private final SmChain smChain = SmChain.getSmChain("txchannel", new File(networkConfigFile));

    private final String chaincodeName = "testCommon";
    private final String chaincodeVersion = "1.0";
    private final String chaincodePath = "/home/cy/Documents/ATL/SuperMap/ATLab-Chaincodes/java/Common";
    private final String chaincodeEndorsementPolicyFile = this.getClass().getResource("/chaincode-endorsement-policy.yaml").getFile();
    private final String channelName = "txchannel";

    @Test
    public void installTest() {
        boolean result = smChain.getSmChaincode().install(chaincodeName, chaincodeVersion, chaincodePath, TransactionRequest.Type.JAVA);
        Assert.assertTrue(result);
    }

    @Test
    public void instantiateTest() {
        CompletableFuture<BlockEvent.TransactionEvent> completableFuture = smChain.getSmChaincode().instantiate(chaincodeName, chaincodeVersion, chaincodePath, new File(chaincodeEndorsementPolicyFile), TransactionRequest.Type.JAVA);
        System.out.println(completableFuture.toString());
        Assert.assertTrue(completableFuture.isDone());
    }

    @Test
    public void upgradeTest() throws ExecutionException, InterruptedException {

        // 版本号自动递增
        String upgradeVersion = String.valueOf(getInstantiatedVersion(chaincodeName) + 1.0);
        boolean result = smChain.getSmChaincode().install(chaincodeName, upgradeVersion, chaincodePath, TransactionRequest.Type.JAVA);

        if (result) {
            CompletableFuture<BlockEvent.TransactionEvent> completableFuture = smChain.getSmChaincode().upgrade(chaincodeName, upgradeVersion, chaincodePath, new File(chaincodeEndorsementPolicyFile), TransactionRequest.Type.JAVA);

            // 处理交易返回事件
            completableFuture.thenApply(transactionEvent -> {
                Channel channel = smChain.getChannel();
                Peer peer = channel.getPeers().iterator().next();
                List<Query.ChaincodeInfo> list = smChain.getSmChaincode().listInstantiated(peer);
                for (Query.ChaincodeInfo info : list) {
                    if (chaincodeName.equals(info.getName())) {
                        Assert.assertTrue(upgradeVersion.equals(info.getVersion()));
                    }
                }
                return null;
            }).get();   // 等待事件处理完成
        }
    }

    @Test
    public void listInstalledTest() {
        Channel channel = smChain.getChannel();
        Peer peer = channel.getPeers().iterator().next();
        List<Query.ChaincodeInfo> list = smChain.getSmChaincode().listInstalled(peer);
        for (Query.ChaincodeInfo info : list) {
            if (chaincodeName.equals(info.getName())) {
                System.out.println(info);
                System.out.println("name: " + info.getName());
                System.out.println("version: " + info.getVersion());
                System.out.println("id: " + getHexString(info.getId().toByteArray()));
                System.out.println();
            }
        }
        Assert.assertTrue(list.size() > 0);
    }

    @Test
    public void listInstantiatedTest() {
        Channel channel = smChain.getChannel();
        Peer peer = channel.getPeers().iterator().next();
        List<Query.ChaincodeInfo> list = smChain.getSmChaincode().listInstantiated(peer);
        for (Query.ChaincodeInfo info : list) {
            if (chaincodeName.equals(info.getName())) {
                System.out.println("name: " + info.getName());
                System.out.println("version: " + info.getVersion());
            }
        }
        Assert.assertTrue(list.size() > 0);
    }

    @Test
    public void getInstantiatedVersionNull() {
        System.out.println(getInstantiatedVersion(null));
    }

    private double getInstantiatedVersion(String chaincodeName) {
        Channel channel = smChain.getChannel();
        Peer peer = channel.getPeers().iterator().next();
        List<Query.ChaincodeInfo> list = smChain.getSmChaincode().listInstantiated(peer);
        for (Query.ChaincodeInfo info : list) {
            if (chaincodeName.equals(info.getName())) {
                return Double.parseDouble(info.getVersion());
            }
        }
        return -1;
    }
}