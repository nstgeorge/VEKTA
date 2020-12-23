package vekta.menu.handle;

import vekta.knowledge.ObservationLevel;
import vekta.menu.Menu;
import vekta.terrain.settlement.Settlement;

import static vekta.Vekta.getNextContext;
import static vekta.Vekta.v;

/**
 * Menu renderer for landing on planets
 */
public class SettlementMenuHandle extends MenuHandle {
	private static final float ZOOM_TIME = .05F;

	private final Settlement settlement;

	private float smoothZoom = 1;
	private float smoothX;
	private float smoothY;

	public SettlementMenuHandle(Settlement settlement) {
		//super(0, v.height / 4 + 120, v.width, v.height - (v.height / 4 + 240));
		this.settlement = settlement;
	}

	public Settlement getSettlement() {
		return settlement;
	}

	@Override
	public int getItemWidth() {
		return super.getItemWidth() * 2;
	}

	@Override
	public void init(Menu menu) {
		super.init(menu);

		onVisit(getMenu());
	}

	@Override
	public void unfocus(Menu menu) {
		super.unfocus(getMenu());

		// Transition zoom between menus
		if(getNextContext() instanceof Menu && ((Menu)getNextContext()).getHandle() instanceof SettlementMenuHandle) {
			SettlementMenuHandle handle = (SettlementMenuHandle)((Menu)getNextContext()).getHandle();
			handle.smoothZoom = smoothZoom;
			handle.smoothX = smoothX;
			handle.smoothY = smoothY;
		}
	}

	@Override
	public void beforeDraw() {
		super.beforeDraw();

		smoothZoom += (getZoomScale() - smoothZoom) * ZOOM_TIME;
		smoothX += (getX() - smoothX) * ZOOM_TIME;
		smoothY += (getY() - smoothY) * ZOOM_TIME;

		// Draw settlement
		v.pushMatrix();
		v.translate(v.width / 2F + smoothX, v.height / 2F + smoothY);
		v.rotate(v.frameCount / 5000F);
		v.scale(smoothZoom);
		v.noFill();
		v.stroke(v.lerpColor(0, getSettlement().getColor(), .3F));
		getSettlement().getVisual().draw();
		v.popMatrix();
	}

	@Override
	public void render() {
		super.render();

		v.textSize(32);
		v.fill(100);
		v.text("Welcome to", v.width / 2F, v.height / 4F - 64);

		v.textSize(48);
		v.fill(settlement.getColor());

		v.fill(200);
		v.text(settlement.getName(), v.width / 2F, v.height / 4F);

		v.textSize(20);
		v.fill(100);
		v.text(getSubtext(), v.width / 2F, v.height / 4F + 100);
	}

	protected String getSubtext() {
		return settlement.getGenericName() + ", " + settlement.getFaction().getName();
	}

	protected void onVisit(Menu menu) {
		getSettlement().observe(ObservationLevel.VISITED, getMenu().getPlayer());
	}

	protected float getZoomScale() {
		return 1;
	}

//	protected float getX() {
//		return smoothX;
//	}
//
//	public float getY() {
//		return smoothY;
//	}
}
