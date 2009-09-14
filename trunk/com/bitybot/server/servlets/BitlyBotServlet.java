package com.bitybot.server.servlets;

import java.io.IOException;
import java.util.logging.Logger;

import org.json.JSONException;

import com.bitybot.server.util.Util;
import com.google.wave.api.AbstractRobotServlet;
import com.google.wave.api.Blip;
import com.google.wave.api.Event;
import com.google.wave.api.RobotMessageBundle;
import com.google.wave.api.TextView;
import com.google.wave.api.Wavelet;

@SuppressWarnings("serial")
public class BitlyBotServlet extends AbstractRobotServlet {
	private static final Logger log = Logger.getLogger(BitlyBotServlet.class
			.getName());
	private final String INSTRUCTION = "Hello i will shorten your url with Bit.ly";

	@Override
	public void processEvents(RobotMessageBundle robotMessageBundle) {
		Wavelet wavelet = robotMessageBundle.getWavelet();
		// detect robot self add event
		if (robotMessageBundle.wasSelfAdded()) {
			Blip blip = wavelet.appendBlip();
			TextView textView = blip.getDocument();
			textView.append(INSTRUCTION);
			log.warning("\nAddedOn:" + wavelet.getTitle() + "\nAddedBy:"
					+ blip.getDocument().getAuthor() + "-"
					+ wavelet.getCreator());
		} else {
			for (Event event : robotMessageBundle.getBlipSubmittedEvents()) {
				Blip blip = event.getBlip();
				String text = blip.getDocument().getText();
				if (blip.getBlipId().equals(wavelet.getRootBlipId())) {
					// this is the root blip, use getTitle() to get its content

				} else {
					String replacement = null;
					try {
						replacement = Util.process(text);
					} catch (JSONException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					if (replacement != null) {
						TextView view=blip.getDocument();
						view.replace(replacement);
						view.appendMarkup("<br/><br/><b>Try my online version http://bit.ly/bitlybot</b>");
						log.warning("\nOn:" + wavelet.getTitle() + "\nBy:"
								+ blip.getDocument().getAuthor() + "-"
								+ blip.getCreator() + "-"
								+ wavelet.getCreator() + "\nMessage:" + text
								+ "\nReplacement:" + replacement);
					}
				}
			}
		}
	}

}
