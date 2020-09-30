package vekta.mission.objective;

import vekta.item.ItemType;
import vekta.menu.Menu;
import vekta.menu.handle.LandingMenuHandle;
import vekta.menu.option.CustomButton;
import vekta.object.SpaceObject;
import vekta.terrain.LandingSite;

public class LandingTaskObjective extends Objective {
	private final String name;
	private final SpaceObject object;

	public LandingTaskObjective(String name) {
		this(name, null);
	}

	public LandingTaskObjective(String name, SpaceObject object) {
		this.name = name;
		this.object = object;
	}

	@Override
	public String getName() {
		return name + (getSpaceObject() != null ? " (" + getSpaceObject().getName() + ")" : "");
	}

	@Override
	public SpaceObject getSpaceObject() {
		return object;
	}

	@Override
	public void onMenu(Menu menu) {
		if(menu.getHandle() instanceof LandingMenuHandle) {
			LandingSite site = ((LandingMenuHandle)menu.getHandle()).getSite();
			if(getSpaceObject() == null || site.getParent() == getSpaceObject()) {
				menu.add(new CustomButton(name, m -> complete())
						.withColor(ItemType.KNOWLEDGE.getColor())
						.withRemoval());
			}
		}
	}
}
