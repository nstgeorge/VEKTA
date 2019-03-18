package vekta.spawner.dialog;

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
	public void setup(Menu menu, Dialog dialog) {
		dialog.add(new CustomOption("(Learn More)", m -> {
			Menu sub = new Menu(m.getPlayer(), m.getDefault(), new MissionMenuHandle());
			sub.add(new MissionOption(MissionGenerator.createMission(m.getPlayer(), dialog.getPerson())));
			sub.addDefault();
			setContext(sub);
		}));
	}
}
