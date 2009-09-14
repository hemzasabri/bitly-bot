package com.bitybot.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class BityBot implements EntryPoint {
	private final BitlyBotServiceAsync bitlyBotService = GWT
			.create(BitlyBotService.class);

	public void onModuleLoad() {
		HorizontalPanel top = new HorizontalPanel();
		HorizontalPanel topPanel = new HorizontalPanel();
		VerticalPanel container = new VerticalPanel();
		HorizontalPanel bottomPanel = new HorizontalPanel();

		final Label messageLabel = new Label("Your Message:");
		final Label status = new Label("");
		final Label counter = new Label("0");
		final TextArea messageField = new TextArea();
		final Button tweetThis = new Button("Tweet This");
		final Button sendButton = new Button("Shorten");

		sendButton.addStyleName("sendButton");
		tweetThis.addStyleName("sendButton");
		counter.addStyleName("counter");
		messageLabel.addStyleName("message");
		messageField.setHeight("100px");
		status.setVisible(false);

		container.setSpacing(10);
		container.setWidth("100%");
		topPanel.setWidth("100%");
		bottomPanel.setWidth("100%");
		top.setSpacing(5);

		top.add(messageLabel);
		top.add(status);
		topPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		topPanel.add(top);
		topPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		topPanel.add(counter);

		container.add(topPanel);
		container.add(messageField);

		bottomPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		bottomPanel.add(tweetThis);
		bottomPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		bottomPanel.add(sendButton);

		container.add(bottomPanel);

		RootPanel.get("Container").add(container);

		class CustomeHandler implements ClickHandler, KeyUpHandler,
				FocusHandler, ChangeHandler {
			public void onClick(ClickEvent event) {
				if (event.getSource().equals(tweetThis))
					tweetIt();
				else {
					sendNameToServer();
					status.setText("Working...");
					status.setVisible(true);
					status.removeStyleName("doneStatus");
					status.removeStyleName("errorStatus");
					status.addStyleName("workingStatus");
				}
			}

			public void onKeyUp(KeyUpEvent event) {
				counter.setText(messageField.getText().length() + "");
				/*
				 * if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
				 * sendNameToServer(); }
				 */
			}

			private void sendNameToServer() {
				sendButton.setEnabled(false);
				String messageToServer = messageField.getText();
				bitlyBotService.shortenService(messageToServer,
						new AsyncCallback<String>() {
							public void onFailure(Throwable caught) {
								status.setText("Some Server Error.");
								status.setVisible(true);
								status.removeStyleName("workingStatus");
								status.removeStyleName("doneStatus");
								status.addStyleName("errorStatus");
								sendButton.setEnabled(true);
							}

							public void onSuccess(String result) {
								messageField.setText(result);
								messageField.selectAll();
								sendButton.setEnabled(true);
								counter.setText(messageField.getText().length()
										+ "");
								status.setText("Done");
								status.setVisible(true);
								status.removeStyleName("workingStatus");
								status.removeStyleName("errorStatus");
								status.addStyleName("doneStatus");
							}
						});
			}

			private void tweetIt() {
				if (messageField.getText().length() > 0
						&& messageField.getText().length() <= 140)
					Window.open("http://twitter.com/home/?status="
							+ messageField.getText(), "_blank", "");
				else {
					if (messageField.getText().length() <= 0)
						status.setText("No Message");
					else
						status.setText("Message more than 140 characters.");
					status.setVisible(true);
					status.removeStyleName("workingStatus");
					status.removeStyleName("doneStatus");
					status.addStyleName("errorStatus");
				}
			}

			public void onFocus(FocusEvent event) {
				counter.setText(messageField.getText().length() + "");
			}

			public void onChange(ChangeEvent event) {
				counter.setText(messageField.getText().length() + "");
			}
		}

		// Add a handler to send the name to the server
		messageField.setFocus(true);
		CustomeHandler handler = new CustomeHandler();
		sendButton.addClickHandler(handler);
		tweetThis.addClickHandler(handler);
		messageField.addKeyUpHandler(handler);
		messageField.addChangeHandler(handler);
		messageField.addFocusHandler(handler);
	}
}
