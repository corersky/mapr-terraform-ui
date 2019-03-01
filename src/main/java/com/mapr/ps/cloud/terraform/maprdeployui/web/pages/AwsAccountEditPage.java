package com.mapr.ps.cloud.terraform.maprdeployui.web.pages;


import com.mapr.ps.cloud.terraform.maprdeployui.model.AwsAccountDTO;
import com.mapr.ps.cloud.terraform.maprdeployui.model.ClusterConfigurationDTO;
import com.mapr.ps.cloud.terraform.maprdeployui.service.AwsAccountService;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

@MountPath("/awsaccountedit")
public class AwsAccountEditPage extends BasePage {
    @SpringBean
    private AwsAccountService awsAccountService;

    private final FeedbackPanel feedback;
    private final IModel<AwsAccountDTO> model;


    public AwsAccountEditPage() {
        this(Model.of());
    }

    public AwsAccountEditPage(IModel<String> model) {
        this.model = new LoadableDetachableModel<AwsAccountDTO>() {
            @Override
            protected AwsAccountDTO load() {
                String id = model.getObject();
                if(id == null) {
                    return new AwsAccountDTO();
                }
                AwsAccountDTO awsAccount = awsAccountService.getAwsAccountById(id);
                awsAccount.setAwsSecretAccessKey("Not displayed for security reasons.");
                return awsAccount;
            }
        };

        add(deleteButton());
        Form<ClusterConfigurationDTO> form = new Form<ClusterConfigurationDTO>("form") {
            @Override
            public boolean isEnabled() {
                return !isReadOnly();
            }
        };
        form.add(awsAccountName());
        form.add(awsSecretAccessKey());
        form.add(awsAccessKeyId());

        form.add(feedback = feedbackPanel());
        AjaxSubmitLink submitLink = new AjaxSubmitLink("saveButton") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                AwsAccountDTO object = AwsAccountEditPage.this.model.getObject();
                awsAccountService.save(object);
                info("AWS Account saved");
                setResponsePage(AwsAccountListPage.class);
            }

            @Override
            protected void onError(AjaxRequestTarget target) {
                target.add(feedback);
            }
        };
        form.add(submitLink);
        add(form);
    }


    private Button deleteButton() {
        return new Button("deleteButton") {
            @Override
            public void onSubmit() {
                awsAccountService.delete(AwsAccountEditPage.this.model.getObject().getId());
                info("AWS Account deleted.");
            }

            @Override
            public boolean isVisible() {
                return isReadOnly();
            }
        };
    }

    private RequiredTextField<String> awsAccessKeyId() {
        RequiredTextField<String> textField = new RequiredTextField<>("awsAccessKeyId", new PropertyModel<>(model, "awsAccessKeyId"));
        textField.setOutputMarkupId(true);
        return textField;
    }

    private PasswordTextField awsSecretAccessKey() {
        PasswordTextField textField = new PasswordTextField("awsSecretAccessKey", new PropertyModel<>(model, "awsSecretAccessKey"));
        textField.setRequired(true);
        textField.setOutputMarkupId(true);
        return textField;
    }

    private RequiredTextField<String> awsAccountName() {
        return new RequiredTextField<>("name", new PropertyModel<>(model, "name"));
    }

    private FeedbackPanel feedbackPanel() {
        FeedbackPanel feedback = new FeedbackPanel("feedback") {
            @Override
            public boolean isVisible() {
                return anyErrorMessage();
            }
        };
        feedback.setOutputMarkupId(true);
        feedback.setOutputMarkupPlaceholderTag(true);
        return feedback;
    }

    public boolean isReadOnly() {
        return false;
    }
}
