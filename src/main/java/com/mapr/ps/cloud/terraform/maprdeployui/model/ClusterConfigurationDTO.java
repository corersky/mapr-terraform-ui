package com.mapr.ps.cloud.terraform.maprdeployui.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClusterConfigurationDTO implements Serializable {
    private Date deployedAt;
    private Date destroyedAt;
    private String customerName;
    private String envPrefix;
    private String clusterName;
    private String privateDomain;
    private AwsRegionDTO awsRegion;
    private String awsAvZone;
    private DefaultClusterLayoutDTO defaultClusterLayout;
    private AwsInstanceDTO awsInstanceType;
    private List<NodeLayoutDTO> nodesLayout = new ArrayList<>();
    private DeploymentStatus deploymentStatus;

    public DeploymentStatus getDeploymentStatus() {
        return deploymentStatus;
    }

    public void setDeploymentStatus(DeploymentStatus deploymentStatus) {
        this.deploymentStatus = deploymentStatus;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getEnvPrefix() {
        return envPrefix;
    }

    public void setEnvPrefix(String envPrefix) {
        this.envPrefix = envPrefix;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getPrivateDomain() {
        return privateDomain;
    }

    public void setPrivateDomain(String privateDomain) {
        this.privateDomain = privateDomain;
    }


    public String getAwsAvZone() {
        return awsAvZone;
    }

    public void setAwsAvZone(String awsAvZone) {
        this.awsAvZone = awsAvZone;
    }

    public DefaultClusterLayoutDTO getDefaultClusterLayout() {
        return defaultClusterLayout;
    }

    public void setDefaultClusterLayout(DefaultClusterLayoutDTO defaultClusterLayout) {
        this.defaultClusterLayout = defaultClusterLayout;
    }

    public AwsRegionDTO getAwsRegion() {
        return awsRegion;
    }

    public void setAwsRegion(AwsRegionDTO awsRegion) {
        this.awsRegion = awsRegion;
    }

    public AwsInstanceDTO getAwsInstanceType() {
        return awsInstanceType;
    }

    public void setAwsInstanceType(AwsInstanceDTO awsInstanceType) {
        this.awsInstanceType = awsInstanceType;
    }

    public List<NodeLayoutDTO> getNodesLayout() {
        return nodesLayout;
    }

    public void setNodesLayout(List<NodeLayoutDTO> nodesLayout) {
        this.nodesLayout = nodesLayout;
    }

    public Date getDeployedAt() {
        return deployedAt;
    }

    public void setDeployedAt(Date deployedAt) {
        this.deployedAt = deployedAt;
    }

    public Date getDestroyedAt() {
        return destroyedAt;
    }

    public void setDestroyedAt(Date destroyedAt) {
        this.destroyedAt = destroyedAt;
    }

    @Override
    public String toString() {
        return "ClusterConfigurationDTO{" +
                "deployedAt=" + deployedAt +
                ", destroyedAt=" + destroyedAt +
                ", customerName='" + customerName + '\'' +
                ", envPrefix='" + envPrefix + '\'' +
                ", clusterName='" + clusterName + '\'' +
                ", privateDomain='" + privateDomain + '\'' +
                ", awsRegion=" + awsRegion +
                ", awsAvZone='" + awsAvZone + '\'' +
                ", defaultClusterLayout=" + defaultClusterLayout +
                ", awsInstanceType=" + awsInstanceType +
                ", nodesLayout=" + nodesLayout +
                ", deploymentStatus=" + deploymentStatus +
                '}';
    }
}
