package com.supermap.blockchain.sdk;

/**
 * 交易相关
 */
public interface SmTransaction {
    public String query(String chaincodeName, String functionName, String[] args);

    public byte[][] queryByte(String chaincodeName, String functionName, byte[][] args);

    public String invoke(String chaincodeName, String functionName, String[] args);

    public String invokeByte(String chaincodeName, String functionName, byte[][] args);
}
