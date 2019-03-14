package vekta.spawner;

import vekta.Player;
import vekta.Resources;
import vekta.person.Dialog;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static vekta.Vekta.v;

public final class DialogGenerator {
	private static final Map<String, DialogSpawner> SPAWNERS = Arrays.stream(Resources.getSubclassInstances(DialogSpawner.class))
			.collect(Collectors.toMap(DialogSpawner::getType, Function.identity()));

	private static final Map<String, List<String>> NEXT_MAP = Resources.getStringMap("dialog_next_map", false);

	public static void initDialog(Dialog dialog) {
		List<String> nextList = NEXT_MAP.get(dialog.getType());
		if(nextList != null && !nextList.isEmpty()) {
			dialog.then(v.random(nextList));
		}
	}
	
	public static void setupPlayerDialog(Player player, Dialog dialog) {
		DialogSpawner spawner = SPAWNERS.get(dialog.getType());
		if(spawner != null) {
			spawner.setup(player, dialog);
		}
	}

	public interface DialogSpawner {
		String getType();

		void setup(Player player, Dialog dialog);
	}
}
