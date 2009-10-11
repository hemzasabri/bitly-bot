package com.bitybot.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface BitlyBotServiceAsync {
	void shortenService(String message, AsyncCallback<String> callback);
}
