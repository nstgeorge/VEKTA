package vekta.menu.handle;

import vekta.menu.Menu;
import vekta.terrain.settlement.District;
import vekta.terrain.settlement.Settlement;

/**
 * Menu renderer for visiting districts
 */
public class DistrictMenuHandle extends SettlementMenuHandle {
	private final District district;

	public DistrictMenuHandle(Settlement settlement, District district) {
		super(settlement);

		this.district = district;
	}

	public District getDistrict() {
		return district;
	}

//	@Override
//	public int getItemY(int i) {
//		return super.getItemY(i - 1);
//	}

	@Override
	public void render() {
		super.render();

		//		v.textSize(48);
		//		v.fill(district.getSettlement().getColor());
		//		v.text(district.getName(), v.width / 2F, getItemY(-2));
	}

	@Override
	protected String getSubtext() {
		return getDistrict().getName();
	}

	@Override
	protected void onVisit(Menu menu) {
		// Override default behavior (observe settlement)
	}

	@Override
	protected float getZoomScale() {
		return 2;
	}

	@Override
	protected float getX() {
		return getDistrict().getX();
	}

	@Override
    public float getY() {
		return getDistrict().getY();
	}
}
