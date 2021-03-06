package com.supermap.blockchain.sdk.bcos;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.fisco.bcos.web3j.protocol.core.DefaultBlockParameterNumber;
import org.fisco.bcos.web3j.protocol.core.Request;
import org.fisco.bcos.web3j.protocol.core.methods.response.BcosBlock;
import org.fisco.bcos.web3j.protocol.core.methods.response.BlockNumber;
import org.fisco.bcos.web3j.protocol.core.methods.response.Transaction;
import org.fisco.bcos.web3j.protocol.exceptions.TransactionException;
import org.fisco.bcos.web3j.tx.txdecode.TransactionDecoder;
import org.fisco.bcos.web3j.tx.txdecode.TransactionDecoderFactory;
import org.junit.Test;

import java.math.BigInteger;
import java.util.List;

public class BcosTransactionImpTest {

    @Test
    public void queryByString() throws Exception {
        BcosTransactionImp transactionImp = new BcosTransactionImp();
        transactionImp.queryByString("", "", new String[]{"name4", "4"});
    }

    @Test
    public void invokeByString() throws Exception {
        BcosTransactionImp transactionImp = new BcosTransactionImp();
        String txHash = transactionImp.invokeByString("", "", new String[]{"name4", "4", "item_name4"});
        System.out.println(txHash);
    }

    @Test
    public void testGetTx() throws Exception {
        BcosTransactionImp transactionImp = new BcosTransactionImp();
        Request<?, BlockNumber> blockNumberRequest = transactionImp.web3j.getBlockNumber();
        BigInteger blockNumber = blockNumberRequest.send().getBlockNumber();
        System.out.println(blockNumber);
        for (int i = 0; i < blockNumber.intValue(); i++) {
            Request<?, BcosBlock> block = transactionImp.web3j.getBlockByNumber(new DefaultBlockParameterNumber(i), true);
            List<BcosBlock.TransactionResult> transactions = block.send().getBlock().getTransactions();
            for (BcosBlock.TransactionResult transaction : transactions) {
                Transaction t = (Transaction) transaction.get();
                String input = t.getInput();
                TransactionDecoder transactionDecoder = TransactionDecoderFactory.buildTransactionDecoder("CommonContract");
                try {
                    // {"function":"insert(string,int256,string)","methodID":"0xebf3b24f","result":[{"name":"name","type":"string","data":"name1"},{"name":"item_id","type":"int256","data":1},{"name":"item_name","type":"string","data":"item_name1"}]}
                    String s = transactionDecoder.decodeInputReturnJson(input);
                    JSONObject jsonObject = (JSONObject)JSONObject.parse(s);
                    String function = jsonObject.getString("function");
                    JSONArray result = jsonObject.getJSONArray("result");

                    System.out.println(function);
                    System.out.println(result);
                } catch (TransactionException e) {
                    continue;
                }
            }
        }

    }
}