package vekta.menu.handle;

import vekta.menu.Menu;
import vekta.object.SpaceObject;
import vekta.terrain.LandingSite;

import static vekta.Vekta.v;

/**
 * Menu renderer for landing on planets
 */
public class LandingMenuHandle extends MenuHandle {
	private final LandingSite site;

	public LandingMenuHandle(LandingSite site) {
		this.site = site;
	}

	public LandingSite getSite() {
		return site;
	}

	@Override
	public void init(Menu menu) {
		setMenu(menu);
		site.getTune().reset();
	}

	@Override
	public void render() {
		super.render();

		site.getTune().update();

		SpaceObject s = site.getParent();
		v.textSize(32);
		v.fill(100);
		v.text("Welcome to", v.width / 2F, v.height / 4F - 64);
		v.textSize(48);
		v.fill(s.getColor());
		v.text(s.getName(), v.width / 2F, v.height / 4F);
		v.textSize(20);
		v.fill(100);
		v.text(site.getTerrain().getOverview(), v.width / 2F, v.height / 4F + 100);
	}
}
