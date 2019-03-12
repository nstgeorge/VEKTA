package vekta.menu.handle;

import vekta.menu.Menu;
import vekta.menu.option.MenuOption;
import vekta.terrain.settlement.District;

/**
 * Menu renderer for looting/scavenging
 */
public class DistrictMenuHandle extends MenuHandle {
	private final District district;

	public DistrictMenuHandle(MenuOption defaultOption, District district) {
		super(defaultOption);

		this.district = district;
	}

	public District getDistrict() {
		return district;
	}

	@Override
	public int getSpacing() {
		return 70;
	}

	@Override
	public int getButtonY(int i) {
		return super.getButtonY(i - 2);
	}

	@Override
	public void render(Menu menu) {
		super.render(menu);

		// TODO: customize for districts
	}
}
