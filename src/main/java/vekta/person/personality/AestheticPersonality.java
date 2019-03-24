package vekta.person.personality;

import vekta.Resources;
import vekta.person.Dialog;

public class AestheticPersonality extends Personality {
	@Override
	public String transformDialog(String type, String text) {
		return String.join(" ", text.split("")).trim()
				.replaceAll("\\s\\s", " ")
				.toLowerCase();
	}

	@Override
	public void setupDialog(Dialog dialog) {
		dialog.parseResponse(Resources.generateString("response_aesthetic"));
	}
}
