package vekta.spawner;

import vekta.Player;
import vekta.Resources;
import vekta.person.Dialog;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class DialogGenerator {
	private static final Map<String, DialogSpawner> SPAWNERS = Arrays.stream(Resources.getSubclassInstances(DialogSpawner.class))
			.collect(Collectors.toMap(DialogSpawner::getType, Function.identity()));

	public static void setupDialog(Player player, Dialog dialog) {
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
