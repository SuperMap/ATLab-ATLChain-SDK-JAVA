package com.supermap.blockchain.sdk;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class SmTransactionImpTest {
    private static final String channelName = "txchannel";
    private static final String chaincodeName = "testCommon";
    private File networkFile = new File(this.getClass().getResource("/network-config-testC.yaml").getPath());
    private SmChain smChain;

    public SmTransactionImpTest() {
        smChain = SmChain.getChain(channelName, networkFile);
    }

    @Test
    public void testQuery() {
        try {
            String result = smChain.getTransaction().queryByString(
                    chaincodeName,
                    "GetRecordByKey",
                    new String[]{"a"}
                    );
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInvoke() {
        try {
            String result = smChain.getTransaction().invokeByString(
                    chaincodeName,
                    "PutRecord",
                    new String[]{"a" ,"10"}
                    );
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPutManyRecord() {
        long startTime = System.currentTimeMillis();
        int loop = 20000;
        for(int i = 0; i<loop; i++) {
            String key = "ttkey" + i;
            try {
                String result = smChain.getTransaction().invokeByByte(
                        chaincodeName,
                        "PutRecordBytes",
                        new byte[][]{key.getBytes(), ("value" + String.valueOf(i)).getBytes()}
                );
                System.out.println(i + ": " + result);
//                Assert.assertNotEquals("", result);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        long endTime = System.currentTimeMillis();
        System.out.println("Time: " + (endTime - startTime)/1000 + " ,TPS: " + 1000.0*loop/(endTime - startTime));
    }

    @Test
    public void testGetManyRecord() {
        long startTime = System.currentTimeMillis();
        int loop = 5000;
        for(int i = 0; i<loop; i++) {
            String key = "DString3-max-" + i;
            try {
                byte[][] result = smChain.getTransaction().queryByByte(
                        chaincodeName,
                        "GetRecordByKey",
                        new byte[][]{key.getBytes()}
                );
                for (byte[] res : result) {
                    System.out.println(key + ": " + new String(res));
                }
//                Assert.assertNotEquals("", result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Time: " + (endTime - startTime) + "ms ,TPS: " + 1000.0*loop/(endTime - startTime));
    }

    @Test
    public void testGetRecordByRange() {
        long startTime = System.currentTimeMillis();
        // Query by lexical order
        String startKey = "ttkey0";
        String endKey = "ttkey99999";
        try {
            byte[][] result = smChain.getTransaction().queryByByte(
                    chaincodeName,
                    "GetRecordByKeyRange",
                    new byte[][]{startKey.getBytes(), endKey.getBytes()}
            );
            for (byte[] res : result) {
                System.out.println(startKey + ": " + new String(res));
            }
            Assert.assertNotEquals("", result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Time: " + (endTime - startTime)/1000);
    }
}