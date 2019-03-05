package com.mapr.ps.cloud.terraform.maprdeployui.web.pages;

import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.giffing.wicket.spring.boot.context.scan.WicketSignInPage;
import org.wicketstuff.annotation.mount.MountPath;

@WicketSignInPage
@MountPath("/login")
public class LoginPage extends BasePage {

	public LoginPage(PageParameters parameters) {
		super(parameters);
		if (((AbstractAuthenticatedWebSession) getSession()).isSignedIn()) {
			continueToOriginalDestination();
			setResponsePage( ClusterListPage.class );
		}
		add(new LoginForm("loginForm"));
	}

	private class LoginForm extends Form<LoginForm> {

		private String username;
		
		private String password;

		public LoginForm(String id) {
			super(id);
			setModel(new CompoundPropertyModel<>(this));
			add(getFeedback());
			add(new RequiredTextField<String>("username"));
			add(new PasswordTextField("password"));
		}

		private FeedbackPanel getFeedback() {
			FeedbackPanel feedback = new FeedbackPanel("feedback") {
				@Override
				public boolean isVisible() {
					return anyErrorMessage();
				}
			};
			return feedback;
		}

		@Override
		protected void onSubmit() {
			AuthenticatedWebSession session = AuthenticatedWebSession.get();
			if (session.signIn(username, password)) {
				setResponsePage(ClusterListPage.class);
			} else {
				error("Login failed");
			}
		}
	}
}