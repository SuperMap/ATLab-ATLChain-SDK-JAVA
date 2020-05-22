package com.supermap.blockchain.sdk.bcos;

import com.alibaba.fastjson.JSONArray;
import org.fisco.bcos.web3j.protocol.core.DefaultBlockParameterNumber;
import org.fisco.bcos.web3j.protocol.core.Request;
import org.fisco.bcos.web3j.protocol.core.methods.response.BcosBlock;
import org.fisco.bcos.web3j.protocol.core.methods.response.BlockNumber;
import org.fisco.bcos.web3j.protocol.core.methods.response.Transaction;
import org.fisco.bcos.web3j.protocol.exceptions.TransactionException;
import org.fisco.bcos.web3j.tx.txdecode.TransactionDecoder;
import org.fisco.bcos.web3j.tx.txdecode.TransactionDecoderFactory;

import java.math.BigInteger;
import java.util.List;

public class Utils {
    /**
     * 获取某个合约的所有历史操作
     * @param contractName
     * @throws Exception
     */
    public static String GetHistoryOpOfContract(String contractName) throws Exception {
        BcosTransactionImp transactionImp = new BcosTransactionImp();
        Request<?, BlockNumber> blockNumberRequest = transactionImp.web3j.getBlockNumber();

        // 获取区块高度
        BigInteger blockNumber = blockNumberRequest.send().getBlockNumber();

        JSONArray resultJsonArray = new JSONArray();

        // 遍历每一个区块
        for (int i = 0; i < blockNumber.intValue(); i++) {
            Request<?, BcosBlock> block = transactionImp.web3j.getBlockByNumber(new DefaultBlockParameterNumber(i), true);
            List<BcosBlock.TransactionResult> transactions = block.send().getBlock().getTransactions();

            // 遍历每个区块中的交易
            for (BcosBlock.TransactionResult transaction : transactions) {
                Transaction t = (Transaction) transaction.get();
                String input = t.getInput();
                TransactionDecoder transactionDecoder = TransactionDecoderFactory.buildTransactionDecoder(contractName);
                try {
                    // 交易解码示例
                    // {"function":"insert(string,int256,string)","methodID":"0xebf3b24f","result":[{"name":"name","type":"string","data":"name1"},{"name":"item_id","type":"int256","data":1},{"name":"item_name","type":"string","data":"item_name1"}]}
                    String s = transactionDecoder.decodeInputReturnJson(input);
//                    JSONObject jsonObject = (JSONObject)JSONObject.parse(s);
                    resultJsonArray.add(s);
//                    String function = jsonObject.getString("function");
//                    JSONArray result = jsonObject.getJSONArray("result");
//
//                    System.out.println(function);
//                    System.out.println(result);
                } catch (TransactionException e) {
                    // 当交易不是该合约产生的，则会抛出该异常，这里直接跳过
                    continue;
                }
            }
        }

        return resultJsonArray.toJSONString();
    }
}
