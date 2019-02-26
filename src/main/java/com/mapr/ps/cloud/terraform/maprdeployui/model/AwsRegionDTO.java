package com.mapr.ps.cloud.terraform.maprdeployui.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class AwsRegionDTO implements Serializable {
    private String description;
    private String code;
    private List<String> zones;

    public AwsRegionDTO() {
    }

    public AwsRegionDTO(String description, String code, List<String> zones) {
        this.description = description;
        this.code = code;
        this.zones = Collections.unmodifiableList(zones);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setZones(List<String> zones) {
        this.zones = zones;
    }

    public String getDescription() {
        return description;
    }

    public String getCode() {
        return code;
    }

    public List<String> getZones() {
        return zones;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AwsRegionDTO that = (AwsRegionDTO) o;
        return Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        return "AwsRegionDTO{" +
                "description='" + description + '\'' +
                ", code='" + code + '\'' +
                ", zones=" + zones +
                '}';
    }
}
