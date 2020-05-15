package com.supermap.blockchain.sdk.bcos;

import org.junit.Test;

import static org.junit.Assert.*;

public class TransactionImpTest {

    @Test
    public void queryByString() throws Exception {
        TransactionImp transactionImp = new TransactionImp();
        transactionImp.queryByString("", "", null);
    }
}