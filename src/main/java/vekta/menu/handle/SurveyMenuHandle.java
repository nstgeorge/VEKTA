package vekta.menu.handle;

import vekta.KeyBinding;
import vekta.knowledge.ObservationLevel;
import vekta.menu.Menu;
import vekta.terrain.LandingSite;

import static vekta.Vekta.*;

/**
 * Menu renderer for surveying/scanning an object
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
	public int getItemY(int i) {
		return super.getItemY(i) + PLANET_SIZE;
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
		v.translate(getItemX(), getItemY(-1) - PLANET_SIZE);

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

		int i = 0;
		int size = site.getTerrain().getFeatures().size();
		for(String feature : site.getTerrain().getFeatures()) {
			v.text(feature, PLANET_SIZE * 1.5F, (i - (size - 1) / 2F) * 50);
			i++;
		}

		v.strokeWeight(1);
		v.popMatrix();
	}
}
