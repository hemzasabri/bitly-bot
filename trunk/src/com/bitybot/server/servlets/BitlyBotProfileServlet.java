package com.bitybot.server.servlets;

import com.google.wave.api.ProfileServlet;

@SuppressWarnings("serial")
public class BitlyBotProfileServlet extends ProfileServlet {

	@Override
	public String getRobotAvatarUrl() {
		return "http://bitly-bot.appspot.com/images/icon.png";
	}

	@Override
	public String getRobotName() {
		return "Bit.ly Bot";
	}

	@Override
	public String getRobotProfilePageUrl() {
		return "http://bitly-bot.appspot.com/";
	}
}