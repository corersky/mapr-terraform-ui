package com.mapr.ps.cloud.terraform.maprdeployui.web.pages;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;

public class BasePage extends WebPage {

    public BasePage() {
        add(new BookmarkablePageLink<>("clusterListLink", ClusterListPage.class));
        add(new BookmarkablePageLink<>("sshKeyPairListLink", KeyPairListPage.class));
        add(new BookmarkablePageLink<>("awsAccountsListLink", AwsAccountListPage.class));
//        add(new BookmarkablePageLink<NewClusterPage>("newClusterLink", NewClusterPage.class));
    }
}