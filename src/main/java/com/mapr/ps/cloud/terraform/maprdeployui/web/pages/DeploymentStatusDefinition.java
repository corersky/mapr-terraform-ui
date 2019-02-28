package com.mapr.ps.cloud.terraform.maprdeployui.web.pages;

import com.mapr.ps.cloud.terraform.maprdeployui.model.DeploymentComponent;

import java.io.Serializable;

public class DeploymentStatusDefinition implements Serializable {
    private String label;
    private DeploymentComponent component;

    public DeploymentStatusDefinition() {
    }

    public DeploymentStatusDefinition(String label, DeploymentComponent component) {
        this.label = label;
        this.component = component;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public DeploymentComponent getComponent() {
        return component;
    }

    public void setComponent(DeploymentComponent component) {
        this.component = component;
    }

    @Override
    public String toString() {
        return "DeploymentStatusDefinition{" +
                "label='" + label + '\'' +
                ", component=" + component +
                '}';
    }
}
