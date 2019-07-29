package com.atlchain.sdk;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class ATLChainTest {

    @Test
    public void testQuery() {
        File certFile = new File(this.getClass().getResource("/certs/user/cert.pem").getPath());
        File skFile = new File(this.getClass().getResource("/certs/user/user_sk").getPath());
        ATLChain atlChain = new ATLChain(certFile, skFile, "TestOrgA", "grpc://127.0.0.1:7051", "TestOrgA", "user");
        try {
            String result = atlChain.query("atlchannel", "javacc", "query", new String[]{"a"});
            System.out.println(result);
            Assert.assertNotEquals("", result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Test
//    public void testInvoke() {
//        File certFile = new File(this.getClass().getResource("/certs/admin/cert.pem").getPath());
//        File skFile = new File(this.getClass().getResource("/certs/admin/admin_sk").getPath());
//
//        ATLChain atlChain = new ATLChain(certFile, skFile, "TestOrgA", "grpc://127.0.0.1:7051", "TestOrgA", "admin", "OrdererTestOrgA", "grpc://127.0.0.1:7050");
//
//        try {
//            String result = atlChain.invoke("atlchannel", "javacc", "invoke", new String[]{"a", "b" ,"10"});
//            System.out.println(result);
//            Assert.assertNotEquals("", result);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}