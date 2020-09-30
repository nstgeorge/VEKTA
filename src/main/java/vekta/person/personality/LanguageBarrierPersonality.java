package vekta.person.personality;

import vekta.Resources;
import vekta.person.Dialog;

public class LanguageBarrierPersonality extends Personality {
	@Override
	public String transformDialog(Dialog dialog, String type, String text) {
		return Resources.generateString("dialog_different_language");
	}
}
