package com.mapr.ps.cloud.terraform.maprdeployui.web.pages;

import com.mapr.ps.cloud.terraform.maprdeployui.model.TemplateDTO;
import com.mapr.ps.cloud.terraform.maprdeployui.service.TemplateService;
import org.apache.wicket.Component;
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

import java.util.List;

@MountPath("/templates")
public class TemplateListPage extends BasePage {

    @SpringBean
    private TemplateService templateService;
    private final IModel<List<TemplateDTO>> templatesModel;

    public TemplateListPage() {
        templatesModel = new LoadableDetachableModel<List<TemplateDTO>>() {
            @Override
            protected List<TemplateDTO> load() {
                return templateService.getTemplates();
            }
        };

        add(new BookmarkablePageLink<NewClusterPage>("newTemplateTableLink", NewTemplatePage.class));
        add(tableContainer());
    }

    private Component tableContainer() {
        ListView<TemplateDTO> listView = new ListView<TemplateDTO>("templates", templatesModel) {
            @Override
            protected void populateItem(final ListItem<TemplateDTO> item) {
                item.add(new Label("clusterName", new PropertyModel<String>(item.getModel(), "clusterName")));
                item.add(new Label("privateDomain", new PropertyModel<String>(item.getModel(), "privateDomain")));
                item.add(new Label("customerName", new PropertyModel<String>(item.getModel(), "customerName")));
                item.add(new Label("envPrefix", new PropertyModel<String>(item.getModel(), "envPrefix")));
                item.add(new Link<Void>("editLink") {
                    @Override
                    public void onClick() {
                        setResponsePage(new NewTemplatePage(Model.of(templateService.getTemplateById(item.getModelObject().getTemplateId()))));
                    }
                });
                item.add(new Link<Void>("newClusterLink") {
                    @Override
                    public void onClick() {
                        setResponsePage(new NewClusterPage(Model.of(templateService.mapClusterConfiguration(item.getModelObject()))));
                    }
                });
            }
        };
        return listView;
    }
}