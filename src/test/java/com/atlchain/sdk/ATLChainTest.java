package com.atlchain.sdk;

import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.NetworkConfigurationException;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class ATLChainTest {
//    private File certFile = new File(this.getClass().getResource("/certs/user/cert.pem").getPath());
//    private File skFile = new File(this.getClass().getResource("/certs/user/user_sk").getPath());
    private File networkFile = new File("/home/cy/Documents/ATL/SuperMap/ATLab-ATLChain-SDK-JAVA/src/main/resources/network-config-test.yaml");

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

    public ATLChainTest() throws IOException, NetworkConfigurationException, InvalidArgumentException, IllegalAccessException, InstantiationException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, CryptoException {
        atlChain = new ATLChain(networkFile);
    }

    @Test
    public void testQuery() {
        try {
            String result = atlChain.query(
                    "javacc",
                    "query",
                    new String[]{"a"}
                    );
            System.out.println(result);
            Assert.assertNotEquals("", result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInvoke() {
        try {
            String result = atlChain.invoke(
                    "javacc",
                    "invoke",
                    new String[]{"b", "a" ,"10"}
                    );
            System.out.println(result);
            Assert.assertNotEquals("", result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPutManyRecord() {
//        long startTime = System.currentTimeMillis();
        for(int i = 0; i<10; i++) {
            String key = "t2key" + i;
            try {
                String result = atlChain.invokeByte(
                        "bincc",
                        "PutByteArray",
                        new byte[][]{key.getBytes(), String.valueOf(i).getBytes()}
                );
                System.out.println(result);
                Assert.assertNotEquals("", result);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
//        long endTime = System.currentTimeMillis();
//        System.out.println((endTime - startTime)/1000);
    }

    @Test
    public void testGetManyRecord() {
//        long startTime = System.currentTimeMillis();
        for(int i = 0; i<10; i++) {
            String key = "t2key" + i;
            try {
                byte[][] result = atlChain.queryByte(
                        "bincc",
                        "GetByteArray",
                        new byte[][]{key.getBytes()}
                );
                for (byte[] res : result) {
                    System.out.println(key + ": " + new String(res));
                }
                Assert.assertNotEquals("", result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

//        long endTime = System.currentTimeMillis();
//        System.out.println((endTime - startTime)/1000);
    }
}