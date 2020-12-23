package vekta.menu.handle;

import vekta.menu.Menu;
import vekta.terrain.location.Location;

import static vekta.Vekta.v;

/**
 * Menu renderer for landing on planets
 */
public class LocationMenuHandle extends MenuHandle {

	private final Location location;

	public LocationMenuHandle(Location location) {
		this.location = location;
	}

	public Location getLocation() {
		return location;
	}

	@Override
	public int getItemWidth() {
		return super.getItemWidth() * 2;
	}

	@Override
	public void init(Menu menu) {
		super.init(menu);
	}

	@Override
	public void render() {
		super.render();

		v.textSize(32);
		v.fill(100);
		v.text("Welcome to", v.width / 2F, v.height / 4F - 64);

		v.textSize(48);
		v.fill(location.getColor());

		v.fill(200);
		v.text(location.getName(), v.width / 2F, v.height / 4F);

		v.textSize(20);
		v.fill(100);
		v.text(getSubtext(), v.width / 2F, v.height / 4F + 100);
	}

	protected String getSubtext() {
		return location.getOverview();
	}

}
