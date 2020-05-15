package com.supermap.blockchain.sdk;

import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.Orderer;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;

import java.util.Properties;
import java.util.logging.Logger;

/**
 * @author liqs
 * @version 1.0
 * @date 2020/5/13 12:32
 * 说明  因为现在该 peer 节点所在的组织已经加入到通道里面，所以现在需要的环境只是该组织的证书秘钥以及 orderer 的证书秘钥即可
 */
public class SmAddPeerImp {

    Logger logger = Logger.getLogger(SmAddPeerImp.class.toString());
    private HFClient hfClient;
    private Channel channel;

    public SmAddPeerImp(HFClient hfClient, Channel channel) {
        this.hfClient = hfClient;
        this.channel = channel;
    };

    /**
     * 基于 TLS 创建peer
     * @param peerName           加入的节点名称
     * @param peerGrpcURL        加入的节点的地址
     * @param pemFile            加入节点的 tls 证书
     * @param clientKeyFile      加入节点的 admin 用户的 key
     * @param clientCertFile     加入节点的 admin 用户的 证书
     * @return
     */
    public Peer creatPeerByTls(String peerName, String peerGrpcURL, String pemFile, String clientKeyFile, String clientCertFile ){

//        String peerName = "peer1.org4.example.com";
//        String peerGrpcURL = "grpcs://peer1.org4.example.com:14051";
//        String pemFile = "D:\\crypto-config-org4\\peerOrganizations\\org4.example.com\\peers\\peer1.org4.example.com\\tls\\server.crt";
//        String clientKeyFile = "D:\\crypto-config-org4\\peerOrganizations\\org4.example.com\\users\\Admin@org4.example.com\\tls\\server.key";
//        String clientCertFile = "D:\\crypto-config-org4\\peerOrganizations\\org4.example.com\\users\\Admin@org4.example.com\\tls\\server.crt";

        Properties peerProperties = new Properties();
        peerProperties.setProperty("pemFile", pemFile);
        peerProperties.setProperty("sslProvider", "openSSL");
        peerProperties.setProperty("negotiationType", "TLS");
        peerProperties.setProperty("hostnameOverride", peerName);
        peerProperties.setProperty("trustServerCertificate", "true");
        peerProperties.setProperty("clientKeyFile", clientKeyFile);
        peerProperties.setProperty("clientCertFile", clientCertFile);
        peerProperties.put("grpc.NettyChannelBuilderOption.maxInboundMessageSize", 9000000);

        Peer peer = null;
        try {
             peer = this.hfClient.newPeer(peerName, peerGrpcURL, peerProperties);
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
        }
        return peer;
    }

    /**
     * 基于 TLS 创建 orderer
     * @param ordererName           orderer 名称
     * @param ordererGrpcURL        orderer 地址
     * @param pemFile               orderer tls 证书
     * @param clientKeyFile         orderer admin 用户的 key
     * @param clientCertFile        orderer admin 用户的 证书
     * @return
     */
    public Orderer createOrdererByTls(String ordererName, String ordererGrpcURL, String pemFile, String clientKeyFile, String clientCertFile ){
//        String ordererName = "orderer0.example.com";
//        String ordererGrpcURL = "grpcs://orderer.example.com:7050";
//        String pemFile = "D:\\crypto-config\\ordererOrganizations\\example.com\\orderers\\orderer0.example.com\\tls\\server.crt";
//        String clientKeyFile = "D:\\crypto-config\\ordererOrganizations\\example.com\\users\\Admin@example.com\\tls\\server.key";
//        String clientCertFile = "D:\\crypto-config\\ordererOrganizations\\example.com\\users\\Admin@example.com\\tls\\server.crt";

        Properties ordererProperties = new Properties();
        ordererProperties.setProperty("pemFile", pemFile);
        ordererProperties.setProperty("sslProvider", "openSSL");
        ordererProperties.setProperty("negotiationType", "TLS");
        ordererProperties.setProperty("hostnameOverride", ordererName);
        ordererProperties.setProperty("trustServerCertificate", "true");
        ordererProperties.setProperty("clientKeyFile", clientKeyFile);
        ordererProperties.setProperty("clientCertFile", clientCertFile);
        ordererProperties.put("grpc.NettyChannelBuilderOption.maxInboundMessageSize", 9000000);

        Orderer orderer = null;
        try {
            orderer = this.hfClient.newOrderer(ordererName, ordererGrpcURL, ordererProperties);
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
        }
        return orderer;
    }
}
