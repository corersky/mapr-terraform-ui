package com.mapr.ps.cloud.terraform.maprdeployui.web.pages;

import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class BasePage extends WebPage {

    public BasePage() {
        this(new PageParameters());
    }

    public BasePage(PageParameters params) {
        add(new BookmarkablePageLink<>("clusterListLink", ClusterListPage.class));
        add(new BookmarkablePageLink<>("sshKeyPairListLink", KeyPairListPage.class));
        add(new BookmarkablePageLink<>("awsAccountsListLink", AwsAccountListPage.class));
        add(new BookmarkablePageLink<>("templatesListLink", TemplateListPage.class));
        add(new ExternalLink("logoutLink", Model.of("/logout")) {
            @Override
            public boolean isVisible() {
                return ((AbstractAuthenticatedWebSession) getSession()).isSignedIn();
            }
        });
    }
}