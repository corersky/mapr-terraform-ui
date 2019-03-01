package com.mapr.ps.cloud.terraform.maprdeployui.web.pages;


import com.mapr.ps.cloud.terraform.maprdeployui.model.ClusterConfigurationDTO;
import com.mapr.ps.cloud.terraform.maprdeployui.model.SshKeyPairDTO;
import com.mapr.ps.cloud.terraform.maprdeployui.service.SshKeyPairService;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

@MountPath("/sshkeypairedit")
public class KeyPairEditPage extends BasePage {
    @SpringBean
    private SshKeyPairService sshKeyPairService;

    private final FeedbackPanel feedback;
    private final IModel<SshKeyPairDTO> model;


    public KeyPairEditPage() {
        this(Model.of());
    }

    public KeyPairEditPage(IModel<String> model) {
        this.model = new LoadableDetachableModel<SshKeyPairDTO>() {
            @Override
            protected SshKeyPairDTO load() {
                String id = model.getObject();
                if(id == null) {
                    return new SshKeyPairDTO();
                }
                SshKeyPairDTO sshKeyPair = sshKeyPairService.getSshKeyPairById(id);
                sshKeyPair.setPrivateKey("Not displayed for security reasons.");
                return sshKeyPair;
            }
        };

        add(deleteButton(model));
        Form<ClusterConfigurationDTO> form = new Form<ClusterConfigurationDTO>("form") {
            @Override
            public boolean isEnabled() {
                return !isReadOnly();
            }
        };
        form.add(keyPairName());
        form.add(sshPrivateKey());
        form.add(sshPublicKey());

        form.add(feedback = feedbackPanel());
        AjaxSubmitLink submitLink = new AjaxSubmitLink("saveButton") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                SshKeyPairDTO object = KeyPairEditPage.this.model.getObject();
                sshKeyPairService.save(object);
                info("SSH key pair saved");
                setResponsePage(KeyPairListPage.class);
            }

            @Override
            protected void onError(AjaxRequestTarget target) {
                target.add(feedback);
            }
        };
        form.add(submitLink);
        add(form);
    }

    private Button deleteButton(IModel<String> model) {
        return new Button("deleteButton") {
            @Override
            public void onSubmit() {
                sshKeyPairService.delete(model.getObject());
                info("SSH key pair deleted.");
            }

            @Override
            public boolean isVisible() {
                return !isReadOnly();
            }
        };
    }

    private TextArea<String> sshPublicKey() {
        TextArea<String> sshPublicKey = new TextArea<>("sshPublicKey", new PropertyModel<>(model, "publicKey"));
        sshPublicKey.setRequired(true);
        return sshPublicKey;
    }

    private TextArea<String> sshPrivateKey() {
        TextArea<String> sshPrivateKey = new TextArea<>("sshPrivateKey", new PropertyModel<>(model, "privateKey"));
        sshPrivateKey.setRequired(true);
        return sshPrivateKey;
    }

    private RequiredTextField<String> keyPairName() {
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
