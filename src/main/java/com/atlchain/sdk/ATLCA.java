package com.atlchain.sdk;

import org.hyperledger.fabric.sdk.NetworkConfig;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException;

import java.io.File;
import java.io.IOException;

public interface ATLCA {
    public void registe();

    public void enroll();

    public void reenroll();

    public void revoke();

    public void getCRL();
}
