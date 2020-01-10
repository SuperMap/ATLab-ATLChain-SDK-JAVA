package com.supermap.blockchain.sdk;

import org.hyperledger.fabric.protos.peer.Query;
import org.hyperledger.fabric.sdk.BlockEvent;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.TransactionRequest;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 链码处理类
 */
public interface SmChaincode {

    boolean install(
            String chaincodeName,
            String chaincodeVersion,
            String chaincodePath,
            TransactionRequest.Type type
    );

    CompletableFuture<BlockEvent.TransactionEvent> instantiate(
            String chaincodeName,
            String chaincodeVersion,
            String chaincodePath,
            File chaincodeEndorsementPolicyFile,
            TransactionRequest.Type type
    );

    CompletableFuture<BlockEvent.TransactionEvent> upgrade(
            String chaincodeName,
            String chaincodeVersion,
            String chaincodePath,
            File chaincodeEndorsementPolicyFile,
            TransactionRequest.Type type
    );

    List<Query.ChaincodeInfo> listInstalled(Peer peer);

    List<Query.ChaincodeInfo> listInstantiated(Peer peer);

    // Fabric sdk java not support.
    // public String pack();
    // public String signpackage();
}
