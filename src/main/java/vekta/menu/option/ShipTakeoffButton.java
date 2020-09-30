package vekta.menu.option;

import vekta.menu.Menu;
import vekta.terrain.LandingSite;
import vekta.world.World;

import static vekta.Vekta.setContext;

public class ShipTakeoffButton implements ButtonOption {
	private final LandingSite site;
	private final World world;

	public ShipTakeoffButton(LandingSite site, World world) {
		this.site = site;
		this.world = world;
	}

	@Override
	public String getName() {
		return "Take Off";
	}

	@Override
	public void onSelect(Menu menu) {
		setContext(world);
//		applyContext();
		site.takeoff();
	}
}
