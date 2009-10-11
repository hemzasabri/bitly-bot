package com.bitybot.server.service;

import java.util.logging.Logger;

import com.bitybot.client.BitlyBotService;
import com.bitybot.server.util.Util;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class BitlyBotServiceImpl extends RemoteServiceServlet implements
		BitlyBotService {
	private static final Logger log = Logger
			.getLogger(BitlyBotServiceImpl.class.getName());

	public String shortenService(String message) {
		String replacementMessage = null;
		replacementMessage = Util.process(message);
		log.warning("\n By:Web" + "\n Message:" + message + "\n Replacement:"
				+ replacementMessage);
		return replacementMessage != null ? replacementMessage : message;
	}
}
