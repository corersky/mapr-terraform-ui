package com.mapr.ps.cloud.terraform.maprdeployui.model;

import java.io.Serializable;

public class AdditionalClusterInfoDTO implements Serializable {
    private String mcsUrl;
    private String openvpnFile;
    private String maprUser;
    private String maprPassword;
    private String dbPassword;
    private String domainName;
    private String envPrefix;

    public String getMcsUrl() {
        return mcsUrl;
    }

    public void setMcsUrl(String mcsUrl) {
        this.mcsUrl = mcsUrl;
    }

    public String getOpenvpnFile() {
        return openvpnFile;
    }

    public void setOpenvpnFile(String openvpnFile) {
        this.openvpnFile = openvpnFile;
    }

    public String getMaprUser() {
        return maprUser;
    }

    public void setMaprUser(String maprUser) {
        this.maprUser = maprUser;
    }

    public String getMaprPassword() {
        return maprPassword;
    }

    public void setMaprPassword(String maprPassword) {
        this.maprPassword = maprPassword;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getEnvPrefix() {
        return envPrefix;
    }

    public void setEnvPrefix(String envPrefix) {
        this.envPrefix = envPrefix;
    }

    @Override
    public String toString() {
        return "AdditionalClusterInfoDTO{" +
                "mcsUrl='" + mcsUrl + '\'' +
                ", openvpnFile='" + openvpnFile + '\'' +
                ", maprUser='" + maprUser + '\'' +
                ", maprPassword='" + maprPassword + '\'' +
                ", dbPassword='" + dbPassword + '\'' +
                ", domainName='" + domainName + '\'' +
                ", envPrefix='" + envPrefix + '\'' +
                '}';
    }
}


