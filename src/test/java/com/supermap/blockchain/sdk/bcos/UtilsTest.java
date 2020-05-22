package com.supermap.blockchain.sdk.bcos;

import org.junit.Test;

import javax.rmi.CORBA.Util;

import static org.junit.Assert.*;

public class UtilsTest {

    @Test
    public void testGetHistoryOpOfContract() throws Exception {
        String result = Utils.GetHistoryOpOfContract("CommonContract");
        System.out.println(result);
    }
}