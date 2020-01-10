package com.supermap.blockchain.sdk;

import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.TransactionInfo;

import java.util.Set;

/**
 * 区块链网络信息获取
 */
public interface SmChannel {

    SmBlockchainInfo getBlockchainInfo();

    SmBlockInfo getBlockInfoByNumber(Peer peer, long number);

    Set<String> listChannelOfPeerJoined(Peer peer);

    void update();
}
