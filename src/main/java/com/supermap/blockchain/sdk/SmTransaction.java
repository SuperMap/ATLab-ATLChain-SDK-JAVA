package com.supermap.blockchain.sdk;

/**
 * 交易处理接口
 */
public interface SmTransaction {
    /**
     * 查询交易
     * @param chaincodeName 链码名
     * @param functionName 方法名
     * @param args 方法参数
     * @return 查询结果字符串
     */
    String query(String chaincodeName, String functionName, String[] args);

    /**
     * 以字节方式查询交易
     * @param chaincodeName 链码名
     * @param functionName 方法名
     * @param args 方法参数
     * @return 查询结果字节数组
     */
    byte[][] queryByte(String chaincodeName, String functionName, byte[][] args);

    /**
     * 执行交易
     * @param chaincodeName 链码名
     * @param functionName 方法名
     * @param args 方法参数
     * @return 执行结果字符串
     */
    String invoke(String chaincodeName, String functionName, String[] args);

    /**
     * 以字节方式执行交易
     * @param chaincodeName 链码名
     * @param functionName 方法名
     * @param args 方法参数
     * @return 执行结果字符串
     */
    String invokeByte(String chaincodeName, String functionName, byte[][] args);
}
