package com.bitybot.server.api;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.bitybot.server.util.Util;

@SuppressWarnings("serial")
public class BitlyBotAPI extends HttpServlet {
	private static String MESSAGE = "message";
	private static String FORMAT = "format";
	private static String CALLBACK = "callback";
	private static String XML = "xml";
	private static String STATUS = "status";
	private static String ERROR = "error";
	private static String OK = "ok";
	private static final Logger log = Logger.getLogger(BitlyBotAPI.class
			.getName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Map<String, String[]> parameterMap = req.getParameterMap();
		String response = null;
		String message = null;
		if (parameterMap.containsKey(MESSAGE)
				&& parameterMap.get(MESSAGE)[0].length() > 0) {
			message = parameterMap.get(MESSAGE)[0];
			if (parameterMap.containsKey(FORMAT)) {
				String[] format = parameterMap.get(FORMAT);
				if (XML.equalsIgnoreCase(format[0])) {
					resp.setContentType("text/xml");
					response = getXML(message);
				} else {

					if (parameterMap.containsKey(CALLBACK))
						response = getJSON(message,
								parameterMap.get(CALLBACK)[0]);
					else {
						resp.setContentType("application/json");
						response = getJSON(message);
					}
				}
			} else {
				resp.setContentType("application/json");
				response = getJSON(message);
			}
		} else {
			response = "Invalid request";
		}
		resp.getOutputStream().print(response);
		resp.getOutputStream().flush();
		log.warning("\nBy:API" + "\nMessage:" + message + "\nResponse:"
				+ response);
	}

	private String getJSON(String message) {
		return getJSON(message, null);
	}

	private String getJSON(String message, String callback) {
		String shortenMessage = null;
		try {
			shortenMessage = Util.process(message);

			JSONObject jsonObject = new JSONObject();
			if (shortenMessage != null)
				jsonObject.put(MESSAGE, shortenMessage);
			else
				jsonObject.put(MESSAGE, message);

			jsonObject.put(STATUS, OK);
			shortenMessage = jsonObject.toString();

		} catch (JSONException e) {
			e.printStackTrace();
			shortenMessage = getErrorJson(message);
		}
		if (callback != null)
			shortenMessage = getCallbackJson(callback, shortenMessage);
		return shortenMessage;
	}

	private String getErrorJson(String message) {
		return "{ \"" + STATUS + "\": \"" + ERROR + "\", \"" + MESSAGE
				+ "\": \"" + message + "\"}";
	}

	private String getCallbackJson(String callback, String message) {
		return callback + "(" + message + ")";
	}

	private String getXML(String message) {
		StringBuilder response = new StringBuilder("<bitlybot>");
		String shortenMessage = message;

		shortenMessage = Util.process(message);
		response.append("<" + STATUS + ">");
		response.append(OK);
		response.append("</" + STATUS + ">");

		if (shortenMessage == null)
			shortenMessage = message;
		response.append("<" + MESSAGE + ">");
		response.append(shortenMessage);
		response.append("</" + MESSAGE + ">");
		response.append("</bitlybot>");
		return response.toString();
	}

}
