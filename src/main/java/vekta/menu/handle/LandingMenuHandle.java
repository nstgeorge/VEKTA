package vekta.menu.handle;

import vekta.LandingSite;
import vekta.object.SpaceObject;
import vekta.Vekta;
import vekta.context.World;
import vekta.menu.Menu;
import vekta.menu.TakeoffOption;

import static vekta.Vekta.getInstance;

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

		Vekta v = getInstance();
		SpaceObject s = site.getParent();
		v.textSize(32);
		v.fill(100);
		v.text("Welcome to", v.width / 2F, v.height / 4F);
		v.textSize(48);
		v.fill(s.getColor());
		v.text(s.getName(), v.width / 2F, v.height / 4F + 64);
	}
}
