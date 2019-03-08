package vekta.mission;

import vekta.menu.Menu;
import vekta.menu.handle.LandingMenuHandle;
import vekta.menu.option.BasicOption;
import vekta.object.SpaceObject;
import vekta.terrain.LandingSite;

public class TaskObjective extends Objective {
	private final String name;
	private final SpaceObject object;

	public TaskObjective(String name) {
		this(name, null);
	}

	public TaskObjective(String name, SpaceObject object) {
		this.name = name;
		this.object = object;
	}

	@Override
	public String getName() {
		return name + " (" + (getSpaceObject() != null ? getSpaceObject().getName() : "anywhere") + ")";
	}

	@Override
	public SpaceObject getSpaceObject() {
		return object;
	}

	@Override
	public void onMenu(Menu menu) {
		if(menu.getHandle() instanceof LandingMenuHandle) {
			LandingSite site = ((LandingMenuHandle)menu.getHandle()).getSite();
			if(site.getParent() == getSpaceObject()) {
				menu.add(new BasicOption(getName(), m -> {
					complete();
					m.close();
				}));
			}
		}
	}
}
