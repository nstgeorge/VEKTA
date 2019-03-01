package vekta.menu.handle;

import vekta.context.World;
import vekta.menu.Menu;
import vekta.menu.option.TakeoffOption;
import vekta.object.SpaceObject;
import vekta.terrain.LandingSite;

/**
 * Menu renderer for landing on planets
 */
public class LandingMenuHandle extends MenuHandle {
	private final LandingSite site;

	public LandingMenuHandle(LandingSite site, World world) {
		super(new TakeoffOption(site, world));

		this.site = site;
	}

	public void render(Menu menu) {
		super.render(menu);

		SpaceObject s = site.getParent();
		v.textSize(32);
		v.fill(100);
		v.text("Welcome to", v.width / 2F, v.height / 4F - 32);
		v.textSize(48);
		v.fill(s.getColor());
		v.text(s.getName(), v.width / 2F, v.height / 4F + 32);
		v.textSize(20);
		v.fill(100);
		v.text(site.getTerrain().getOverview(), v.width / 2F, v.height / 4F + 128);
	}
}
