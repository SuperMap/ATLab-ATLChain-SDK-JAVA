package com.supermap.blockchain.sdk;

import org.hyperledger.fabric_ca.sdk.exception.GenerateCRLException;
import org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException;
import org.junit.Test;

import java.io.File;

public class SmCAImpTest {
    private final String networkConfigFile = this.getClass().getResource("/network-config-testC.yaml").getFile();
    private final SmChain smChain = SmChain.getSmChain("txchannel", new File(networkConfigFile));

    @Test
    public void getCRLTest() throws GenerateCRLException, InvalidArgumentException {
        SmCA smCA = smChain.getSmCa("OrgC");
        String crl = smCA.getCRL(null, null, null, null);
        System.out.println(crl);
    }
}