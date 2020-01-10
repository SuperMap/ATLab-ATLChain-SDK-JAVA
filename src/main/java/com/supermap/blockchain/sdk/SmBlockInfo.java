package com.supermap.blockchain.sdk;

public class SmBlockInfo {
    private String currentBlockHash;
    private String previousBlockHash;

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
}
