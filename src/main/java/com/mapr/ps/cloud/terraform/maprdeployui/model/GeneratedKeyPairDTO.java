package com.mapr.ps.cloud.terraform.maprdeployui.model;

import java.io.Serializable;

public class GeneratedKeyPairDTO implements Serializable {
    private String privateKey;
    private String publicKey;

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
