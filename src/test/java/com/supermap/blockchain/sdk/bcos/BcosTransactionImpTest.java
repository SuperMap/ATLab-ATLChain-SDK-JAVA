package com.supermap.blockchain.sdk.bcos;

import org.junit.Test;

public class BcosTransactionImpTest {

    @Test
    public void queryByString() throws Exception {
        BcosTransactionImp transactionImp = new BcosTransactionImp();
        transactionImp.queryByString("", "", null);
    }
}