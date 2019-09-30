package com.atlchain.sdk;

import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.security.CryptoSuite;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

class Utils {

    static UserContext getUserContext(File keyFile, File certFile, String mspId, String userName) {
        UserContext userContext = new UserContext();
        userContext.setMspId(mspId);
        userContext.setName(userName);
        UserEnrollment userEnrollment = null;
        try {
            userEnrollment = getUserEnrollment(keyFile ,certFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        userContext.setEnrollment(userEnrollment);
        return userContext;
    }

    static UserContext getUserContext(Enrollment enrollment, String mspId, String userName) {
        UserContext userContext = new UserContext();
        userContext.setMspId(mspId);
        userContext.setName(userName);
        userContext.setEnrollment(enrollment);
        return userContext;
    }

    static UserEnrollment getUserEnrollment(File keyFile, File certFile) {
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

            certificate = new String(Files.readAllBytes(Paths.get(certFile.getPath())));
            byte[] encoded = DatatypeConverter.parseBase64Binary(keyBuilder.toString());
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
            KeyFactory kf = KeyFactory.getInstance("EC");
            key = kf.generatePrivate(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                isKey.close();
                brKey.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        UserEnrollment enrollment = new UserEnrollment(key, certificate);
        return enrollment;
    }

    static TransactionProposalRequest getTransactionProposalRequest(HFClient hfClient, String chaincodeName, String functionName, String[] args) {
        TransactionProposalRequest transactionProposalRequest = hfClient.newTransactionProposalRequest();
        transactionProposalRequest.setFcn(functionName);
        transactionProposalRequest.setArgs(args);
        transactionProposalRequest.setChaincodeID(ChaincodeID.newBuilder().setName(chaincodeName).build());
        return transactionProposalRequest;
    }

    static TransactionProposalRequest getTransactionProposalRequest(HFClient hfClient, String chaincodeName, String functionName, byte[][] args) {
        TransactionProposalRequest transactionProposalRequest = hfClient.newTransactionProposalRequest();
        transactionProposalRequest.setFcn(functionName);
        transactionProposalRequest.setArgBytes(args);
        transactionProposalRequest.setChaincodeID(ChaincodeID.newBuilder().setName(chaincodeName).build());
        return transactionProposalRequest;
    }

    static HFClient getHFClient(File keyFile, File certFile, String mspId, String userName) {
        HFClient hfClient = HFClient.createNewInstance();
        try {
            hfClient.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
            hfClient.setUserContext(Utils.getUserContext(keyFile, certFile, mspId, userName));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hfClient;
    }

    static HFClient getHFClient(Enrollment enrollment, String mspId, String userName) {
        HFClient hfClient = HFClient.createNewInstance();
        try {
            hfClient.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
            hfClient.setUserContext(Utils.getUserContext(enrollment, mspId, userName));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hfClient;
    }

    static Channel getChannel(HFClient hfClient, String channelName, String peerName, String peerURL, String ordererName, String ordererURL) {
        Channel channel = null;
        try {
            channel = hfClient.newChannel(channelName);
            Peer peer = hfClient.newPeer(peerName, peerURL);
            Orderer orderer = hfClient.newOrderer(ordererName, ordererURL);
            channel.addPeer(peer);
            channel.addOrderer(orderer);
            channel.initialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return channel;
    }

    public static String getSHA256(byte[] bytes) {
        if (null == bytes) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.reset();
            messageDigest.update(bytes);
            return byte2Hex(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);}
    }

    public static String getSHA256(String str) {
        return getSHA256(str.getBytes());
    }

    private static String byte2Hex(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i = 0; i < bytes.length; i++) {
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length() == 1) {
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }
}
