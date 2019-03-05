package com.mapr.ps.cloud.terraform.maprdeployui.web.pages;

import com.mapr.ps.cloud.terraform.maprdeployui.model.SshKeyPairDTO;
import com.mapr.ps.cloud.terraform.maprdeployui.model.SshKeyPairFileRefDTO;
import com.mapr.ps.cloud.terraform.maprdeployui.service.SshKeyPairService;
import org.apache.wicket.Component;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
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
import org.wicketstuff.annotation.mount.MountPath;

import java.text.SimpleDateFormat;
import java.util.List;

@MountPath("/sshkeypairs")
@AuthorizeInstantiation("USER")
public class KeyPairListPage extends BasePage {

    @SpringBean
    private SshKeyPairService sshKeyPairService;
    private final IModel<List<SshKeyPairFileRefDTO>> keyPairsModel;

    public KeyPairListPage() {
        keyPairsModel = new LoadableDetachableModel<List<SshKeyPairFileRefDTO>>() {
            @Override
            protected List<SshKeyPairFileRefDTO> load() {
                return sshKeyPairService.getSshKeyPairsFileRef();
            }
        };

        add(new BookmarkablePageLink<NewClusterPage>("newKeyPairLink", KeyPairEditPage.class));
        add(tableContainer());
    }

    private Component tableContainer() {
        ListView<SshKeyPairFileRefDTO> listView = new ListView<SshKeyPairFileRefDTO>("keypairs", keyPairsModel) {
            @Override
            protected void populateItem(final ListItem<SshKeyPairFileRefDTO> item) {
                item.add(new Label("name", new PropertyModel<String>(item.getModel(), "name")));
                item.add(new Label("createdAt", new LoadableDetachableModel<String>() {
                    @Override
                    protected String load() {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        return sdf.format(item.getModelObject().getCreatedAt());
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