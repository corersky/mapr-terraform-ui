package com.mapr.ps.cloud.terraform.maprdeployui.model;

import java.io.Serializable;
import java.util.Objects;

public class MaprClusterDTO implements Serializable {
    private String name;
    private String domain;
    private String customer;
    private String prefix;
    private String avZone;
    private String instanceType;
    private Integer numberNodes;

    public MaprClusterDTO() {
    }

    public MaprClusterDTO(String name, String domain, String customer, String prefix, String avZone, String instanceType, Integer numberNodes) {
        this.name = name;
        this.domain = domain;
        this.customer = customer;
        this.prefix = prefix;
        this.avZone = avZone;
        this.instanceType = instanceType;
        this.numberNodes = numberNodes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getAvZone() {
        return avZone;
    }

    public void setAvZone(String avZone) {
        this.avZone = avZone;
    }

    public String getInstanceType() {
        return instanceType;
    }

    public void setInstanceType(String instanceType) {
        this.instanceType = instanceType;
    }

    public Integer getNumberNodes() {
        return numberNodes;
    }

    public void setNumberNodes(Integer numberNodes) {
        this.numberNodes = numberNodes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MaprClusterDTO that = (MaprClusterDTO) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(domain, that.domain) &&
                Objects.equals(customer, that.customer) &&
                Objects.equals(prefix, that.prefix) &&
                Objects.equals(avZone, that.avZone) &&
                Objects.equals(instanceType, that.instanceType) &&
                Objects.equals(numberNodes, that.numberNodes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, domain, customer, prefix, avZone, instanceType, numberNodes);
    }

    @Override
    public String toString() {
        return "MaprClusterDTO{" +
                "name='" + name + '\'' +
                ", domain='" + domain + '\'' +
                ", customer='" + customer + '\'' +
                ", prefix='" + prefix + '\'' +
                ", avZone='" + avZone + '\'' +
                ", instanceType='" + instanceType + '\'' +
                ", numberNodes=" + numberNodes +
                '}';
    }
}
