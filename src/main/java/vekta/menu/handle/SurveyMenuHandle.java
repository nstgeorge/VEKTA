package vekta.menu.handle;

import vekta.KeyBinding;
import vekta.knowledge.ObservationLevel;
import vekta.menu.Menu;
import vekta.terrain.LandingSite;

import java.util.List;

import static vekta.Vekta.*;

/**
 * Main inject renderer
 */
public class SurveyMenuHandle extends MenuHandle {
	private static final int PLANET_SIZE = 200;

	private final LandingSite site;

	public SurveyMenuHandle(LandingSite site) {
		this.site = site;
	}

	public LandingSite getSite() {
		return site;
	}

	@Override
	public int getButtonY(int i) {
		return super.getButtonY(i) + PLANET_SIZE;
	}

	@Override
	public KeyBinding getShortcutKey() {
		return KeyBinding.SHIP_SCAN;
	}

	@Override
	public void focus(Menu menu) {
		super.focus(menu);

		getSite().getParent().observe(ObservationLevel.SCANNED, menu.getPlayer());
	}

	@Override
	public void render(Menu menu) {
		super.render(menu);

		v.pushMatrix();
		v.translate(getButtonX(), getButtonY(-1) - PLANET_SIZE);

		v.shapeMode(CENTER);
		v.strokeWeight(2);
		v.noFill();

		// Draw planet
		getSite().getParent().drawPreview(PLANET_SIZE);

		// TODO: render object info (mass, radius, etc.)

		// Draw features
		v.textAlign(LEFT, CENTER);
		v.textSize(24);
		v.fill(100);
		List<String> features = site.getTerrain().getFeatures();
		for(int i = 0; i < features.size(); i++) {
			String feature = features.get(i);
			v.text(feature, PLANET_SIZE * 1.5F, (i - (features.size() - 1) / 2F) * 50);
		}

		v.strokeWeight(1);
		v.popMatrix();
	}
}
