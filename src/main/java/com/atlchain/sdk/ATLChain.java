package com.atlchain.sdk;


import org.hyperledger.fabric.sdk.*;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
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

    public ATLChain(File certFile, File keyFile, String peerName, String peerURL, String mspId, String userName, String ordererName, String ordererURL) {
        this.peerName = peerName;
        this.peerURL = peerURL;
        this.ordererName = ordererName;
        this.ordererURL = ordererURL;
        this.hfClient = Utils.getHFClient(keyFile, certFile, mspId, userName);
    }

    /**
     * 查询
     * @param channelName   通道名称
     * @param chaincodeName 链码名称
     * @param functionName  方法名称
     * @param args  参数
     * @return  查询结果
     */
    public String query(String channelName, String chaincodeName, String functionName, String[] args) {
        Channel channel = Utils.getChannel(hfClient, channelName, peerName, peerURL, ordererName, ordererURL);
        TransactionProposalRequest queryByChaincodeRequest = Utils.getTransactionProposalRequest(hfClient, chaincodeName, functionName, args);
        Collection<ProposalResponse> proposalResponses = null;
        try {
            // 发送交易提案
            proposalResponses = channel.sendTransactionProposal(queryByChaincodeRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (ProposalResponse res : proposalResponses) {
            stringBuilder.append(res.getMessage());
        }
        return stringBuilder.toString();
    }

    /**
     * 查询键为 byte[] 格式的值
     * @param channelName   通道名称
     * @param chaincodeName 链码名称
     * @param functionName  方法名称
     * @param args  参数
     * @return  查询结果
     */
    public byte[][] queryByte(String channelName, String chaincodeName, String functionName, byte[][] args) {
        Channel channel = Utils.getChannel(hfClient, channelName, peerName, peerURL, ordererName, ordererURL);
        TransactionProposalRequest queryByChaincodeRequest = Utils.getTransactionProposalRequest(hfClient, chaincodeName, functionName, args);
        Collection<ProposalResponse> proposalResponses = null;
        try {
            // 发送交易提案
            proposalResponses = channel.sendTransactionProposal(queryByChaincodeRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 将结果构造为 byte[][]
        ArrayList<byte[]> byteArrayList = new ArrayList<>();
        for (ProposalResponse res : proposalResponses) {
            byteArrayList.add(res.getProposalResponse().getResponse().getPayload().toByteArray());
        }
        byte[][] bytes = byteArrayList.toArray(new byte[1][byteArrayList.size()]);

        return bytes;
    }

    /**
     * 执行链码
     * @param channelName   通道名称
     * @param chaincodeName 链码名称
     * @param functionName  方法名称
     * @param args  参数
     * @return  执行结果
     */
    public String invoke(String channelName, String chaincodeName, String functionName, String[] args) {
        Channel channel = Utils.getChannel(hfClient, channelName, peerName, peerURL, ordererName, ordererURL);
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

        StringBuilder stringBuilder = new StringBuilder();
        for (ProposalResponse res : proposalResponses) {
            stringBuilder.append(res.getMessage());
        }
        return stringBuilder.toString();
    }

    /**
     * 执行链码，参数为 byte[] 格式
     * @param channelName   通道名称
     * @param chaincodeName 链码名称
     * @param functionName  方法名称
     * @param args  参数
     * @return  执行结果
     */
    public String invokeByte(String channelName, String chaincodeName, String functionName, byte[][] args) {
        Channel channel = Utils.getChannel(hfClient, channelName, peerName, peerURL, ordererName, ordererURL);
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

        StringBuilder stringBuilder = new StringBuilder();
        for (ProposalResponse res : proposalResponses) {
            stringBuilder.append(res.getMessage());
        }
        return stringBuilder.toString();
    }

}
