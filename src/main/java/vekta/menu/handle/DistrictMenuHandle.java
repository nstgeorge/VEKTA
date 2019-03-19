package vekta.menu.handle;

import vekta.menu.Menu;
import vekta.terrain.settlement.District;

import static vekta.Vekta.v;

/**
 * Menu renderer for looting/scavenging
 */
public class DistrictMenuHandle extends MenuHandle {
	private final District district;

	public DistrictMenuHandle(District district) {
		this.district = district;
	}

	public District getDistrict() {
		return district;
	}

	@Override
	public int getButtonY(int i) {
		return super.getButtonY(i - 1);
	}

	@Override
	public void render(Menu menu) {
		super.render(menu);

		v.textSize(48);
		v.fill(district.getSettlement().getColor());
		v.text(district.getName(), v.width / 2F, getButtonY(-2));
	}
}
