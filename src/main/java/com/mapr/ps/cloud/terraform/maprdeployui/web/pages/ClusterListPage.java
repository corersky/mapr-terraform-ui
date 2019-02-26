package com.mapr.ps.cloud.terraform.maprdeployui.web.pages;

import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;
import com.mapr.ps.cloud.terraform.maprdeployui.model.MaprClusterDTO;
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

    public ClusterListPage(final PageParameters parameters) {
        super(parameters);
        IModel<List<MaprClusterDTO>> clustersModel = new LoadableDetachableModel<List<MaprClusterDTO>>() {
            @Override
            protected List<MaprClusterDTO> load() {
                return maprClusterService.getMaprClusters();
            }
        };

        add(new BookmarkablePageLink<NewClusterPage>("newClusterTableLink", NewClusterPage.class));
        add(new ListView<MaprClusterDTO>("clusters", clustersModel) {
            @Override
            protected void populateItem(ListItem<MaprClusterDTO> item) {
                item.add(new Label("name", new PropertyModel<String>(item.getModel(), "name")));
                item.add(new Label("domain", new PropertyModel<String>(item.getModel(), "domain")));
                item.add(new Label("customer", new PropertyModel<String>(item.getModel(), "customer")));
                item.add(new Label("prefix", new PropertyModel<String>(item.getModel(), "prefix")));
                item.add(new Label("avZone", new PropertyModel<String>(item.getModel(), "avZone")));
                item.add(new Label("instanceType", new PropertyModel<String>(item.getModel(), "instanceType")));
                item.add(new Label("numberNodes", new PropertyModel<String>(item.getModel(), "numberNodes")));
                item.add(new Link<Void>("moreInfoLink") {
                    @Override
                    public void onClick() {
                        setResponsePage(MoreInfoPage.class);
                    }
                });
            }
        });
    }
}