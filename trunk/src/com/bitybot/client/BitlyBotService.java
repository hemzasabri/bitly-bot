package com.bitybot.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("BitlyBotService")
public interface BitlyBotService extends RemoteService {
	String shortenService(String message);
}
