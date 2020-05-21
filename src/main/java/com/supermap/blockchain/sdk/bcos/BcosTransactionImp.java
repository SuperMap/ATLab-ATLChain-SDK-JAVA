package com.supermap.blockchain.sdk.bcos;

import com.supermap.blockchain.sdk.SmTransaction;
import org.fisco.bcos.channel.client.Service;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.crypto.gm.GenCredential;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.channel.ChannelEthereumService;
import org.fisco.bcos.web3j.protocol.core.methods.response.Log;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.fisco.bcos.web3j.tx.gas.StaticGasProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigInteger;
import java.util.List;

// BCOS 交易相关类
public class BcosTransactionImp implements SmTransaction {

    @Override
    public String queryByString(String chaincodeName, String functionName, String[] args) throws Exception {
        //读取配置文件，sdk与区块链节点建立连接，获取web3j对象
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        Service service = context.getBean(Service.class);
        service.run();
        ChannelEthereumService channelEthereumService = new ChannelEthereumService();
        channelEthereumService.setChannelService(service);
        channelEthereumService.setTimeout(10000);
        Web3j web3j = Web3j.build(channelEthereumService, service.getGroupId());
        //准备部署和调用合约的参数
        BigInteger gasPrice = new BigInteger("300000000");
        BigInteger gasLimit = new BigInteger("300000000");
        String privateKey = "b83261efa42895c38c6c2364ca878f43e77f3cddbc922bf57d0d48070f79feb6";
        //指定外部账户私钥，用于交易签名
        Credentials credentials = GenCredential.create(privateKey);
        //部署合约
//        HelloWorld contract = HelloWorld.deploy(web3j, credentials, new StaticGasProvider(gasPrice, gasLimit)).send();
//        String address = contract.getContractAddress();
//        System.out.println(address);
        //根据合约地址加载合约
        HelloWorld contract = HelloWorld.load("0xfc6c71ac1cfa6c1650f2f696ff12a666683b4a13", web3j, credentials, new StaticGasProvider(gasPrice, gasLimit));
        //调用合约方法发送交易
        TransactionReceipt transactionReceipt = contract.set("HAHA").send();
        List<Log> logs = transactionReceipt.getLogs();

        //查询合约方法查询该合约的数据状态
//        String result = contract.get().send();
//        System.out.println(result);
        return null;
    }

    @Override
    public byte[][] queryByByte(String chaincodeName, String functionName, byte[][] args) {
        return new byte[0][];
    }

    @Override
    public String invokeByString(String chaincodeName, String functionName, String[] args) {
        return null;
    }

    @Override
    public String invokeByByte(String chaincodeName, String functionName, byte[][] args) {
        return null;
    }
}
