package com.mapr.ps.cloud.terraform.maprdeployui.web.pages;

import com.mapr.ps.cloud.terraform.maprdeployui.model.*;
import com.mapr.ps.cloud.terraform.maprdeployui.service.InvalidClusterStateException;
import com.mapr.ps.cloud.terraform.maprdeployui.service.MaprClusterService;
import com.mapr.ps.cloud.terraform.maprdeployui.web.components.ClusterConfigurationPanel;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.*;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.time.Duration;
import org.wicketstuff.annotation.mount.MountPath;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@MountPath("/moreinfo")
@AuthorizeInstantiation("USER")
public class MoreInfoPage extends BasePage {
    @SpringBean
    private MaprClusterService maprClusterService;

    private final IModel<ClusterConfigurationDTO> clusterConfigModel;
    private final IModel<AdditionalClusterInfoDTO> additionalClusterInfoModel;
    private final IModel<File> openvpnFilePathModel;
    private final IModel<File> logFilePathModel;

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
        this.logFilePathModel = new LoadableDetachableModel<File>() {
            @Override
            protected File load() {
                File logFile = maprClusterService.getLogFile(prefixModel.getObject());
                if(logFile != null && logFile.exists()) {
                    return logFile;
                }
                return null;
            }
        };
        add(statusRefreshContainer());
        add(additionalInfoRefreshContainer());
        add(clusterConfigurationPanel());
    }

    private WebMarkupContainer statusRefreshContainer() {
        WebMarkupContainer statusRefreshContainer = new WebMarkupContainer("statusRefreshContainer");
        statusRefreshContainer.add(statusDeployingContainer());
        statusRefreshContainer.add(statusDeployedContainer());
        statusRefreshContainer.add(statusDestroyingContainer());
        statusRefreshContainer.add(statusDestroyedContainer());
        statusRefreshContainer.add(statusAbortedContainer());
        statusRefreshContainer.add(statusAbortingContainer());
        statusRefreshContainer.add(statusWaitDeployContainer());
        statusRefreshContainer.add(statusWaitDestroyContainer());
        statusRefreshContainer.add(statusFailedContainer());
        statusRefreshContainer.add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(5)) {
            @Override
            public void beforeRender(Component component) {
                clusterConfigModel.detach();
                super.beforeRender(component);
            }
        });
        statusRefreshContainer.add(destroyButton());
        statusRefreshContainer.add(redeployButton());
        statusRefreshContainer.add(deleteButton());
        statusRefreshContainer.add(abortButton());
        statusRefreshContainer.setOutputMarkupId(true);
        return statusRefreshContainer;
    }

    private WebMarkupContainer additionalInfoRefreshContainer() {
        WebMarkupContainer additionalInfoRefreshContainer = new WebMarkupContainer("additionalInfoRefreshContainer");
        additionalInfoRefreshContainer.add(new Label("maprUser", new PropertyModel<>(additionalClusterInfoModel, "maprUser")));
        additionalInfoRefreshContainer.add(new Label("maprPassword", new PropertyModel<>(additionalClusterInfoModel, "maprPassword")));
        additionalInfoRefreshContainer.add(new Label("sshConnection", new PropertyModel<>(additionalClusterInfoModel, "sshConnection")));
        ExternalLink mcsUrlLink = new ExternalLink("mcsUrl", new PropertyModel<>(additionalClusterInfoModel, "mcsUrl"));
        mcsUrlLink.add(new Label("mcsUrlLabel", new PropertyModel<>(additionalClusterInfoModel, "mcsUrl")));
        additionalInfoRefreshContainer.add(mcsUrlLink);
        ExternalLink extDsrLink = new ExternalLink("extDsrUrl", new PropertyModel<>(additionalClusterInfoModel, "extDsrUrl"));
        extDsrLink.add(new Label("extDsrUrlLabel", new LoadableDetachableModel<String>() {
            @Override
            protected String load() {
                AdditionalClusterInfoDTO dto = additionalClusterInfoModel.getObject();
                if(dto.getExtDsrInstalled() == 1) {
                    return dto.getExtDsrUrl();
                }
                return "Not Installed";
            }
        }));
        additionalInfoRefreshContainer.add(extDsrLink);


        additionalInfoRefreshContainer.add(new DownloadLink("openvpnDownloadButton", openvpnFilePathModel) {
            @Override
            public boolean isEnabled() {
                return openvpnFilePathModel.getObject() != null;
            }
        });


        Link<Void> components = new Link<Void>("showOutputLog") {
            @Override
            public void onClick() {
                File file = logFilePathModel.getObject();
                FileResourceStream resourceStream = new FileResourceStream(file);
                ResourceStreamRequestHandler handler = new ResourceStreamRequestHandler(resourceStream, "deployment.log");
                handler.setContentDisposition(ContentDisposition.INLINE);
                handler.setCacheDuration(Duration.NONE);
                getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
            }

            @Override
            public boolean isEnabled() {
                return logFilePathModel.getObject() != null;
            }
        };
        components.setPopupSettings(new PopupSettings(PopupSettings.RESIZABLE | PopupSettings.SCROLLBARS));
        additionalInfoRefreshContainer.add(components);


        additionalInfoRefreshContainer.add(new ListView<DeploymentStatusDefinition>("deploymentStatusList", createDeploymentDefinitionModel()) {
            @Override
            protected void populateItem(final ListItem<DeploymentStatusDefinition> item) {
                WebMarkupContainer finishedContainer = new WebMarkupContainer("finishedContainer") {
                    @Override
                    public boolean isVisible() {
                        return clusterConfigModel.getObject().getDeploymentComponents().contains(item.getModelObject().getComponent());
                    }
                };
                finishedContainer.add(new Label("finishedLabel", new PropertyModel<>(item.getModel(), "label")));
                item.add(finishedContainer);
                WebMarkupContainer pendingContainer = new WebMarkupContainer("pendingContainer") {
                    @Override
                    public boolean isVisible() {
                        return !clusterConfigModel.getObject().getDeploymentComponents().contains(item.getModelObject().getComponent());
                    }
                };
                pendingContainer.add(new Label("pendingLabel", new PropertyModel<>(item.getModel(), "label")));
                item.add(pendingContainer);
            }
        });

        additionalInfoRefreshContainer.add(new

            AjaxSelfUpdatingTimerBehavior(Duration.seconds(5))

            {
                @Override
                public void beforeRender (Component component){
                additionalClusterInfoModel.detach();
                openvpnFilePathModel.detach();
                logFilePathModel.detach();
                super.beforeRender(component);
            }
            });
        additionalInfoRefreshContainer.setOutputMarkupId(true);
        return additionalInfoRefreshContainer;
        }

    private IModel<List<DeploymentStatusDefinition>> createDeploymentDefinitionModel() {
        List<DeploymentStatusDefinition> defs = new ArrayList<>();
        defs.add(new DeploymentStatusDefinition("VPC provisioned", DeploymentComponent.VPC));
        defs.add(new DeploymentStatusDefinition("EC2 instances provisioned", DeploymentComponent.EC2));
        defs.add(new DeploymentStatusDefinition("OpenVPN provisioned", DeploymentComponent.OPENVPN));
        defs.add(new DeploymentStatusDefinition("MapR installation", DeploymentComponent.ANSIBLE));
        if(clusterConfigModel.getObject().isExtensionDsr()) {
            defs.add(new DeploymentStatusDefinition("Extension: Data Science Refinery", DeploymentComponent.EXT_DSR));
        }
        if(clusterConfigModel.getObject().isExtensionDsr()) {
            defs.add(new DeploymentStatusDefinition("Extension: Usecase 1 (Clickstream)", DeploymentComponent.EXT_USECASE1));
        }
        defs.add(new DeploymentStatusDefinition("Post actions", DeploymentComponent.ALL));
        return Model.ofList(defs);
    }

    private ClusterConfigurationPanel clusterConfigurationPanel() {
        return new ClusterConfigurationPanel("clusterConfiguration", clusterConfigModel) {
            @Override
            public boolean isReadOnly() {
                return true;
            }

            @Override
            protected String getSubmitButtonLabel() {
                return "Not required";
            }

            @Override
            protected void onSubmit(AjaxRequestTarget target, IModel model, FeedbackPanel feedback) {

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
                DeploymentStatus deploymentStatus = clusterConfigModel.getObject().getDeploymentStatus();
                return deploymentStatus == DeploymentStatus.DESTROYED || deploymentStatus == DeploymentStatus.FAILED;
            }
        };
    }

    private Link<Void> abortButton() {
        return new Link<Void>("abortButton") {
            @Override
            public void onClick() {
                try {
                    maprClusterService.abortClusterDeployment(clusterConfigModel.getObject());
                } catch (InvalidClusterStateException e) {
                    error(e.getMessage());
                    return;
                }
                info("Cluster is being aborted.");
            }

            @Override
            public boolean isVisible() {
                DeploymentStatus deploymentStatus = clusterConfigModel.getObject().getDeploymentStatus();
//                return deploymentStatus == DeploymentStatus.DEPLOYING || deploymentStatus == DeploymentStatus.WAIT_DEPLOY;
                return deploymentStatus == DeploymentStatus.WAIT_DEPLOY;
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
                DeploymentStatus deploymentStatus = clusterConfigModel.getObject().getDeploymentStatus();
                return deploymentStatus == DeploymentStatus.DEPLOYED  || deploymentStatus == DeploymentStatus.FAILED || deploymentStatus == DeploymentStatus.ABORTED;
            }
        };
    }

    private WebMarkupContainer statusWaitDestroyContainer() {
        return new WebMarkupContainer("statusWaitDestroy") {
            @Override
            public boolean isVisible() {
                return clusterConfigModel.getObject().getDeploymentStatus() == DeploymentStatus.WAIT_DESTROY;
            }
        };
    }

    private WebMarkupContainer statusWaitDeployContainer() {
        return new WebMarkupContainer("statusWaitDeploy") {
            @Override
            public boolean isVisible() {
                return clusterConfigModel.getObject().getDeploymentStatus() == DeploymentStatus.WAIT_DEPLOY;
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

    private WebMarkupContainer statusAbortedContainer() {
        return new WebMarkupContainer("statusAborted") {
            @Override
            public boolean isVisible() {
                return clusterConfigModel.getObject().getDeploymentStatus() == DeploymentStatus.ABORTED;
            }
        };
    }

    private WebMarkupContainer statusAbortingContainer() {
        return new WebMarkupContainer("statusAborting") {
            @Override
            public boolean isVisible() {
                return clusterConfigModel.getObject().getDeploymentStatus() == DeploymentStatus.ABORTING;
            }
        };
    }

    private WebMarkupContainer statusFailedContainer() {
        return new WebMarkupContainer("statusFailed") {
            @Override
            public boolean isVisible() {
                return clusterConfigModel.getObject().getDeploymentStatus() == DeploymentStatus.FAILED;
            }
        };
    }
}
