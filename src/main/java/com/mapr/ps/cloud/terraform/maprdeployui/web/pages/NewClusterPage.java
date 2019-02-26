package com.mapr.ps.cloud.terraform.maprdeployui.web.pages;

import com.mapr.ps.cloud.terraform.maprdeployui.model.*;
import com.mapr.ps.cloud.terraform.maprdeployui.service.AwsInfoService;
import com.mapr.ps.cloud.terraform.maprdeployui.service.ClusterLayoutsService;
import com.mapr.ps.cloud.terraform.maprdeployui.web.components.ClusterConfigurationPanel;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListItemModel;
import org.apache.wicket.markup.html.list.ListView;
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
public class NewClusterPage extends BasePage {


    public NewClusterPage(PageParameters parameters) {
        super(parameters);
        add(new ClusterConfigurationPanel("clusterConfiguration", Model.of(new ClusterConfigurationDTO())));
    }

}
