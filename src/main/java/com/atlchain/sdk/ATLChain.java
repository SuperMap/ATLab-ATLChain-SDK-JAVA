package com.atlchain.sdk;

import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Collection;

public class ATLChain {
    private File cert;
    private File key;
    private String peerName;
    private String peerURL;
    private String mspId;
    private String userName;
    private String ordererName;
    private String ordererURL;
    private HFClient hfClient;
    private Channel channel;

    public ATLChain(File cert, File key, String peerName, String peerURL, String mspId, String userName) {
        this.cert = cert;
        this.key = key;
        this.peerName = peerName;
        this.peerURL = peerURL;
        this.mspId = mspId;
        this.userName = userName;
        this.ordererName = "";
        this.ordererURL = "";
    }

    public ATLChain(File cert, File key, String peerName, String peerURL, String mspId, String userName, String ordererName, String ordererURL) {
        this.cert = cert;
        this.key = key;
        this.peerName = peerName;
        this.peerURL = peerURL;
        this.mspId = mspId;
        this.userName = userName;
        this.ordererName = ordererName;
        this.ordererURL = ordererURL;
    }

    public String query(String channelName, String chaincodeName, String functionName, String[] args) {
        this.channel = getChannel(channelName);
        Peer peer = null;

        QueryByChaincodeRequest queryByChaincodeRequest = getQueryByChaincodeRequest(chaincodeName, functionName, args);
        Collection<ProposalResponse> proposalResponses = null;
        try {
            peer = getPeer();
            this.channel.addPeer(peer);
            this.channel.initialize();
            proposalResponses = this.channel.queryByChaincode(queryByChaincodeRequest);
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
        } catch (TransactionException e) {
            e.printStackTrace();
        } catch (ProposalException e) {
            e.printStackTrace();
        }

        StringBuilder stringBuilder = new StringBuilder();

        for (ProposalResponse res : proposalResponses) {
            stringBuilder.append(res.getMessage());
        }

        return stringBuilder.toString();
    }

    public String invoke(String channelName, String chaincodeName, String functionName, String[] args) {
        channel = getChannel(channelName);
        Peer peer = null;
        Orderer orderer = null;
        Collection<ProposalResponse> proposalResponses = null;
        TransactionProposalRequest transactionProposalRequest = getTransactionProposalRequest(chaincodeName, functionName, args);
        try {
            peer = getPeer();
            channel.addPeer(peer);
            orderer = getOrderer();
            channel.addOrderer(orderer);
            channel.initialize();
            proposalResponses = channel.sendTransactionProposal(transactionProposalRequest);
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
        } catch (TransactionException e) {
            e.printStackTrace();
        } catch (ProposalException e) {
            e.printStackTrace();
        }

        StringBuilder stringBuilder = new StringBuilder();

        for (ProposalResponse res : proposalResponses) {
            stringBuilder.append(res.getMessage());
        }

        return stringBuilder.toString();
    }

    private Peer getPeer() throws InvalidArgumentException {
        return hfClient.newPeer(peerName, peerURL);
    }

    private Orderer getOrderer() throws InvalidArgumentException {
        return hfClient.newOrderer(ordererName, ordererURL);
    }

    private Channel getChannel(String channelName) {
        hfClient = HFClient.createNewInstance();
        Channel channel = null;
        try {
            hfClient.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
            hfClient.setUserContext(getUserContext());
            channel = hfClient.newChannel(channelName);
        } catch (CryptoException e) {
            e.printStackTrace();
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return channel;
    }

    private QueryByChaincodeRequest getQueryByChaincodeRequest(String chaincodeName, String functionName, String[] args) {
        QueryByChaincodeRequest queryByChaincodeRequest = hfClient.newQueryProposalRequest();
        queryByChaincodeRequest.setFcn(functionName);
        queryByChaincodeRequest.setArgs(args);
        queryByChaincodeRequest.setChaincodeID(ChaincodeID.newBuilder().setName(chaincodeName).build());
        return queryByChaincodeRequest;
    }

    private TransactionProposalRequest getTransactionProposalRequest(String chaincodeName, String functionName, String[] args) {
        TransactionProposalRequest transactionProposalRequest = hfClient.newTransactionProposalRequest();
        transactionProposalRequest.setFcn(functionName);
        transactionProposalRequest.setArgs(args);
        transactionProposalRequest.setChaincodeID(ChaincodeID.newBuilder().setName(chaincodeName).build());
//        transactionProposalRequest.setChaincodeLanguage(TransactionRequest.Type.JAVA);
        return transactionProposalRequest;
    }

    private UserContext getUserContext() {
        UserContext userContext = new UserContext();
        userContext.setMspId(mspId);
        userContext.setName(userName);
        UserEnrollment userEnrollment = null;
        try {
            userEnrollment = getUserEnrollment(key.getPath() ,cert.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        userContext.setEnrollment(userEnrollment);
        return userContext;
    }

    private UserEnrollment getUserEnrollment(String keyFile, String certFile)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        PrivateKey key = null;
        String certificate = null;
        InputStream isKey = null;
        BufferedReader brKey = null;

        try {
            isKey = new FileInputStream(keyFile);
            brKey = new BufferedReader(new InputStreamReader(isKey));
            StringBuilder keyBuilder = new StringBuilder();

            for (String line = brKey.readLine(); line != null; line = brKey.readLine()) {
                if (line.indexOf("PRIVATE") == -1) {
                    keyBuilder.append(line);
                }
            }

            certificate = new String(Files.readAllBytes(Paths.get(certFile)));

            byte[] encoded = DatatypeConverter.parseBase64Binary(keyBuilder.toString());
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
            KeyFactory kf = KeyFactory.getInstance("EC");
            key = kf.generatePrivate(keySpec);
        } finally {
            isKey.close();
            brKey.close();
        }

        UserEnrollment enrollment = new UserEnrollment(key, certificate);
        return enrollment;
    }
}
