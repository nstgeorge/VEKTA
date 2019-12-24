package vekta.spawner;

import vekta.Player;
import vekta.Resources;
import vekta.menu.Menu;
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
	private static final Map<String, DialogSpawner> SPAWNERS = Arrays.stream(Resources.findSubclassInstances(DialogSpawner.class))
			.collect(Collectors.toMap(DialogSpawner::getType, Function.identity()));

	private static final Map<String, List<String>> NEXT_MAP = Resources.getStringMap("dialog_next_map", false);

	public static void initDialog(Dialog dialog) {
		List<String> nextList = NEXT_MAP.get(dialog.getType());
		if(nextList != null && !nextList.isEmpty()) {
			dialog.then(v.random(nextList));
		}
	}

	public static void setupDialogMenu(Menu menu, Dialog dialog) {
		DialogSpawner spawner = SPAWNERS.get(dialog.getType());
		if(spawner != null) {
			spawner.setup(menu, dialog);
		}
	}

	public static Dialog randomVisitDialog(Player player, Person person) {
		if(person.getFaction() == player.getFaction()) {
			return person.createDialog("follower");
		}
		else if(person.getOpinion(player.getFaction()) == OpinionType.UNFRIENDLY) {
			return person.createDialog(v.chance(.5F) ? "busy" : "ask_leave");
		}
		else if(person.getOpinion(player.getFaction()) == OpinionType.ENEMY) {
			return person.createDialog(v.chance(.8F) ? "call_security" : "ask_leave");
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

		void setup(Menu menu, Dialog dialog);
	}
}
