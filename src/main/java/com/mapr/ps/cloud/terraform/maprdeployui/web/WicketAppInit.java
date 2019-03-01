package com.mapr.ps.cloud.terraform.maprdeployui.web;

import com.giffing.wicket.spring.boot.context.extensions.ApplicationInitExtension;
import com.giffing.wicket.spring.boot.context.extensions.WicketApplicationInitConfiguration;
import com.mapr.ps.cloud.terraform.maprdeployui.web.components.FormErrorDecorator;
import org.apache.wicket.protocol.http.WebApplication;

@ApplicationInitExtension
public class WicketAppInit implements WicketApplicationInitConfiguration {

    @Override
    public void init(WebApplication webApplication) {
        FormErrorDecorator error = new FormErrorDecorator();
        webApplication.getAjaxRequestTargetListeners().add(error);
        webApplication.getComponentPreOnBeforeRenderListeners().add(error);
    }
}