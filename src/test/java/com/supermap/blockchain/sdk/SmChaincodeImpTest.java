package com.supermap.blockchain.sdk;

import org.hyperledger.fabric.protos.peer.Query;
import org.hyperledger.fabric.sdk.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SmChaincodeImpTest {
    private final String networkConfigFile = this.getClass().getResource("/network-config-testC.yaml").getFile();
    private final SmChain smChain = SmChain.getSmChain("txchannel", new File(networkConfigFile));

    private final String chaincodeName = "testCommon";
    private final String chaincodeVersion = "1.5";
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
    public void upgradeTest() {
        String upgradeVersion = "1.006";
        boolean result = smChain.getSmChaincode().install(chaincodeName, upgradeVersion, chaincodePath, TransactionRequest.Type.JAVA);

        if (result) {
            CompletableFuture<BlockEvent.TransactionEvent> completableFuture = smChain.getSmChaincode().upgrade(chaincodeName, upgradeVersion, chaincodePath, new File(chaincodeEndorsementPolicyFile), TransactionRequest.Type.JAVA);
            // TODO 交易响应结果处理
//            final Collection<CompletableFuture<BlockEvent.TransactionEvent>> futures = new ArrayList<>(1);
//            final CompletableFuture<Void> voidCompletableFuture = CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
//            voidCompletableFuture.thenApply(avoid -> {
//
//                ArrayList<Thread> threads = new ArrayList<>();
//                TestPair[] testPairs = {new TestPair(fooChannel, sampleOrg1), new TestPair(barChannel, sampleOrg2)};
//
//                for (int i = 0; i < WORKER_COUNT; ++i) {
//
//                    Thread thread = new Thread(new Worker(i, client, testPairs));
//                    thread.setName("TCW_" + i);
//                    thread.setDaemon(true);
//                    thread.start();
//
//                    threads.add(thread);
//
//                    try {
//                        Thread.sleep(random.nextInt(3000)); // stage them to be doing different tasks
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//
//                threads.forEach(t -> {
//                    try {
//                        t.join();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                });
//
//                return null;
//
//            }).get();
//            System.out.println(completableFuture.toString());
//            System.out.println(completableFuture.getNumberOfDependents());
//            System.out.println(completableFuture.isCancelled());
//            System.out.println(completableFuture.isCompletedExceptionally());
//            System.out.println(completableFuture.isDone());
            Assert.assertNotNull(completableFuture.toString());
        }
        Assert.assertTrue(result);
    }

    @Test
    public void listInstalledTest() {
        Channel channel = smChain.getChannel();
        Peer peer = channel.getPeers().iterator().next();
        List<Query.ChaincodeInfo> list = smChain.getSmChaincode().listInstalled(peer);
        for (Query.ChaincodeInfo info : list) {
            if ("testCommon".equals(info.getName())) {
                System.out.println(info);
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
            if ("testCommon".equals(info.getName())) {
                System.out.println(info);
            }
        }
        Assert.assertTrue(list.size() > 0);
    }

//    class TestPair {
//        public Channel getChannel() {
//            return channel;
//        }
//
//        public SampleOrg getSampleOrg() {
//            return sampleOrg;
//        }
//
//        final Channel channel;
//        final SampleOrg sampleOrg;
//
//        TestPair(Channel channel, SampleOrg sampleOrg) {
//            this.channel = channel;
//            this.sampleOrg = sampleOrg;
//        }
//    }
//
//    class Worker implements Runnable {
//
//        private final int id;
//        private final HFClient client;
//        private final TestPair[] testPairs;
//        private int[] start;
////        private final Channel channel;
////        private SampleOrg sampleOrg;
//
//        Worker(int id, HFClient client, TestPair... testPairs) {
//            this.id = id;
//            this.client = client;
//            this.testPairs = testPairs;
//
//        }
//
//        @Override
//        public void run() {
//            start = new int[testPairs.length];
//            for (int i = 0; i < start.length; i++) {
//                start[i] = 200;
//
//            }
//
//            for (int i = 0; i < 200000000; ++i) {
//                out("Worker %d doing run: %d", id, i);
//                int moveAmount = random.nextInt(9) + 1;
//
//                int whichChannel = random.nextInt(testPairs.length);
//
//                runChannel(client, testPairs[whichChannel].getChannel(), id, i, testPairs[whichChannel].getSampleOrg(), moveAmount, start[whichChannel]);
//
//                start[whichChannel] += moveAmount;
//            }
//
//        }
//    }
}