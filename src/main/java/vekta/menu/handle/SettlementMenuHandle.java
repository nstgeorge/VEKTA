package vekta.menu.handle;

import vekta.knowledge.ObservationLevel;
import vekta.menu.Menu;
import vekta.terrain.settlement.Settlement;

import static vekta.Vekta.v;

/**
 * Menu renderer for landing on planets
 */
public class SettlementMenuHandle extends MenuHandle {
	private final Settlement settlement;

	public SettlementMenuHandle(Settlement settlement) {
		this.settlement = settlement;
	}

	public Settlement getSettlement() {
		return settlement;
	}

	@Override
	public int getButtonWidth() {
		return super.getButtonWidth() * 2;
	}

	@Override
	public void init(Menu menu) {
		super.init(menu);

		getSettlement().observe(ObservationLevel.VISITED, menu.getPlayer());
	}

	@Override
	public void beforeDraw() {
		super.beforeDraw();

		// Draw settlement
		v.pushMatrix();
		v.translate(v.width / 2F, v.height / 2F/*getButtonY(-2)*/);
		v.rotate(v.frameCount / 5000F);
		v.noFill();
		v.stroke(v.lerpColor(0, getSettlement().getColor(), .3F));
		getSettlement().getVisual().draw();
		v.popMatrix();
	}

	@Override
	public void render(Menu menu) {
		super.render(menu);

		v.textSize(32);
		v.fill(100);
		v.text("Welcome to", v.width / 2F, getButtonY(-2) - 64);

		v.textSize(48);
		v.fill(settlement.getColor());

		v.fill(200);
		v.text(settlement.getName(), v.width / 2F, getButtonY(-2));

		v.textSize(20);
		v.fill(100);
		v.text(settlement.getGenericName() + ", " + settlement.getFaction().getName(), v.width / 2F, getButtonY(-2) + 50);
	}
}
