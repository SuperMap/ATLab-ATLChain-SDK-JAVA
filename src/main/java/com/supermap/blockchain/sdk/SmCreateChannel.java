package com.supermap.blockchain.sdk;

import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.identity.X509Enrollment;
import org.hyperledger.fabric.sdk.security.CryptoPrimitives;
import org.hyperledger.fabric.sdk.security.CryptoSuite;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * @author liqs
 * @version 1.0
 * @date 2020/5/14 18:24
 */
public class SmCreateChannel {

    public void creatChannel() throws Exception {

        // 第一步 根据 peerAdmin 构造 peerAdminUser  和   HFClient
        HFClient hfClient = HFClient.createNewInstance();
        hfClient.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
        User peerAdminUser = getUser("OrgA",
                "peer0.orga.example.com",
                "D:\\crypto-config\\peerOrganizations\\orga.example.com\\users\\Admin@orga.example.com\\msp\\keystore\\315edd7722516c1b2770891587773dbcd52ebd87736ed946bb8ddc7555421788_sk",
                "D:\\crypto-config\\peerOrganizations\\orga.example.com\\users\\Admin@orga.example.com\\msp\\signcerts\\Admin@orga.example.com-cert.pem");
        hfClient.setUserContext(peerAdminUser);

        //第二步  初始化 ChannelConfiguration 对象.参数是通道初始化文件路径
        ChannelConfiguration channelConfiguration = new ChannelConfiguration(new File("D:\\channel3.tx"));

        //第三步 根据 HFClient 构造 orderder节点信息
        Properties ordererProperties = new Properties();
        File clientCert;
        File clientKey;
        File cert = new File("D:\\crypto-config\\ordererOrganizations\\example.com\\orderers\\orderer0.example.com\\tls\\server.crt");
        clientCert = new File("D:\\crypto-config\\ordererOrganizations\\example.com\\users\\Admin@example.com\\tls\\client.crt");
        clientKey = new File("D:\\crypto-config\\ordererOrganizations\\example.com\\users\\Admin@example.com\\tls\\client.key");

        String ordererName = "orderer0.example.com";
        ordererProperties.setProperty("clientCertFile", clientCert.getAbsolutePath());
        ordererProperties.setProperty("clientKeyFile", clientKey.getAbsolutePath());
        ordererProperties.setProperty("pemFile", cert.getAbsolutePath());
        ordererProperties.setProperty("hostnameOverride", ordererName);
        ordererProperties.setProperty("sslProvider", "openSSL");
        ordererProperties.setProperty("negotiationType", "TLS");
        ordererProperties.put("grpc.NettyChannelBuilderOption.maxInboundMessageSize", 9000000);
        Orderer orderer = hfClient.newOrderer(ordererName, "grpcs://orderer0.example.com:7050", ordererProperties);

        // 第四步 创建 channel
        hfClient.newChannel("channel3", orderer, channelConfiguration, hfClient.getChannelConfigurationSignature(channelConfiguration, peerAdminUser));

    }

    /**
     * 构造 AdminUser
     * @param keyFile
     * @param certFile
     * @return
     * @throws Exception
     */
    public User getUser(String userMspId, String userName, String keyFile, String certFile) throws Exception {

//        String userName = "peer0.orga.example.com";
//        String userMspId = "OrgA";
//        String keyFile = "D:\\crypto-config\\peerOrganizations\\orga.example.com\\users\\Admin@orga.example.com\\msp\\keystore\\3ba8424db358ec6083c852a5b3701217a4c8d201d60aaba2e42dd9a7867a0c62_sk";
//        String certFile = "D:\\crypto-config\\peerOrganizations\\orga.example.com\\users\\Admin@orga.example.com\\msp\\signcerts\\Admin@orga.example.com-cert.pem";
        Enrollment enrollment = loadFromPemFile(keyFile, certFile);
        //构造用户
        SmUser user = new SmUser();
        user.setName(userName);
        user.setEnrollment(enrollment);
        user.setMspId(userMspId);
        return user;
    }

    /**
     * 根据证书和秘钥构造 Enrollment 用于构建 User
     * @param keyFile
     * @param certFile
     * @return
     * @throws Exception
     */
    public Enrollment loadFromPemFile(String keyFile, String certFile) throws Exception{
        byte[] keyPem = Files.readAllBytes(Paths.get(keyFile));     //载入私钥PEM文本
        byte[] certPem = Files.readAllBytes(Paths.get(certFile));   //载入证书PEM文本
        CryptoPrimitives suite = new CryptoPrimitives();            //载入密码学套件
        PrivateKey privateKey = suite.bytesToPrivateKey(keyPem);    //将PEM文本转换为私钥对象
        return new X509Enrollment(privateKey, new String(certPem));  //创建并返回X509Enrollment对象
    }


}
