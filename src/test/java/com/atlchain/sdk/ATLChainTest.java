package com.atlchain.sdk;

import org.junit.Assert;
import org.junit.Test;
//import org.locationtech.jts.geom.Geometry;
//import org.locationtech.jts.io.WKBReader;

import java.io.File;

public class ATLChainTest {
    File certFile = new File(this.getClass().getResource("/certs/user/cert.pem").getPath());
    File skFile = new File(this.getClass().getResource("/certs/user/user_sk").getPath());

    ATLChain atlChain = new ATLChain(
            certFile,
            skFile,
            "TestOrgA",
            "grpc://172.16.15.66:7051",
            "TestOrgA",
            "admin",
            "OrdererTestOrgA",
            "grpc://172.16.15.66:7050"
    );

    @Test
    public void testQuery() {
        try {
            String result = atlChain.query(
                    "atlchannel",
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
                    "atlchannel",
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

//    @Test
//    public void testByteInvoke() {
//        try {
//            byte[] bytes = "ByteContent".getBytes();
//            byte[] byteKey =  "bytekey".getBytes();
//
//            String result = atlChain.invokeByte(
//                    "atlchannel",
//                    "bincc",
//                    "PutByteArray",
//                    new byte[][]{byteKey, bytes}
//            );
//            System.out.println(result);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void testByteQuery() {
//        try {
//            byte[] byteKey =  "Line".getBytes();
//
//            byte[][] result = atlChain.queryByte(
//                    "atlchannel",
//                    "bincc",
//                    "GetByteArray",
//                    new byte[][]{byteKey}
//            );
//            System.out.println(result.toString());
//            System.out.println(result.length);
//
//            WKBReader wkbReader = new WKBReader();
//            Geometry geometry = wkbReader.read(result[0]);
//            System.out.println(geometry.toString());
//
//            Assert.assertNotEquals("", result);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}