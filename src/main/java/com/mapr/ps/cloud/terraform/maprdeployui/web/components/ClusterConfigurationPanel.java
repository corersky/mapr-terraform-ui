package com.mapr.ps.cloud.terraform.maprdeployui.web.components;


import com.mapr.ps.cloud.terraform.maprdeployui.model.*;
import com.mapr.ps.cloud.terraform.maprdeployui.service.*;
import com.mapr.ps.cloud.terraform.maprdeployui.web.pages.MoreInfoPage;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class ClusterConfigurationPanel<T extends ClusterConfigurationDTO> extends Panel {
    @SpringBean
    private AwsInfoService awsInfoService;
    @SpringBean
    private ClusterLayoutsService clusterLayoutsService;
    @SpringBean
    private SshKeyPairService sshKeyPairService;
    @SpringBean
    private AwsAccountService awsAccountService;

    private final DropDownChoice<AwsInstanceDTO> awsInstanceTypeDropDownChoice;
    private final DropDownChoice<String> awsAvZoneDropDownChoice;
    private final WebMarkupContainer deploymentMatrixComponent;
    private final WebMarkupContainer deploymentMatrixHint;
    private final FeedbackPanel feedback;
    private final IModel<T> model;
    private final IModel<AwsRegionDTO> regionModel;
    private final IModel<AwsInstanceDTO> instanceModel;
    private final IModel<SshKeyPairFileRefDTO> sshKeyPairModel;
    private final IModel<DefaultClusterLayoutDTO> defaultClusterLayoutModel;
    private final IModel<List<NodeLayoutDTO>> nodeLayoutsModel;
    private final IModel<AwsAccountDTO> awsAccountModel;

    public ClusterConfigurationPanel(String id, IModel<T> model) {
        super(id, model);
        this.model = model;
        regionModel = new PropertyModel<>(model, "awsRegion");
        instanceModel = new PropertyModel<>(model, "awsInstanceType");
        sshKeyPairModel = new PropertyModel<>(model, "sshKeyPairFileRef");
        awsAccountModel = new PropertyModel<>(model, "awsAccount");
        defaultClusterLayoutModel = new PropertyModel<>(model, "defaultClusterLayout");
        nodeLayoutsModel = new PropertyModel<>(model, "nodesLayout");
        Form<ClusterConfigurationDTO> form = new Form<ClusterConfigurationDTO>("form") {

            @Override
            public boolean isEnabled() {
                return !ClusterConfigurationPanel.this.isReadOnly();

            }
        };
        form.add(customerName());
        form.add(envPrefix());
        form.add(clusterName());
        form.add(privateDomain());
        form.add(awsAvZoneDropDownChoice = awsAvZoneDropDownChoice());
        form.add(awsInstanceTypeDropDownChoice = awsInstanceTypeDropDownChoice());
        form.add(deploymentMatrixComponent = deploymentMatrixComponent());
        form.add(deploymentMatrixHint = deploymentMatrixHint());
        form.add(awsRegionDropDownChoice());
        form.add(sshKeyPairDropDownChoice());
        form.add(awsAccountsDropDownChoice());
        form.add(numberNodesDropDownChoice());
        form.add(extensionDsr());
        form.add(feedback = feedbackPanel());
        AjaxSubmitLink submitLink = new AjaxSubmitLink("submitButton") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                ClusterConfigurationPanel.this.onSubmit(target, model, feedback);
            }

            @Override
            protected void onError(AjaxRequestTarget target) {
                target.add(feedback);
            }

            @Override
            public boolean isVisible() {
                return !ClusterConfigurationPanel.this.isReadOnly();
            }
        };
        submitLink.add(new Label("submitButtonLabel", this::getSubmitButtonLabel));
        form.add(submitLink);
        add(form);
    }

    protected abstract String getSubmitButtonLabel();
    protected abstract void onSubmit(AjaxRequestTarget target, IModel<T> model, FeedbackPanel feedback);

    private CheckBox extensionDsr() {
        return new CheckBox("extensionDsr", new PropertyModel<>(model, "extensionDsr"));
    }

    private RequiredTextField<String> privateDomain() {
        return new RequiredTextField<String>("privateDomain", new PropertyModel<>(model, "privateDomain"));
    }

    private RequiredTextField<String> clusterName() {
        return new RequiredTextField<String>("clusterName", new PropertyModel<>(model, "clusterName"));
    }

    private RequiredTextField<String> envPrefix() {
        return new RequiredTextField<String>("envPrefix", new PropertyModel<>(model, "envPrefix"));
    }

    private RequiredTextField<String> customerName() {
        return new RequiredTextField<>("customerName", new PropertyModel<>(model, "customerName"));
    }

    private WebMarkupContainer deploymentMatrixHint() {
        WebMarkupContainer webMarkupContainer = new WebMarkupContainer("deploymentMatrixHint") {
            @Override
            public boolean isVisible() {
                return defaultClusterLayoutModel.getObject() == null;
            }
        };
        webMarkupContainer.setOutputMarkupId(true);
        webMarkupContainer.setOutputMarkupPlaceholderTag(true);
        return webMarkupContainer;
    }


    private FeedbackPanel feedbackPanel() {
        FeedbackPanel feedback = new FeedbackPanel("feedback") {
            @Override
            public boolean isVisible() {
                return anyErrorMessage() && !ClusterConfigurationPanel.this.isReadOnly();
            }
        };
        feedback.setOutputMarkupId(true);
        feedback.setOutputMarkupPlaceholderTag(true);
        return feedback;
    }

    private WebMarkupContainer deploymentMatrixComponent() {
        WebMarkupContainer component = new WebMarkupContainer("deploymentMatrix") {
            @Override
            public boolean isVisible() {
                return defaultClusterLayoutModel.getObject() != null;
            }
        };
        component.setOutputMarkupPlaceholderTag(true);
        component.add(new ListView<NodeLayoutDTO>("nodesLayout", nodeLayoutsModel) {
            @Override
            protected void populateItem(ListItem<NodeLayoutDTO> item) {
                item.add(new Label("nodeIndex", new PropertyModel<Integer>(item.getModel(), "nodeIndex")));
                item.add(new CheckBox("zookeeper", new PropertyModel<>(item.getModel(), "zookeeper")));
                item.add(new CheckBox("cldb", new PropertyModel<>(item.getModel(), "cldb")));
                item.add(new CheckBox("fileserver", new PropertyModel<>(item.getModel(), "fileserver")));
                item.add(new CheckBox("kafkaClient", new PropertyModel<>(item.getModel(), "kafkaClient")));
                item.add(new CheckBox("kafkaKsql", new PropertyModel<>(item.getModel(), "kafkaKsql")));
                item.add(new CheckBox("mcs", new PropertyModel<>(item.getModel(), "mcs")));
                item.add(new CheckBox("resourceManager", new PropertyModel<>(item.getModel(), "resourceManager")));
                item.add(new CheckBox("nodeManager", new PropertyModel<>(item.getModel(), "nodeManager")));
                item.add(new CheckBox("historyServer", new PropertyModel<>(item.getModel(), "historyServer")));
                item.add(new CheckBox("mySQL", new PropertyModel<>(item.getModel(), "mySQL")));
                item.add(new CheckBox("spark", new PropertyModel<>(item.getModel(), "spark")));
                item.add(new CheckBox("sparkHistoryServer", new PropertyModel<>(item.getModel(), "sparkHistoryServer")));
                item.add(new CheckBox("nfs", new PropertyModel<>(item.getModel(), "nfs")));
                item.add(new CheckBox("drill", new PropertyModel<>(item.getModel(), "drill")));
                item.add(new CheckBox("flume", new PropertyModel<>(item.getModel(), "flume")));
                item.add(new CheckBox("hbaseCli", new PropertyModel<>(item.getModel(), "hbaseCli")));
                item.add(new CheckBox("hiveCli", new PropertyModel<>(item.getModel(), "hiveCli")));
                item.add(new CheckBox("hiveMetaStore", new PropertyModel<>(item.getModel(), "hiveMetaStore")));
                item.add(new CheckBox("hiveServer2", new PropertyModel<>(item.getModel(), "hiveServer2")));
                item.add(new CheckBox("collectd", new PropertyModel<>(item.getModel(), "collectd")));
                item.add(new CheckBox("openTSDB", new PropertyModel<>(item.getModel(), "openTSDB")));
                item.add(new CheckBox("grafana", new PropertyModel<>(item.getModel(), "grafana")));
            }
        });

        return component;
    }

    private DropDownChoice<DefaultClusterLayoutDTO> numberNodesDropDownChoice() {
        DropDownChoice<DefaultClusterLayoutDTO> dropdown = new DropDownChoice<>("numberNodes", defaultClusterLayoutModel, new LoadableDetachableModel<List<? extends DefaultClusterLayoutDTO>>() {
            @Override
            protected List<? extends DefaultClusterLayoutDTO> load() {
                return clusterLayoutsService.getPredefindedClusterLayouts();
            }
        }, new ChoiceRenderer<>("numberNodes"));
        dropdown.add(new AjaxFormComponentUpdatingBehavior("change") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                nodeLayoutsModel.setObject(clusterLayoutsService.createNodeLayoutList(defaultClusterLayoutModel.getObject()));
                target.add(deploymentMatrixHint);
                target.add(deploymentMatrixComponent);
            }
        });
        dropdown.setRequired(true);
        return dropdown;
    }

    private DropDownChoice<AwsInstanceDTO> awsInstanceTypeDropDownChoice() {
        DropDownChoice<AwsInstanceDTO> awsInstanceType = new DropDownChoice<>("awsInstanceType", instanceModel, new LoadableDetachableModel<List<? extends AwsInstanceDTO>>() {
            @Override
            protected List<? extends AwsInstanceDTO> load() {
                AwsRegionDTO region = regionModel.getObject();
                String code = region != null ? region.getCode() : "";
                return awsInfoService.getAwsAllowedInstances().stream().filter(instance -> instance.getAvailableRegion().contains(code)).collect(Collectors.toList());
            }
        }, new IChoiceRenderer<AwsInstanceDTO>() {
            @Override
            public Object getDisplayValue(AwsInstanceDTO object) {
                return object.getInstanceCode() + " (" + object.getvCPU() + " vCPUs - " + object.getMemoryInGB() + " GB RAM - " + object.getStorage() + " - Network " + object.getNetwork() + ")";
            }

            @Override
            public String getIdValue(AwsInstanceDTO object, int index) {
                return object.getInstanceCode();
            }

            @Override
            public AwsInstanceDTO getObject(String id, IModel<? extends List<? extends AwsInstanceDTO>> choices) {
                return choices.getObject().stream().filter(awsInstance -> awsInstance.getInstanceCode().equals(id)).findFirst().orElseGet(null);
            }
        });
        awsInstanceType.setRequired(true);
        awsInstanceType.setOutputMarkupId(true);
        return awsInstanceType;
    }

    private DropDownChoice<SshKeyPairFileRefDTO> sshKeyPairDropDownChoice() {
        DropDownChoice<SshKeyPairFileRefDTO> sshKeyPair = new DropDownChoice<>("sshKeyPair", sshKeyPairModel, new LoadableDetachableModel<List<SshKeyPairFileRefDTO>>() {
            @Override
            protected List<SshKeyPairFileRefDTO> load() {
                return sshKeyPairService.getSshKeyPairsFileRef();
            }
        }, new ChoiceRenderer<>("name", "id"));
        sshKeyPair.setRequired(true);
        sshKeyPair.setOutputMarkupId(true);
        return sshKeyPair;
    }

    private DropDownChoice<AwsAccountDTO> awsAccountsDropDownChoice() {
        DropDownChoice<AwsAccountDTO> sshKeyPair = new DropDownChoice<AwsAccountDTO>("awsAccount", awsAccountModel, new LoadableDetachableModel<List<AwsAccountDTO>>() {
            @Override
            protected List<AwsAccountDTO> load() {
                return awsAccountService.getAwsAccounts();
            }
        }, new ChoiceRenderer<>("name", "id"));
        sshKeyPair.setRequired(true);
        sshKeyPair.setOutputMarkupId(true);
        return sshKeyPair;
    }




    private DropDownChoice<String> awsAvZoneDropDownChoice() {
        DropDownChoice<String> awsAvZone = new DropDownChoice<>("awsAvZone", new PropertyModel<>(model, "awsAvZone"), new LoadableDetachableModel<List<String>>() {
            @Override
            protected List<String> load() {
                AwsRegionDTO object = regionModel.getObject();
                return object != null ? object.getZones() : Collections.emptyList();
            }
        });
        awsAvZone.setRequired(true);
        awsAvZone.setOutputMarkupId(true);
        return awsAvZone;
    }

    private DropDownChoice<AwsRegionDTO> awsRegionDropDownChoice() {
        DropDownChoice<AwsRegionDTO> awsRegion = new DropDownChoice<>("awsRegion", regionModel, new LoadableDetachableModel<List<? extends AwsRegionDTO>>() {
            @Override
            protected List<? extends AwsRegionDTO> load() {
                return awsInfoService.getAwsAllowedRegions();
            }
        }, new IChoiceRenderer<AwsRegionDTO>() {
            @Override
            public Object getDisplayValue(AwsRegionDTO object) {
                return object.getDescription() + " - " + object.getCode();
            }

            @Override
            public String getIdValue(AwsRegionDTO object, int index) {
                return object.getCode();
            }

            @Override
            public AwsRegionDTO getObject(String id, IModel<? extends List<? extends AwsRegionDTO>> choices) {
                return choices.getObject().stream().filter(region -> ((AwsRegionDTO) region).getCode().equals(id)).findFirst().orElseGet(null);
            }
        });
        awsRegion.add(new AjaxFormComponentUpdatingBehavior("change") {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                target.add(awsAvZoneDropDownChoice);
                target.add(awsInstanceTypeDropDownChoice);
            }
        });
        awsRegion.setRequired(true);
        return awsRegion;
    }

    public boolean isReadOnly() {
        return false;
    }
}
