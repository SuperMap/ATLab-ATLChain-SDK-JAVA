package com.supermap.blockchain.sdk.bcos;

import com.supermap.blockchain.sdk.SmTransaction;
import org.fisco.bcos.channel.client.Service;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.crypto.gm.GenCredential;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.channel.ChannelEthereumService;
import org.fisco.bcos.web3j.protocol.core.RemoteCall;
import org.fisco.bcos.web3j.protocol.core.Request;
import org.fisco.bcos.web3j.protocol.core.methods.response.BcosTransaction;
import org.fisco.bcos.web3j.protocol.core.methods.response.Transaction;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.fisco.bcos.web3j.protocol.exceptions.TransactionException;
import org.fisco.bcos.web3j.tuples.generated.Tuple3;
import org.fisco.bcos.web3j.tx.gas.StaticGasProvider;
import org.fisco.bcos.web3j.tx.txdecode.BaseException;
import org.fisco.bcos.web3j.tx.txdecode.TransactionDecoder;
import org.fisco.bcos.web3j.tx.txdecode.TransactionDecoderFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

// BCOS 交易相关类
public class BcosTransactionImp implements SmTransaction {
    BigInteger gasPrice;
    BigInteger gasLimit;
    Web3j web3j;
    Credentials credentials;

    public BcosTransactionImp() throws Exception {
        //读取配置文件，sdk与区块链节点建立连接，获取web3j对象
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        Service service = context.getBean(Service.class);
        service.run();
        ChannelEthereumService channelEthereumService = new ChannelEthereumService();
        channelEthereumService.setChannelService(service);
        channelEthereumService.setTimeout(10000);
        web3j = Web3j.build(channelEthereumService, service.getGroupId());
        //准备部署和调用合约的参数
        gasPrice = new BigInteger("300000000");
        gasLimit = new BigInteger("300000000");
        String privateKey = "b83261efa42895c38c6c2364ca878f43e77f3cddbc922bf57d0d48070f79feb6";
        //指定外部账户私钥，用于交易签名
        credentials = GenCredential.create(privateKey);
    }

    @Override
    public String queryByString(String chaincodeName, String functionName, String[] args) throws Exception {

        /** HelloWorld 合约调用
         * //部署合约
         * //        HelloWorld contract = HelloWorld.deploy(web3j, credentials, new StaticGasProvider(gasPrice, gasLimit)).send();
         * //        String address = contract.getContractAddress();
         * //        System.out.println(address);
         *
         *         //根据合约地址加载合约
         *         HelloWorld contract = HelloWorld.load("0xfc6c71ac1cfa6c1650f2f696ff12a666683b4a13", web3j, credentials, new StaticGasProvider(gasPrice, gasLimit));
         *
         *         //查询合约方法查询该合约的数据状态
         *         String result = contract.get().send();
         *         System.out.println(result);
         */

        // 合约地址需要在安装合约的时候获取
        CommonContract commonContract = CommonContract.load("0x5fc67db57fe1112c99f44e52a628e38422dbf9af", web3j, credentials, new StaticGasProvider(gasPrice, gasLimit));
        RemoteCall<Tuple3<List<String>, List<BigInteger>, List<String>>> name2 = commonContract.select(args[0], BigInteger.valueOf(Long.parseLong(args[1])));

        Tuple3<List<String>, List<BigInteger>, List<String>> tuple3 = name2.send();

        int size = tuple3.getSize();

        for (int i = 0; i < tuple3.getValue1().size(); i++) {
            System.out.println(tuple3.getValue1().get(i));
            System.out.println(tuple3.getValue2().get(i));
            System.out.println(tuple3.getValue3().get(i));
            System.out.println();
        }

        return null;
    }

    @Override
    public byte[][] queryByByte(String chaincodeName, String functionName, byte[][] args) {
        return new byte[0][];
    }

    @Override
    public String invokeByString(String chaincodeName, String functionName, String[] args) throws Exception {

        /** HelloWorld 合约调用
         * //部署合约
         * //        HelloWorld contract = HelloWorld.deploy(web3j, credentials, new StaticGasProvider(gasPrice, gasLimit)).send();
         * //        String address = contract.getContractAddress();
         * //        System.out.println(address);
         *
         *         //根据合约地址加载合约
         *         HelloWorld contract = HelloWorld.load("0xfc6c71ac1cfa6c1650f2f696ff12a666683b4a13", web3j, credentials, new StaticGasProvider(gasPrice, gasLimit));
         *         //调用合约方法发送交易
         *         TransactionReceipt transactionReceipt = contract.set("HAHA").send();
         *         transactionReceipt.getTransactionHash();
         */

        // 合约地址需要在安装合约的时候获取
        CommonContract commonContract = CommonContract.load("0x5fc67db57fe1112c99f44e52a628e38422dbf9af", web3j, credentials, new StaticGasProvider(gasPrice, gasLimit));
        RemoteCall<TransactionReceipt> insert = commonContract.insert(args[0], BigInteger.valueOf(Long.parseLong(args[1])), args[2]);

        TransactionReceipt transactionReceipt = insert.send();

        return transactionReceipt.getTransactionHash();
    }

    @Override
    public String invokeByByte(String chaincodeName, String functionName, byte[][] args) {
        return null;
    }

    public void getTx() throws IOException, TransactionException, BaseException {
        BcosTransaction bcosTransaction = web3j.getTransactionByHash("0xec76723dbaa17d9dacab0a9ed1b9deed58090b76ee1892199b3bca8bc6dcef3e").send();

        Optional<Transaction> transaction = bcosTransaction.getTransaction();
        Transaction transaction1 = transaction.get();
        String input = transaction1.getInput();
        System.out.println(input);

        // 需要在项目根目录下的solidity文件夹中存放对应的sol合约文件
        TransactionDecoder transactionDecoder = TransactionDecoderFactory.buildTransactionDecoder("CommonContract");
        String s = transactionDecoder.decodeInputReturnJson(input);
        System.out.println(s);
    }
}
