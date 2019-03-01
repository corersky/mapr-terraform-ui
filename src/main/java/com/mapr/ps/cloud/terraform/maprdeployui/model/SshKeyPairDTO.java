package com.mapr.ps.cloud.terraform.maprdeployui.model;

import java.io.Serializable;
import java.util.Date;

public class SshKeyPairDTO implements Serializable {
    private Date createdt;
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

    public Date getCreatedt() {
        return createdt;
    }

    public void setCreatedt(Date createdt) {
        this.createdt = createdt;
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
                "createdt=" + createdt +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", publicKey='" + publicKey + '\'' +
                '}';
    }
}
