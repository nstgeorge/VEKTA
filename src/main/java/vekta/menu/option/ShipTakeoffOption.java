package vekta.menu.option;

import vekta.menu.Menu;
import vekta.terrain.LandingSite;
import vekta.context.World;

import static vekta.Vekta.applyContext;
import static vekta.Vekta.setContext;

public class ShipTakeoffOption implements MenuOption {
	private final LandingSite site;
	private final World world;

	public ShipTakeoffOption(LandingSite site, World world) {
		this.site = site;
		this.world = world;
	}

	@Override
	public String getName() {
		return "Take Off";
	}

	@Override
	public void select(Menu menu) {
		setContext(world);
//		applyContext();
		site.takeoff();
	}
}
