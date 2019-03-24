package vekta.person.personality;

import vekta.Resources;

public class LanguageBarrierPersonality extends Personality {
	@Override
	public String transformDialog(String type, String text) {
		return Resources.generateString("dialog_different_language");
	}
}
