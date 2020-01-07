package com.atlchain.sdk;

import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class ATLTransactionImp implements ATLTransaction {
    private HFClient hfClient;
    private Channel channel;

    public ATLTransactionImp(HFClient hfClient, Channel channel) {
        this.hfClient = hfClient;
        this.channel = channel;
    };

    /**
     * 查询
     * @param chaincodeName 链码名称
     * @param functionName  方法名称
     * @param args  参数
     * @return  查询结果
     */
    @Override
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
    @Override
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
    @Override
    public String invoke(String chaincodeName, String functionName, String[] args) {
        Collection<ProposalResponse> proposalResponses = null;
        TransactionProposalRequest transactionProposalRequest = Utils.getTransactionProposalRequest(hfClient, chaincodeName, functionName, args);
        try {
            // 向endoser发送交易，成功后返回要发往orderer的提案
            proposalResponses = channel.sendTransactionProposal(transactionProposalRequest);

            // 向orderer发送背书后的交易提案，成功后返回一个区块Event
            CompletableFuture<BlockEvent.TransactionEvent> completableFuture = channel.sendTransaction(proposalResponses);
            System.out.println(completableFuture);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (proposalResponses == null) {
            return "No Response";
        }

        // 获取返回结果
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
    @Override
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
}
