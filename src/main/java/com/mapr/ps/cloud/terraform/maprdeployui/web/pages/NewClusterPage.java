package com.mapr.ps.cloud.terraform.maprdeployui.web.pages;

import com.mapr.ps.cloud.terraform.maprdeployui.model.*;
import com.mapr.ps.cloud.terraform.maprdeployui.service.AwsInfoService;
import com.mapr.ps.cloud.terraform.maprdeployui.service.ClusterLayoutsService;
import com.mapr.ps.cloud.terraform.maprdeployui.service.MaprClusterService;
import com.mapr.ps.cloud.terraform.maprdeployui.web.components.ClusterConfigurationPanel;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListItemModel;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@MountPath("/create_new_cluster")
@AuthorizeInstantiation("USER")
public class NewClusterPage extends BasePage {

    @SpringBean
    private MaprClusterService maprClusterService;


    public NewClusterPage() {
        this(Model.of(new ClusterConfigurationDTO()));
    }

    public NewClusterPage(IModel<ClusterConfigurationDTO> clusterConfigurationModel) {
        add(new ClusterConfigurationPanel<ClusterConfigurationDTO>("clusterConfiguration", clusterConfigurationModel) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, IModel<ClusterConfigurationDTO> model, FeedbackPanel feedback) {
                ClusterConfigurationDTO clusterConfig = model.getObject();
                advancedValidation(clusterConfig);
                if (feedback.anyErrorMessage()) {
                    target.add(feedback);
                    return;
                }
                maprClusterService.deployCluster(clusterConfig);
                info("Cluster deployment started");
                setResponsePage(new MoreInfoPage(new PropertyModel<>(model, "envPrefix")));
            }

            @Override
            protected String getSubmitButtonLabel() {
                return "Deploy!";
            }

            private void advancedValidation(ClusterConfigurationDTO clusterConfig) {
                if (maprClusterService.isPrefixUsed(clusterConfig)) {
                    error("Prefix is already in use. If Cluster was destroyed, delete the configuration.");
                }
                if (clusterConfig.getNodesLayout().stream().filter(NodeLayoutDTO::isMySQL).count() != 1) {
                    error("Please install exactly one MySQL.");
                }
                if (clusterConfig.getNodesLayout().stream().filter(NodeLayoutDTO::isHistoryServer).count() > 1) {
                    error("Please choose only one HistoryServer.");
                }
                if (clusterConfig.getNodesLayout().stream().filter(NodeLayoutDTO::isZookeeper).count() % 2 == 0) {
                    error("Please select an odd number of nodes for Zookeeper.");
                }
            }
        });
    }
}
