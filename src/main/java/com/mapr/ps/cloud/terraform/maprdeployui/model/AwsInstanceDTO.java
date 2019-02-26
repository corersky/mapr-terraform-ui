package com.mapr.ps.cloud.terraform.maprdeployui.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class AwsInstanceDTO implements Serializable {
    private String instanceCode;
    private int vCPU;
    private int memoryInGB;
    private String storage;
    private String network;
    private List<String> availableRegion;

    public AwsInstanceDTO(String instanceCode, int vCPU, int memoryInGB, String storage, String network, List<String> availableRegion) {
        this.instanceCode = instanceCode;
        this.vCPU = vCPU;
        this.memoryInGB = memoryInGB;
        this.storage = storage;
        this.network = network;
        this.availableRegion = availableRegion;
    }

    public String getInstanceCode() {
        return instanceCode;
    }

    public int getvCPU() {
        return vCPU;
    }

    public int getMemoryInGB() {
        return memoryInGB;
    }

    public String getStorage() {
        return storage;
    }

    public String getNetwork() {
        return network;
    }

    public List<String> getAvailableRegion() {
        return availableRegion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AwsInstanceDTO that = (AwsInstanceDTO) o;
        return Objects.equals(instanceCode, that.instanceCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instanceCode);
    }

    @Override
    public String toString() {
        return "AwsInstanceDTO{" +
                "instanceCode='" + instanceCode + '\'' +
                ", vCPU=" + vCPU +
                ", memoryInGB=" + memoryInGB +
                ", storage='" + storage + '\'' +
                ", network='" + network + '\'' +
                ", availableRegion=" + availableRegion +
                '}';
    }
}
