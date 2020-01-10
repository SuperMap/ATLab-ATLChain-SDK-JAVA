package com.supermap.blockchain.sdk;

public class SmBlockchainInfo {
    private String currentBlockHash;
    private String previousBlockHash;
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
