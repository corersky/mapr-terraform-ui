package com.mapr.ps.cloud.terraform.maprdeployui.model;

import java.io.Serializable;

public class AdditionalClusterInfoDTO implements Serializable {
    private boolean dataAvailable;
    private String mcsUrl;
    private String openvpnFile;
    private String maprUser;
    private String maprPassword;
    private String dbPassword;
    private String domainName;
    private String envPrefix;
    private String sshConnection;

    public boolean isDataAvailable() {
        return dataAvailable;
    }

    public void setDataAvailable(boolean dataAvailable) {
        this.dataAvailable = dataAvailable;
    }

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

    public String getSshConnection() {
        return sshConnection;
    }

    public void setSshConnection(String sshConnection) {
        this.sshConnection = sshConnection;
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


