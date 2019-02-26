package com.mapr.ps.cloud.terraform.maprdeployui.web.pages;

import com.mapr.ps.cloud.terraform.maprdeployui.model.ClusterConfigurationDTO;
import com.mapr.ps.cloud.terraform.maprdeployui.model.DeploymentStatus;
import com.mapr.ps.cloud.terraform.maprdeployui.web.components.ClusterConfigurationPanel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

@MountPath("/moreinfo")
public class MoreInfoPage extends BasePage {

    public MoreInfoPage(IModel<ClusterConfigurationDTO> model) {
        add(new WebMarkupContainer("statusDeploying") {
            @Override
            public boolean isVisible() {
                return model.getObject().getDeploymentStatus() == DeploymentStatus.DEPLOYING;
            }
        });
        add(new WebMarkupContainer("statusDeployed") {
            @Override
            public boolean isVisible() {
                return model.getObject().getDeploymentStatus() == DeploymentStatus.DEPLOYED;
            }
        });
        add(new WebMarkupContainer("statusDestroying") {
            @Override
            public boolean isVisible() {
                return model.getObject().getDeploymentStatus() == DeploymentStatus.DESTROYING;
            }
        });
        add(new WebMarkupContainer("statusDestroyed") {
            @Override
            public boolean isVisible() {
                return model.getObject().getDeploymentStatus() == DeploymentStatus.DESTROYED;
            }
        });

        add(new Button("destroyButton") {
            @Override
            public boolean isVisible() {
                return model.getObject().getDeploymentStatus() == DeploymentStatus.DEPLOYED;
            }
        });

        add(new Button("redeployButton") {
            @Override
            public boolean isVisible() {
                return model.getObject().getDeploymentStatus() == DeploymentStatus.DESTROYED;
            }
        });

        add(new Button("deleteButton") {
            @Override
            public boolean isVisible() {
                return model.getObject().getDeploymentStatus() == DeploymentStatus.DESTROYED;
            }
        });

        add(new ClusterConfigurationPanel("clusterConfiguration", model) {
            @Override
            public boolean isReadOnly() {
                return true;
            }
        });
    }
}
