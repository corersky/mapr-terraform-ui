package com.mapr.ps.cloud.terraform.maprdeployui.web.pages;

import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class BasePage extends WebPage {

    public BasePage() {
        add(new BookmarkablePageLink<>("clusterListLink", ClusterListPage.class));
//        add(new BookmarkablePageLink<NewClusterPage>("newClusterLink", NewClusterPage.class));
    }
}