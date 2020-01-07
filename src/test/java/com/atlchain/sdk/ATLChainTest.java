package com.atlchain.sdk;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class ATLChainTest {
//    private File certFile = new File(this.getClass().getResource("/certs/user/cert.pem").getPath());
//    private File skFile = new File(this.getClass().getResource("/certs/user/user_sk").getPath());
    private File networkFile = new File(this.getClass().getResource("/network-config-testC.yaml").getPath());

//    private ATLChain atlChain = new ATLChain(
//            certFile,
//            skFile,
//            "TestOrgA",
//            "grpc://172.16.15.66:7051",
//            "TestOrgA",
//            "admin",
//            "OrdererTestOrgA",
//            "grpc://172.16.15.66:7050",
//            "atlchannel"
//    );

    private ATLChain atlChain;

    public ATLChainTest() {
        atlChain = ATLChain.getATLChain("txchannel", networkFile);
    }

    @Test
    public void testQuery() {
        try {
            String result = atlChain.getAtlTransaction().query(
                    "bcgiscc",
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
            String result = atlChain.getAtlTransaction().invoke(
                    "bcgiscc",
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
                String result = atlChain.getAtlTransaction().invokeByte(
                        "bcgiscc",
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
                byte[][] result = atlChain.getAtlTransaction().queryByte(
                        "bcgiscc",
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
            byte[][] result = atlChain.getAtlTransaction().queryByte(
                    "bcgiscc",
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