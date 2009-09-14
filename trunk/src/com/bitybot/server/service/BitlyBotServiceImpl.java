package com.bitybot.server.service;

import java.io.IOException;
import java.util.logging.Logger;

import org.json.JSONException;

import com.bitybot.client.BitlyBotService;
import com.bitybot.server.util.Util;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class BitlyBotServiceImpl extends RemoteServiceServlet implements
		BitlyBotService {
	private static final Logger log = Logger.getLogger(BitlyBotServiceImpl.class
			.getName());
	public String shortenService(String message) {
		String replacementMessage=null;
		try {
			replacementMessage=Util.process(message);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		log.warning("\nBy:Web"
				+ "\nMessage:" + message
				+ "\nReplacement:" + replacementMessage);
		return replacementMessage!=null?replacementMessage:message;
	}
}
