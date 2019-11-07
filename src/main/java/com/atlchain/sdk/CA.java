package com.atlchain.sdk;

import org.hyperledger.fabric.sdk.NetworkConfig;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException;

import java.io.File;
import java.io.IOException;

public class CA {
    private NetworkConfig networkConfig;
    private HFCAClient hfcaClient;
//
//    public void CA(File networkFile) throws IOException, InvalidArgumentException {
//        NetworkConfig.CAInfo networkConfig = NetworkConfig.CAInfo.fromYamlFile(networkFile);
//        hfcaClient = HFCAClient.createNewInstance(networkConfig);
//    }

}
