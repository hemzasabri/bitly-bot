package com.bitybot.server.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("serial")
public class Util {
	private static final Logger log = Logger.getLogger(Util.class.getName());
	private static String URLregex = "((https?|ftp|gopher|telnet|file|notes|ms-help):((//)|(\\\\\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
	private static String[][] loginKeyPairs = {
			{ "rahulgarg", "R_2154f1c6ea071f5aaab9cfb73f629e22" },
			{ "bitlybot", "R_d618b34d2d3944dc76e1c4e88b09fd08" },
			{ "bitlybot2", "R_788978474267b52019da5cceba23b349" } };

	public static String process(String message) {
		StringBuffer sb = new StringBuffer();
		Pattern pattern = Pattern.compile(URLregex, Pattern.DOTALL);
		Matcher matcher = pattern.matcher(message);
		while (matcher.find()) {
			String longurl = matcher.group(1);
			String bitlyurl = longurl;
			boolean isBitlyURL = longurl.matches("\\bhttp://bit.ly/\\S*");
			if (!isBitlyURL) {
				bitlyurl = getBitlyURL(longurl);
				if (bitlyurl == null) {
					log.severe("bitlyurl is null for longurl :" + longurl);
					bitlyurl = longurl;
				}
			}
			matcher.appendReplacement(sb, bitlyurl);
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	private static String getBitlyURL(String longurl) {
		String bitlyurl = longurl;
		String RESTurl = null;
		JSONObject json = null;
		try {
			int id = 0;
			while (id < 3) {
				RESTurl = "http://api.bit.ly/shorten?version=2.0.1" + "&login="
						+ loginKeyPairs[id][0] + "&apiKey="
						+ loginKeyPairs[id][1] + "&longUrl="
						+ URLEncoder.encode(longurl, "UTF-8");
				json = new JSONObject(fetchUrl(RESTurl));
				if ("OK".equals(json.getString("statusCode"))) {
					JSONObject output = json.getJSONObject("results")
							.getJSONObject(longurl);
					if (output.has("shortUrl"))
						bitlyurl = output.getString("shortUrl");

					return bitlyurl;
				} else {
					++id;
				}
			}
		} catch (JSONException e) {
			log.severe("JSONException while getting getBitlyURL :"
					+ e.getMessage());
			if (json != null)
				log.severe("JSON:" + json.toString());
		} catch (UnsupportedEncodingException e) {
			log.severe("UnsupportedEncodingException getting getBitlyURL :"
					+ e.getMessage());
		} catch (IOException e) {
			log.severe("IOException getting getBitlyURL :" + e.getMessage());
		}
		return bitlyurl;
	}

	private static String fetchUrl(String url) throws IOException {
		HttpURLConnection connection = (HttpURLConnection) new URL(url)
				.openConnection();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				connection.getInputStream()));
		StringBuilder result = new StringBuilder();
		try {
			String inputLine;
			while ((inputLine = reader.readLine()) != null) {
				result.append(inputLine);
			}
		} catch (IOException e) {
			log.severe("IOException while requesting URL :" + e.getMessage());
			throw e;
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				log.severe("IOException while closing reader :"
						+ e.getMessage());
				throw e;
			}
		}
		return result.toString();
	}
}
