package com.mapr.ps.cloud.terraform.maprdeployui.web.pages;

import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;
import com.mapr.ps.cloud.terraform.maprdeployui.model.ClusterConfigurationDTO;
import com.mapr.ps.cloud.terraform.maprdeployui.model.DeploymentStatus;
import com.mapr.ps.cloud.terraform.maprdeployui.model.SshKeyPairDTO;
import com.mapr.ps.cloud.terraform.maprdeployui.service.MaprClusterService;
import com.mapr.ps.cloud.terraform.maprdeployui.service.SshKeyPairService;
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
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import org.wicketstuff.annotation.mount.MountPath;

import java.text.SimpleDateFormat;
import java.util.List;

@MountPath("/sshkeypairs")
public class KeyPairListPage extends BasePage {

    @SpringBean
    private SshKeyPairService sshKeyPairService;
    private final IModel<List<SshKeyPairDTO>> clustersModel;

    public KeyPairListPage() {
        clustersModel = new LoadableDetachableModel<List<SshKeyPairDTO>>() {
            @Override
            protected List<SshKeyPairDTO> load() {
                return sshKeyPairService.getSshKeyPairs();
            }
        };

        add(new BookmarkablePageLink<NewClusterPage>("newKeyPairLink", KeyPairEditPage.class));
        add(tableContainer());
    }

    private Component tableContainer() {
        ListView<SshKeyPairDTO> listView = new ListView<SshKeyPairDTO>("clusters", clustersModel) {
            @Override
            protected void populateItem(final ListItem<SshKeyPairDTO> item) {
                item.add(new Label("name", new PropertyModel<String>(item.getModel(), "name")));
                item.add(new Label("createdAt", new LoadableDetachableModel<String>() {
                    @Override
                    protected String load() {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        return sdf.format(item.getModelObject().getCreatedt());
                    }
                }));
                item.add(new Link<Void>("moreInfoLink") {
                    @Override
                    public void onClick() {
                        Model<String> of = Model.of(item.getModelObject().getId());
                        setResponsePage(new KeyPairEditPage(of) {
                            @Override
                            public boolean isReadOnly() {
                                return true;
                            }
                        });
                    }
                });
            }
        };

        WebMarkupContainer tableContainer = new WebMarkupContainer("tableContainer");
        tableContainer.add(listView);
        tableContainer.setOutputMarkupId(true);
        return tableContainer;
    }
}