package com.atlchain.sdk;


import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * ATLChain 客户端
 */
public class ATLChain {
    private String peerName;
    private String peerURL;
    private String ordererName;
    private String ordererURL;
    private HFClient hfClient;
    private Channel channel;

    private File certFile = new File("/home/cy/Documents/ATL/SuperMap/ATLab-ATLChain-SDK-JAVA/src/main/resources/certs/user/cert.pem");
    private File keyFile = new File("/home/cy/Documents/ATL/SuperMap/ATLab-ATLChain-SDK-JAVA/src/main/resources/certs/user/user_sk");

    // TODO 使用配置文件设置参数
    public ATLChain(File certFile, File keyFile, String peerName, String peerURL, String mspId, String userName, String ordererName, String ordererURL, String channelName) {
        this.peerName = peerName;
        this.peerURL = peerURL;
        this.ordererName = ordererName;
        this.ordererURL = ordererURL;
        this.hfClient = Utils.getHFClient(keyFile, certFile, mspId, userName);
        this.channel = Utils.getChannel(hfClient, channelName, peerName, peerURL, ordererName, ordererURL);
    }

    public ATLChain(File networkFile) {
        try {
            NetworkConfig networkConfig = NetworkConfig.fromYamlFile(networkFile);
            String channelName = networkConfig.getChannelNames().iterator().next();
            this.hfClient = HFClient.createNewInstance();
            this.hfClient.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
            this.hfClient.setUserContext(networkConfig.getPeerAdmin());
            this.channel = hfClient.loadChannelFromConfig(channelName, networkConfig);
            channel.initialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询
     * @param chaincodeName 链码名称
     * @param functionName  方法名称
     * @param args  参数
     * @return  查询结果
     */
    public String query( String chaincodeName, String functionName, String[] args) {
        TransactionProposalRequest queryByChaincodeRequest = Utils.getTransactionProposalRequest(hfClient, chaincodeName, functionName, args);
        Collection<ProposalResponse> proposalResponses = null;
        try {
            // 发送交易提案
            proposalResponses = channel.sendTransactionProposal(queryByChaincodeRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (proposalResponses == null) {
            return "No Response";
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (ProposalResponse res : proposalResponses) {
            try {
                stringBuilder.append(new String(res.getChaincodeActionResponsePayload()));
            } catch (InvalidArgumentException e) {
                e.printStackTrace();
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 查询键为 byte[] 格式的值
     * @param chaincodeName 链码名称
     * @param functionName  方法名称
     * @param args  参数
     * @return  查询结果
     */
    public byte[][] queryByte(String chaincodeName, String functionName, byte[][] args) {
        TransactionProposalRequest queryByChaincodeRequest = Utils.getTransactionProposalRequest(hfClient, chaincodeName, functionName, args);
        Collection<ProposalResponse> proposalResponses = null;
        try {
            // 发送交易提案
            proposalResponses = channel.sendTransactionProposal(queryByChaincodeRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (proposalResponses == null) {
            return new byte[][]{"No Response".getBytes()};
        }

        // 将结果构造为 byte[][]
        ArrayList<byte[]> byteArrayList = new ArrayList<>();
        for (ProposalResponse res : proposalResponses) {
            try {
                byteArrayList.add(res.getChaincodeActionResponsePayload());
            } catch (InvalidArgumentException e) {
                e.printStackTrace();
            }
        }
        byte[][] bytes = byteArrayList.toArray(new byte[1][byteArrayList.size()]);

        return bytes;
    }

    /**
     * 执行链码
     * @param chaincodeName 链码名称
     * @param functionName  方法名称
     * @param args  参数
     * @return  执行结果
     */
    public String invoke(String chaincodeName, String functionName, String[] args) {
        Collection<ProposalResponse> proposalResponses = null;
        TransactionProposalRequest transactionProposalRequest = Utils.getTransactionProposalRequest(hfClient, chaincodeName, functionName, args);
        try {
            // 发送交易提案
            proposalResponses = channel.sendTransactionProposal(transactionProposalRequest);

            // 发送交易
            CompletableFuture<BlockEvent.TransactionEvent> completableFuture = channel.sendTransaction(proposalResponses);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (proposalResponses == null) {
            return "No Response";
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (ProposalResponse res : proposalResponses) {
            stringBuilder.append(res.getMessage());
        }
        return stringBuilder.toString();
    }

    /**
     * 执行链码，参数为 byte[] 格式
     * @param chaincodeName 链码名称
     * @param functionName  方法名称
     * @param args  参数
     * @return  执行结果
     */
    public String invokeByte(String chaincodeName, String functionName, byte[][] args) {
        Collection<ProposalResponse> proposalResponses = null;
        TransactionProposalRequest transactionProposalRequest = Utils.getTransactionProposalRequest(hfClient, chaincodeName, functionName, args);
        try {
            // 发送交易提案
            proposalResponses = channel.sendTransactionProposal(transactionProposalRequest);

            // 发送交易
            CompletableFuture<BlockEvent.TransactionEvent> completableFuture = channel.sendTransaction(proposalResponses);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (proposalResponses == null) {
            return "No Response";
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (ProposalResponse res : proposalResponses) {
            stringBuilder.append(res.getMessage());
        }
        return stringBuilder.toString();
    }

    // TODO 获取通道所有链码
    public List<String> getChaincodeList() {
        return null;
    }
}
