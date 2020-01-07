package com.atlchain.sdk;

import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.ChaincodeEndorsementPolicyParseException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.logging.Logger;

public class ATLChaincodeImp implements ATLChaincode {

    Logger logger = Logger.getLogger(ATLChaincodeImp.class.getName());

    private HFClient hfClient;
    private Channel channel;
    private ChaincodeID chaincodeID;

    public ATLChaincodeImp(HFClient hfClient, Channel channel){
        this.hfClient = hfClient;
        this.channel = channel;
    }

    @Override
    public String install() throws InvalidArgumentException, ProposalException {
        // 构造 InstallProposalRequest
        String chaincodeName = "chaincodeName";
        String chaincodeVersion = "1.0";
        String chaincodePath = "/path";
        chaincodeID = ChaincodeID.newBuilder()
                .setName(chaincodeName)
                .setPath(chaincodePath)
                .setVersion(chaincodeVersion)
                .build();
        InstallProposalRequest installProposalRequest = hfClient.newInstallProposalRequest();
        installProposalRequest.setChaincodeID(chaincodeID);
        installProposalRequest.setChaincodeSourceLocation(new File(chaincodePath));

        // 向通道中的节点发送 InstallProposalRequest
        Collection<ProposalResponse> proposalResponses = hfClient.sendInstallProposal(installProposalRequest, channel.getPeers());

        // 等待接收响应
        Collection<ProposalResponse> successful = new LinkedList<>();
        Collection<ProposalResponse> failed = new LinkedList<>();

        for (ProposalResponse response : proposalResponses) {
            if (response.getStatus() == ProposalResponse.Status.SUCCESS) {
                successful.add(response);
            } else {
                failed.add(response);
            }
        }

        // TODO: 链码安装失败节点的提示信息
        if (failed.size() > 0) {
            logger.warning("Not enough endorsers for install: " + successful.size() + ".  " + failed.iterator().next().getMessage());
        }

        return null;
    }

    @Override
    public String instantiate(File chaincodeEndorsementPolicyFile) throws IOException, ChaincodeEndorsementPolicyParseException {
        ChaincodeEndorsementPolicy chaincodeEndorsementPolicy = new ChaincodeEndorsementPolicy();
        chaincodeEndorsementPolicy.fromYamlFile(chaincodeEndorsementPolicyFile);

        // 实例化链码
        InstantiateProposalRequest instantiateProposalRequest = hfClient.newInstantiationProposalRequest();
        instantiateProposalRequest.setChaincodeLanguage(TransactionRequest.Type.JAVA);
        instantiateProposalRequest.setChaincodeID(chaincodeID);
        instantiateProposalRequest.setFcn("init");
        instantiateProposalRequest.setChaincodeEndorsementPolicy(chaincodeEndorsementPolicy);
        instantiateProposalRequest.setProposalWaitTime(12000L);
        return null;
    }

    @Override
    public String upgrade() {
        return null;
    }

    @Override
    public String[] list() {
        return new String[0];
    }

    @Override
    public String pack() {
        return null;
    }

    @Override
    public String signpackage() {
        return null;
    }
}
