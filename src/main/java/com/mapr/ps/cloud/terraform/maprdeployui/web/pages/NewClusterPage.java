package com.mapr.ps.cloud.terraform.maprdeployui.web.pages;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

@MountPath("/create_new_cluster")
public class NewClusterPage extends BasePage {

    public NewClusterPage(PageParameters parameters) {
        super(parameters);
    }
}
