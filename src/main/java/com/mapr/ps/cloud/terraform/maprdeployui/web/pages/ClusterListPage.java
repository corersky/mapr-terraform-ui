package com.mapr.ps.cloud.terraform.maprdeployui.web.pages;

import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;
import com.mapr.ps.cloud.terraform.maprdeployui.model.ClusterConfigurationDTO;
import com.mapr.ps.cloud.terraform.maprdeployui.model.DeploymentStatus;
import com.mapr.ps.cloud.terraform.maprdeployui.service.MaprClusterService;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.List;

@WicketHomePage
@MountPath("/clusters")
public class ClusterListPage extends BasePage {

    @SpringBean
    private MaprClusterService maprClusterService;
    private final IModel<List<ClusterConfigurationDTO>> clustersModel;

    public ClusterListPage() {
        clustersModel = new LoadableDetachableModel<List<ClusterConfigurationDTO>>() {
            @Override
            protected List<ClusterConfigurationDTO> load() {
                return maprClusterService.getMaprClusters();
            }
        };

        add(new BookmarkablePageLink<NewClusterPage>("newClusterTableLink", NewClusterPage.class));
        add(tableContainer());
    }

    private Component tableContainer() {
        ListView<ClusterConfigurationDTO> listView = new ListView<ClusterConfigurationDTO>("clusters", clustersModel) {
            @Override
            protected void populateItem(final ListItem<ClusterConfigurationDTO> item) {
                item.add(new Label("clusterName", new PropertyModel<String>(item.getModel(), "clusterName")));
                item.add(new Label("privateDomain", new PropertyModel<String>(item.getModel(), "privateDomain")));
                item.add(new Label("customerName", new PropertyModel<String>(item.getModel(), "customerName")));
                item.add(new Label("envPrefix", new PropertyModel<String>(item.getModel(), "envPrefix")));
                item.add(new Label("awsAvZone", new PropertyModel<String>(item.getModel(), "awsAvZone")));
                item.add(new Label("awsInstanceType", new PropertyModel<String>(item.getModel(), "awsInstanceType.instanceCode")));
                item.add(new Label("numberNodes", new PropertyModel<String>(item.getModel(), "defaultClusterLayout.numberNodes")));
                item.add(new Label("uptime", new IModel<String>() {
                    @Override
                    public String getObject() {
                        ClusterConfigurationDTO modelObject = item.getModelObject();
                        if(modelObject.getDeploymentStatus() != DeploymentStatus.DESTROYED && modelObject.getDeploymentStatus() != DeploymentStatus.WAIT_DEPLOY) {
                            long duration = System.currentTimeMillis() - modelObject.getDeployedAt().getTime();
                            return DurationFormatUtils.formatDurationWords(duration, true, true);
                        }
                        return "offline";
                    }
                }));
                item.add(new WebMarkupContainer("statusDeploying") {
                    @Override
                    public boolean isVisible() {
                        return item.getModelObject().getDeploymentStatus() == DeploymentStatus.DEPLOYING;
                    }
                });
                item.add(new WebMarkupContainer("statusDeployed") {
                    @Override
                    public boolean isVisible() {
                        return item.getModelObject().getDeploymentStatus() == DeploymentStatus.DEPLOYED;
                    }
                });
                item.add(new WebMarkupContainer("statusDestroying") {
                    @Override
                    public boolean isVisible() {
                        return item.getModelObject().getDeploymentStatus() == DeploymentStatus.DESTROYING;
                    }
                });
                item.add(new WebMarkupContainer("statusDestroyed") {
                    @Override
                    public boolean isVisible() {
                        return item.getModelObject().getDeploymentStatus() == DeploymentStatus.DESTROYED;
                    }
                });
                item.add(new WebMarkupContainer("statusWaitDestroy") {
                    @Override
                    public boolean isVisible() {
                        return item.getModelObject().getDeploymentStatus() == DeploymentStatus.WAIT_DESTROY;
                    }
                });
                item.add(new WebMarkupContainer("statusWaitDeploy") {
                    @Override
                    public boolean isVisible() {
                        return item.getModelObject().getDeploymentStatus() == DeploymentStatus.WAIT_DEPLOY;
                    }
                });
                item.add(new WebMarkupContainer("statusFailed") {
                    @Override
                    public boolean isVisible() {
                        return item.getModelObject().getDeploymentStatus() == DeploymentStatus.FAILED;
                    }
                });
                item.add(new Link<Void>("moreInfoLink") {
                    @Override
                    public void onClick() {
                        setResponsePage(new MoreInfoPage(new PropertyModel<>(item.getModel(), "envPrefix")));
                    }
                });
            }
        };

        WebMarkupContainer tableContainer = new WebMarkupContainer("tableContainer");
        tableContainer.add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(10)) {
            @Override
            public void beforeRender(Component component) {
                clustersModel.detach();
                super.beforeRender(component);
            }
        });
        tableContainer.add(listView);
        tableContainer.setOutputMarkupId(true);
        return tableContainer;
    }
}