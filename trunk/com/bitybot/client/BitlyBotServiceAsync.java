package com.bitybot.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>BitlyBotService</code>.
 */
public interface BitlyBotServiceAsync {
	void shortenService(String message, AsyncCallback<String> callback);
}
