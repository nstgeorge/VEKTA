package vekta.spawner;

import vekta.Player;
import vekta.Resources;
import vekta.person.Dialog;
import vekta.person.OpinionType;
import vekta.person.Person;

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

	public static Dialog randomVisitDialog(Player player, Person person) {
		if(person.getOpinion(player.getFaction()).isNegative()) {
			return person.createDialog("busy");
		}
		else if(person.isBusy()) {
			return person.createDialog("greeting").then("busy");
		}
		else {
			return randomApproachDialog(player, person);
		}
	}

	public static Dialog randomApproachDialog(Player player, Person person) {
		Dialog dialog;
		if(person.getOpinion(player.getFaction()) == OpinionType.GRATEFUL && v.chance(.4F)) {
			dialog = person.createDialog("offer");
			person.setOpinion(player.getFaction(), OpinionType.FRIENDLY);
		}
		else if(v.chance(.3F)) {
			dialog = person.createDialog("offer");
		}
		else {
			dialog = person.createDialog("request");
		}
		return dialog;
	}

	public interface DialogSpawner {
		String getType();

		void setup(Player player, Dialog dialog);
	}
}
