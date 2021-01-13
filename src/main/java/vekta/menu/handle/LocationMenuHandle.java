package vekta.menu.handle;

import vekta.Resources;
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
	public void init() {
		super.init();

		if(location.getTune() != null) {
			location.getTune().reset();
		}

		// TODO: potentially move this to `focus()`
		if(location.getMusic() != null) {
			Resources.setMusic(location.getMusic(), true);
		}
	}

	//	@Override
	//	public int getItemWidth() {
	//		return super.getItemWidth() * 2;
	//	}

	@Override
	public void render() {
		super.render();

		if(location.getTune() != null) {
			location.getTune().update();
		}

		if(location.getWittyText() != null) {
			v.textSize(32);
			v.fill(100);
			v.text(location.getWittyText(), v.width / 2F, v.height / 4F - 64);
		}

		if(location.getName() != null) {
			v.textSize(48);
			v.fill(location.getColor());
			v.text(location.getName(), v.width / 2F, v.height / 4F);
		}

		if(location.getOverview() != null) {
			v.textSize(20);
			v.fill(100);
			v.text(location.getOverview(), v.width / 2F, v.height / 4F + 100);
		}
	}

}
