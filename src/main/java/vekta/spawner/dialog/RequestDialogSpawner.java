package vekta.spawner.dialog;

import vekta.Player;
import vekta.menu.Menu;
import vekta.menu.handle.MissionMenuHandle;
import vekta.menu.option.CustomOption;
import vekta.menu.option.MissionOption;
import vekta.person.Dialog;
import vekta.spawner.DialogGenerator;
import vekta.spawner.MissionGenerator;

import static vekta.Vekta.setContext;

public class RequestDialogSpawner implements DialogGenerator.DialogSpawner {
	@Override
	public String getType() {
		return "request";
	}

	@Override
	public void setup(Player player, Dialog dialog) {
		dialog.add(new CustomOption("Learn More", menu -> {
			Menu sub = new Menu(menu.getPlayer(), new MissionMenuHandle(menu.getDefault()));
			sub.add(new MissionOption(MissionGenerator.createMission(player, dialog.getPerson())));
			sub.addDefault();
			setContext(sub);
		}));
	}
}
