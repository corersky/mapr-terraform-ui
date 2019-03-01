package com.mapr.ps.cloud.terraform.maprdeployui.model;

public class TemplateDTO extends ClusterConfigurationDTO {
    private String templateId;

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    @Override
    public String toString() {
        return "TemplateDTO{" +
                "templateId='" + templateId + '\'' +
                '}';
    }
}
