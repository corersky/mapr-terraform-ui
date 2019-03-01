package com.mapr.ps.cloud.terraform.maprdeployui.model;

import java.io.Serializable;
import java.util.Date;

public class SshKeyPairFileRefDTO implements Serializable {
    private Date createdt;
    private String id;
    private String name;
    private String privateKeyFile;
    private String publicKeyFile;

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

    public String getPrivateKeyFile() {
        return privateKeyFile;
    }

    public void setPrivateKeyFile(String privateKeyFile) {
        this.privateKeyFile = privateKeyFile;
    }

    public String getPublicKeyFile() {
        return publicKeyFile;
    }

    public void setPublicKeyFile(String publicKeyFile) {
        this.publicKeyFile = publicKeyFile;
    }

    @Override
    public String toString() {
        return "SshKeyPairFileRefDTO{" +
                "createdt=" + createdt +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", privateKeyFile='" + privateKeyFile + '\'' +
                ", publicKeyFile='" + publicKeyFile + '\'' +
                '}';
    }
}
