package com.bitybot.server.xmpp;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bitybot.server.util.Util;
import com.google.appengine.api.xmpp.JID;
import com.google.appengine.api.xmpp.Message;
import com.google.appengine.api.xmpp.MessageBuilder;
import com.google.appengine.api.xmpp.SendResponse;
import com.google.appengine.api.xmpp.XMPPService;
import com.google.appengine.api.xmpp.XMPPServiceFactory;

@SuppressWarnings("serial")
public class BitlyBotXmppServlet extends HttpServlet {
	private XMPPService xmppService;
	private static final Logger log = Logger
			.getLogger(BitlyBotXmppServlet.class.getName());

	@Override
	public void init() {
		log.warning("BitlyBotXmppServlet init");
		this.xmppService = XMPPServiceFactory.getXMPPService();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Message message = xmppService.parseMessage(req);
		JID fromId = message.getFromJid();
		String messageString = message.getBody();
		String shortenMessageString = Util.process(messageString);
		if (shortenMessageString != null
				&& !shortenMessageString.equals(messageString)) {
			SendResponse response = xmppService
					.sendMessage(new MessageBuilder().withBody(
							shortenMessageString).withRecipientJids(fromId)
							.build());
		}
		log.warning("\n By:XMPP" + "\n Message:" + messageString
				+ "\n Replacement:" + shortenMessageString);
	}
}
