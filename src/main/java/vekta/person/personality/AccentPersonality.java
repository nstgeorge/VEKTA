package vekta.person.personality;

import vekta.Resources;
import vekta.person.Dialog;

import java.util.List;
import java.util.Map;

import static vekta.Vekta.v;

public class AccentPersonality extends Personality {
	private static final Map<String, List<String>> REPLACE_MAP = Resources.getStringMap("personality_accent_map", false);

	@Override
	public String transformDialog(Dialog dialog, String type, String text) {
		for(String key : REPLACE_MAP.keySet()) {
			text = text.replaceAll(key, v.random(REPLACE_MAP.get(key)));
		}
		return text;
	}

	@Override
	public void prepareDialog(Dialog dialog) {
		if(!"different_language".equals(dialog.getType())) {
			dialog.add("What?", dialog.getPerson().createDialog("different_language"));
		}
	}
}
