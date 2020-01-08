package com.atlchain.sdk;

import org.hyperledger.fabric.protos.peer.Query;
import org.hyperledger.fabric.sdk.BlockEvent;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.TransactionRequest;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ATLChaincode {

    public boolean install(String chaincodeName, String chaincodeVersion, String chaincodePath, TransactionRequest.Type type);

    public CompletableFuture<BlockEvent.TransactionEvent> instantiate(String chaincodeName, String chaincodeVersion, String chaincodePath, File chaincodeEndorsementPolicyFile, TransactionRequest.Type type);

    public String upgrade();

    public List<Query.ChaincodeInfo> listInstalled(Peer peer);

    public List<Query.ChaincodeInfo> listInstantiated(Peer peer);

    // Fabric sdk java not support.
    // public String pack();
    // public String signpackage();
}
