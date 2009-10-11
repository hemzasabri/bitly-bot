package com.bitybot.server.servlets;

import java.util.logging.Logger;

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

	// private final String INSTRUCTION = "Hello i will shorten your url with
	// Bit.ly";

	@Override
	public void processEvents(RobotMessageBundle robotMessageBundle) {
		Wavelet wavelet = robotMessageBundle.getWavelet();
		// detect robot self add event
		if (robotMessageBundle.wasSelfAdded()) {
			// Blip blip = wavelet.appendBlip();
			// TextView textView = blip.getDocument();
			// textView.append(INSTRUCTION);
			log.warning("\n AddedOn:" + wavelet.getTitle() + "\n AddedBy:"
					+ wavelet.getCreator());
		} else {
			for (Event event : robotMessageBundle.getBlipSubmittedEvents()) {
				Blip blip = event.getBlip();
				String text = blip.getDocument().getText();
				if (text.length() != 0) {
					String replacement = null;
					replacement = Util.process(text);
					if (replacement != null && !replacement.equals(text)) {
						TextView view = blip.getDocument();
						view.replace(replacement);
						log.warning("\n On:" + wavelet.getTitle() + "\n By:"
								+ blip.getDocument().getAuthor() + " - "
								+ blip.getCreator() + " - "
								+ wavelet.getCreator() + "\n Message:" + text
								+ "\n Replacement:" + replacement);
						return;
					}
				}
			}
		}
	}

}
