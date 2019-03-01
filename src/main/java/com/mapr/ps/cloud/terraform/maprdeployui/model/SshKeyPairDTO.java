package com.mapr.ps.cloud.terraform.maprdeployui.model;

import java.io.Serializable;
import java.util.Date;

public class SshKeyPairDTO implements Serializable {
    private Date createdAt;
    private String id;
    private String name;
    private String privateKey;
    private String publicKey;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    @Override
    public String toString() {
        return "SshKeyPairDTO{" +
                "createdAt=" + createdAt +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", publicKey='" + publicKey + '\'' +
                '}';
    }
}
