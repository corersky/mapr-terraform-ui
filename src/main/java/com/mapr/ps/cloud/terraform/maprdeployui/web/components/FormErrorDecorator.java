package com.mapr.ps.cloud.terraform.maprdeployui.web.components;

import org.apache.wicket.Component;
import org.apache.wicket.MetaDataKey;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.application.IComponentOnBeforeRenderListener;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.FormComponentLabel;
import org.apache.wicket.markup.html.form.LabeledWebMarkupContainer;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

import java.io.Serializable;
import java.util.Map;

/**
 * Adds CSS classes to form inputs and their labels before they're rendered.
 */
public class FormErrorDecorator implements IComponentOnBeforeRenderListener, AjaxRequestTarget.IListener {
    private static final Behavior COMPONENT_ERROR = new CssClassAppender("invalidform");
    private static final Behavior LABEL_ERROR = new CssClassAppender("invalidform");
     
    private static final MetaDataKey<Serializable> RENDERED_WITH_ERROR = new MetaDataKey<Serializable>() { };
 
    /**
     * Force all FormComponents and FormComponentLabels to output a markupId. If the associated form component
     * is invalid, add a temporary behavior to append a CSS error class.
     */
    @Override
    public void onBeforeRender(Component component) {
        if (component instanceof FormComponent || component instanceof FormComponentLabel) {
            component.setOutputMarkupId(true);
             
            if (isInvalidFormComponent(component)) {
                component.add(COMPONENT_ERROR);
                component.setMetaData(RENDERED_WITH_ERROR, Boolean.TRUE);
            }
            else if (isInvalidFormComponentLabel(component)) {
                component.add(LABEL_ERROR);
                component.setMetaData(RENDERED_WITH_ERROR, Boolean.TRUE);
            }
        }
    }
     
    /**
     * Before responding to AJAX requests, make sure that all form components and their labels that
     * need to be rendered get added to the AjaxRequestTarget.
     */
    @Override
    public void onBeforeRespond(Map<String, Component> map, AjaxRequestTarget target) {
        target.getPage().visitChildren(new AjaxRenderingVisitor(target));
    }

    @Override
    public void onAfterRespond(Map<String, Component> map, AjaxRequestTarget.IJavaScriptResponse response) {

    }

    private static boolean isInvalidFormComponentLabel(Component component) {
        if (component instanceof FormComponentLabel) {
            LabeledWebMarkupContainer labeled = ((FormComponentLabel) component).getFormComponent();
            return isInvalidFormComponent(labeled);
        }
        return false;
    }
     
    private static boolean isInvalidFormComponent(Component component) {
        if (component instanceof FormComponent) {
            FormComponent<?> formComponent = (FormComponent<?>) component;
            return !formComponent.isValid();
        }
        return false;
    }
     
    /**
     * Adds components to an AjaxRequestTarget that are invalid, or that are valid after being invalid
     * last request. This class does NOT add the error styling.
     */
    private static class AjaxRenderingVisitor implements IVisitor<Component, Boolean> {
        private final AjaxRequestTarget target;
         
        public AjaxRenderingVisitor(AjaxRequestTarget target) {
            this.target = target;
        }

        @Override
        public void component(Component component, IVisit<Boolean> visit) {
            if (isInvalidFormComponent(component) || isInvalidFormComponentLabel(component)) {
                target.add(component);
            }
            else if (component.getMetaData(RENDERED_WITH_ERROR) != null) {
                component.setMetaData(RENDERED_WITH_ERROR, null);
                target.add(component);
            }
        }
    }
     
    /**
     * Temporary behavior that appends a CSS class to the component tag.
     */
    private static class CssClassAppender extends Behavior {
        private final String cssClass;
         
        public CssClassAppender(String cssClass) {
            this.cssClass = cssClass;
        }
         
        @Override
        public void onComponentTag(Component component, ComponentTag tag) {
            tag.append("class", cssClass, " ");
        }

        @Override
        public boolean isTemporary(Component component) {
            return true;
        }
    }
}