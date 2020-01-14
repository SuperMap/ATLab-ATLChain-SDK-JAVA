package com.supermap.blockchain.sdk;

/**
 * Supermap 区块链信息类
 */
public class SmBlockchainInfo {
    /**
     * 当前区块哈希，即区块链中最新区块
     */
    private String currentBlockHash;

    /**
     * 上一个区块链哈希
     */
    private String previousBlockHash;

    /**
     * 当前区块总高度
     */
    private long height;

    public String getCurrentBlockHash() {
        return currentBlockHash;
    }

    void setCurrentBlockHash(String currentBlockHash) {
        this.currentBlockHash = currentBlockHash;
    }

    public String getPreviousBlockHash() {
        return previousBlockHash;
    }

    void setPreviousBlockHash(String previousBlockHash) {
        this.previousBlockHash = previousBlockHash;
    }

    public long getHeight() {
        return height;
    }

    void setHeight(long height) {
        this.height = height;
    }
}
