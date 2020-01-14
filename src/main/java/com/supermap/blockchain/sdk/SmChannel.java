package com.supermap.blockchain.sdk;

import org.hyperledger.fabric.sdk.Peer;

import java.util.Set;

/**
 * Supermap 通道处理接口
 */
public interface SmChannel {

    /**
     * 获取区块链信息
     * @return Supermap 区块链信息
     */
    SmBlockchainInfo getBlockchainInfo();

    /**
     * 根据区块链号获取指定节点上的区块信息
     * @param peer 节点
     * @param number 区块号
     * @return Supermap区块信息
     */
    SmBlockInfo getBlockInfoByNumber(Peer peer, long number);

    /**
     * 获取节点加入的通道
     * @param peer 节点
     * @return 通道名列表
     */
    Set<String> listChannelOfPeerJoined(Peer peer);
}
