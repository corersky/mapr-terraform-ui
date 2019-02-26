package com.mapr.ps.cloud.terraform.maprdeployui.web.pages;

import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;
import com.mapr.ps.cloud.terraform.maprdeployui.model.ClusterConfigurationDTO;
import com.mapr.ps.cloud.terraform.maprdeployui.service.MaprClusterServiceMock;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.List;

@WicketHomePage
@MountPath("/clusters")
public class ClusterListPage extends BasePage {

    @SpringBean
    private MaprClusterServiceMock maprClusterService;

    public ClusterListPage() {
        IModel<List<ClusterConfigurationDTO>> clustersModel = new LoadableDetachableModel<List<ClusterConfigurationDTO>>() {
            @Override
            protected List<ClusterConfigurationDTO> load() {
                return maprClusterService.getMaprClusters();
            }
        };

        add(new BookmarkablePageLink<NewClusterPage>("newClusterTableLink", NewClusterPage.class));
        add(new ListView<ClusterConfigurationDTO>("clusters", clustersModel) {
            @Override
            protected void populateItem(ListItem<ClusterConfigurationDTO> item) {
                item.add(new Label("clusterName", new PropertyModel<String>(item.getModel(), "clusterName")));
                item.add(new Label("privateDomain", new PropertyModel<String>(item.getModel(), "privateDomain")));
                item.add(new Label("customerName", new PropertyModel<String>(item.getModel(), "customerName")));
                item.add(new Label("envPrefix", new PropertyModel<String>(item.getModel(), "envPrefix")));
                item.add(new Label("awsAvZone", new PropertyModel<String>(item.getModel(), "awsAvZone")));
                item.add(new Label("awsInstanceType", new PropertyModel<String>(item.getModel(), "awsInstanceType.instanceCode")));
                item.add(new Label("numberNodes", new PropertyModel<String>(item.getModel(), "defaultClusterLayout.numberNodes")));
                item.add(new Link<Void>("moreInfoLink") {
                    @Override
                    public void onClick() {
                        setResponsePage(new MoreInfoPage(item.getModel()));
                    }
                });
            }
        });
    }
}