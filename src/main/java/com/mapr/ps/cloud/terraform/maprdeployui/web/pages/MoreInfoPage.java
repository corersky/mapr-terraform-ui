package com.mapr.ps.cloud.terraform.maprdeployui.web.pages;

import com.mapr.ps.cloud.terraform.maprdeployui.model.ClusterConfigurationDTO;
import com.mapr.ps.cloud.terraform.maprdeployui.web.components.ClusterConfigurationPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

@MountPath("/moreinfo")
public class MoreInfoPage extends BasePage {

    public MoreInfoPage(IModel<ClusterConfigurationDTO> model) {
        add(new ClusterConfigurationPanel("clusterConfiguration", model) {
            @Override
            public boolean isReadOnly() {
                return true;
            }
        });
    }
}
