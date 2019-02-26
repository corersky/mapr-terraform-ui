package com.mapr.ps.cloud.terraform.maprdeployui.web.pages;

import com.mapr.ps.cloud.terraform.maprdeployui.model.AdditionalClusterInfoDTO;
import com.mapr.ps.cloud.terraform.maprdeployui.model.ClusterConfigurationDTO;
import com.mapr.ps.cloud.terraform.maprdeployui.model.DeploymentStatus;
import com.mapr.ps.cloud.terraform.maprdeployui.service.InvalidClusterStateException;
import com.mapr.ps.cloud.terraform.maprdeployui.service.MaprClusterServiceMock2;
import com.mapr.ps.cloud.terraform.maprdeployui.web.components.ClusterConfigurationPanel;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.DownloadLink;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import org.wicketstuff.annotation.mount.MountPath;

import java.io.File;

@MountPath("/moreinfo")
public class MoreInfoPage extends BasePage {
    @SpringBean
    private MaprClusterServiceMock2 maprClusterService;

    private final IModel<ClusterConfigurationDTO> clusterConfigModel;
    private final IModel<AdditionalClusterInfoDTO> additionalClusterInfoModel;
    private final IModel<File> openvpnFilePathModel;

    public MoreInfoPage(IModel<String> prefixModel) {
        this.clusterConfigModel = new LoadableDetachableModel<ClusterConfigurationDTO>() {
            @Override
            protected ClusterConfigurationDTO load() {
                return maprClusterService.getClusterConfigurationByEnvPrefix(prefixModel.getObject());
            }
        };
        this.additionalClusterInfoModel = new LoadableDetachableModel<AdditionalClusterInfoDTO>() {
            @Override
            protected AdditionalClusterInfoDTO load() {
                return maprClusterService.getAdditionalClusterInfo(prefixModel.getObject());
            }
        };
        this.openvpnFilePathModel = new LoadableDetachableModel<File>() {
            @Override
            protected File load() {
                File openvpnFile = maprClusterService.getOpenvpnFile(prefixModel.getObject());
                if(openvpnFile != null && openvpnFile.exists()) {
                    return openvpnFile;
                }
                return null;
            }
        };
        add(statusRefreshContainer());
        add(additionalInfoRefreshContainer());
        add(destroyButton());
        add(redeployButton());
        add(deleteButton());
        add(clusterConfigurationPanel());
    }

    private WebMarkupContainer statusRefreshContainer() {
        WebMarkupContainer statusRefreshContainer = new WebMarkupContainer("statusRefreshContainer");
        statusRefreshContainer.add(statusDeployingContainer());
        statusRefreshContainer.add(statusDeployedContainer());
        statusRefreshContainer.add(statusDestroyingContainer());
        statusRefreshContainer.add(statusDestroyedContainer());
        statusRefreshContainer.add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(5)) {
            @Override
            public void beforeRender(Component component) {
                clusterConfigModel.detach();
                super.beforeRender(component);
            }
        });
        statusRefreshContainer.setOutputMarkupId(true);
        return statusRefreshContainer;
    }

    private WebMarkupContainer additionalInfoRefreshContainer() {
        WebMarkupContainer additionalInfoRefreshContainer = new WebMarkupContainer("additionalInfoRefreshContainer");
        additionalInfoRefreshContainer.add(new Label("maprUser", new PropertyModel<>(additionalClusterInfoModel, "maprUser")));
        additionalInfoRefreshContainer.add(new Label("maprPassword", new PropertyModel<>(additionalClusterInfoModel, "maprPassword")));
        ExternalLink extLink = new ExternalLink("mcsUrl", new PropertyModel<>(additionalClusterInfoModel, "mcsUrl"));
        extLink.add(new Label("mcsUrlLabel", new PropertyModel<>(additionalClusterInfoModel, "mcsUrl")));
        additionalInfoRefreshContainer.add(extLink);

        additionalInfoRefreshContainer.add(new DownloadLink("openvpnDownloadButton", openvpnFilePathModel) {
            @Override
            public boolean isEnabled() {
                return openvpnFilePathModel.getObject() != null;
            }
        });
        additionalInfoRefreshContainer.add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(5)) {
            @Override
            public void beforeRender(Component component) {
                additionalClusterInfoModel.detach();
                openvpnFilePathModel.detach();
                super.beforeRender(component);
            }
        });
        additionalInfoRefreshContainer.setOutputMarkupId(true);
        additionalInfoRefreshContainer.setOutputMarkupPlaceholderTag(true);
        return additionalInfoRefreshContainer;
    }

    private ClusterConfigurationPanel clusterConfigurationPanel() {
        return new ClusterConfigurationPanel("clusterConfiguration", clusterConfigModel) {
            @Override
            public boolean isReadOnly() {
                return true;
            }
        };
    }

    private Link<Void> deleteButton() {
        return new Link<Void>("deleteButton") {
            @Override
            public void onClick() {
                try {
                    maprClusterService.deleteCluster(clusterConfigModel.getObject());
                    info("Cluster configuration was deleted.");
                    setResponsePage(ClusterListPage.class);
                } catch (InvalidClusterStateException e) {
                    error(e.getMessage());
                    return;
                }
            }

            @Override
            public boolean isVisible() {
                return clusterConfigModel.getObject().getDeploymentStatus() == DeploymentStatus.DESTROYED;
            }
        };
    }

    private Link<Void> redeployButton() {
        return new Link<Void>("redeployButton") {
            @Override
            public void onClick() {
                try {
                    maprClusterService.redeployCluster(clusterConfigModel.getObject());
                } catch (InvalidClusterStateException e) {
                    error(e.getMessage());
                    return;
                }
                info("Cluster is being re-deployed.");
            }

            @Override
            public boolean isVisible() {
                return clusterConfigModel.getObject().getDeploymentStatus() == DeploymentStatus.DESTROYED;
            }
        };
    }

    private Link<Void> destroyButton() {
        return new Link<Void>("destroyButton") {
            @Override
            public void onClick() {
                try {
                    maprClusterService.destroyCluster(clusterConfigModel.getObject());
                } catch (InvalidClusterStateException e) {
                    error(e.getMessage());
                    return;
                }
                info("Cluster is being destroyed.");
            }

            @Override
            public boolean isVisible() {
                return clusterConfigModel.getObject().getDeploymentStatus() == DeploymentStatus.DEPLOYED;
            }
        };
    }

    private WebMarkupContainer statusDestroyedContainer() {
        return new WebMarkupContainer("statusDestroyed") {
            @Override
            public boolean isVisible() {
                return clusterConfigModel.getObject().getDeploymentStatus() == DeploymentStatus.DESTROYED;
            }
        };
    }

    private WebMarkupContainer statusDestroyingContainer() {
        return new WebMarkupContainer("statusDestroying") {
            @Override
            public boolean isVisible() {
                return clusterConfigModel.getObject().getDeploymentStatus() == DeploymentStatus.DESTROYING;
            }
        };
    }

    private WebMarkupContainer statusDeployedContainer() {
        return new WebMarkupContainer("statusDeployed") {
            @Override
            public boolean isVisible() {
                return clusterConfigModel.getObject().getDeploymentStatus() == DeploymentStatus.DEPLOYED;
            }
        };
    }

    private WebMarkupContainer statusDeployingContainer() {
        return new WebMarkupContainer("statusDeploying") {
            @Override
            public boolean isVisible() {
                return clusterConfigModel.getObject().getDeploymentStatus() == DeploymentStatus.DEPLOYING;
            }
        };
    }
}
