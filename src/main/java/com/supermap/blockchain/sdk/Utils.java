package com.supermap.blockchain.sdk;

import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.TransactionProposalRequest;

import java.io.ByteArrayInputStream;
import java.security.MessageDigest;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;

/**
 * 工具类
 */
class Utils {
    /**
     * 构造交易提案请求
     * @param hfClient fabric 客户端
     * @param chaincodeName 链码名
     * @param functionName 方法名
     * @param args 参数
     * @return 交易提案请求
     */
    static TransactionProposalRequest getTransactionProposalRequest(HFClient hfClient, String chaincodeName, String functionName, String[] args) {
        TransactionProposalRequest transactionProposalRequest = hfClient.newTransactionProposalRequest();
        transactionProposalRequest.setFcn(functionName);
        transactionProposalRequest.setArgs(args);
        transactionProposalRequest.setChaincodeID(ChaincodeID.newBuilder().setName(chaincodeName).build());
        return transactionProposalRequest;
    }

    /**
     * 构造交易提案请求
     * @param hfClient fabric 客户端
     * @param chaincodeName 链码名
     * @param functionName 方法名
     * @param args 参数
     * @return 交易提案请求
     */
    static TransactionProposalRequest getTransactionProposalRequest(HFClient hfClient, String chaincodeName, String functionName, byte[][] args) {
        TransactionProposalRequest transactionProposalRequest = hfClient.newTransactionProposalRequest();
        transactionProposalRequest.setFcn(functionName);
        transactionProposalRequest.setArgBytes(args);
        transactionProposalRequest.setChaincodeID(ChaincodeID.newBuilder().setName(chaincodeName).build());
        return transactionProposalRequest;
    }

    /**
     * 获取Hash字符串
     * @param bytes 原文
     * @return Hash字符串
     */
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
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取Hash字符串
     * @param str 原文
     * @return Hash字符串
     */
    public static String getSHA256(String str) {
        return getSHA256(str.getBytes());
    }

    /**
     * Base64 编码
     * @param bytes 原文
     * @return Base64字符串
     */
    public static String encodeBase64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * Base64 解码
     * @param str Base64字符串
     * @return 原文
     */
    public static byte[] decodeBase64(String str) {
        return Base64.getDecoder().decode(str);
    }

    /**
     * Base64 解码
     * @param str Base64字符串
     * @return 原文
     */
    public static byte[] decodeBase64(byte[] bytes) {
        return Base64.getDecoder().decode(bytes);
    }

    /**
     * 根据PEM字符串获取 X509 证书
     * @param pemStr PEM 字符串
     * @return X509 证书
     * @throws CertificateException
     */
    public static X509Certificate getCertFromPem(String pemStr) throws CertificateException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        return (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(pemStr.getBytes()));

    }

    /**
     * 字节数组转十六进制
     * @param bytes 字节数组
     * @return 十六进制字符串
     */
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
