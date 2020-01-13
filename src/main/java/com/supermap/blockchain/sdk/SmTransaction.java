package com.supermap.blockchain.sdk;

/**
 * 交易相关
 */
public interface SmTransaction {
    String query(String chaincodeName, String functionName, String[] args);

    byte[][] queryByte(String chaincodeName, String functionName, byte[][] args);

    String invoke(String chaincodeName, String functionName, String[] args);

    String invokeByte(String chaincodeName, String functionName, byte[][] args);
}
