package com.mapr.ps.cloud.terraform.maprdeployui.web.pages;

import com.mapr.ps.cloud.terraform.maprdeployui.model.TemplateDTO;
import com.mapr.ps.cloud.terraform.maprdeployui.service.TemplateService;
import com.mapr.ps.cloud.terraform.maprdeployui.web.components.ClusterConfigurationPanel;
import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

@MountPath("/create_new_template")
@AuthorizeInstantiation("USER")
public class NewTemplatePage extends BasePage {
    @SpringBean
    private TemplateService templateService;
    private IModel<TemplateDTO> templateModel;

    public NewTemplatePage() {
        this(Model.of(new TemplateDTO()));

    }

    public NewTemplatePage(final IModel<TemplateDTO> templateModel) {
        this.templateModel = templateModel;
        add(deleteButton());
        add(new ClusterConfigurationPanel<TemplateDTO>("template", templateModel) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, IModel<TemplateDTO> model, FeedbackPanel feedback) {
                TemplateDTO template = model.getObject();
                if (feedback.anyErrorMessage()) {
                    target.add(feedback);
                    return;
                }
                templateService.save(template);
                info("Template saved");
                setResponsePage(TemplateListPage.class);
            }

            @Override
            protected String getSubmitButtonLabel() {
                return "Save!";
            }
        });
    }

    private Link<Void> deleteButton() {
        return new Link<Void>("deleteButton") {
            @Override
            public void onClick() {
                templateService.delete(templateModel.getObject().getTemplateId());
                info("Template was deleted.");
                setResponsePage(TemplateListPage.class);
            }

            @Override
            public boolean isVisible() {
                return StringUtils.isNotEmpty(templateModel.getObject().getTemplateId());
            }
        };
    }
}
