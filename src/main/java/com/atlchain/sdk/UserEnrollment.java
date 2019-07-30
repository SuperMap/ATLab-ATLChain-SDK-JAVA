package com.atlchain.sdk;

import org.hyperledger.fabric.sdk.Enrollment;

import java.io.Serializable;
import java.security.PrivateKey;

class UserEnrollment implements Enrollment, Serializable {
    private static final long serialVersionUID = 3035666776718760411L;
    private PrivateKey key;
    private String cert;

    public UserEnrollment(PrivateKey key, String cert) {
        this.key = key;
        this.cert = cert;
    }

    @Override
    public PrivateKey getKey() {
        return key;
    }

    @Override
    public String getCert() {
        return cert;
    }
}

