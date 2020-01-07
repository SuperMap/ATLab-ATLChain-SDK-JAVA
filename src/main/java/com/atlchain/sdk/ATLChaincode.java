package com.atlchain.sdk;

import org.hyperledger.fabric.sdk.exception.ChaincodeEndorsementPolicyParseException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;

import java.io.File;
import java.io.IOException;

public interface ATLChaincode {

    public String install() throws InvalidArgumentException, ProposalException;

    public String instantiate(File chaincodeEndorsementPolicy) throws IOException, ChaincodeEndorsementPolicyParseException;

    public String upgrade();

    public String[] list();

    public String pack();

    public String signpackage();
}
