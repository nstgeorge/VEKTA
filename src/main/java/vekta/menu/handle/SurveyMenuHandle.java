package vekta.menu.handle;

import vekta.KeyBinding;
import vekta.knowledge.ObservationLevel;
import vekta.menu.Menu;
import vekta.terrain.LandingSite;
import vekta.terrain.feature.Feature;

import static vekta.Vekta.*;

/**
 * Menu renderer for surveying/scanning an object
 */
public class SurveyMenuHandle extends MenuHandle {
	private static final int PLANET_SIZE = 200;

	private final LandingSite site;

	public SurveyMenuHandle(LandingSite site) {
		super(0, PLANET_SIZE, v.width, v.height - PLANET_SIZE);
		this.site = site;
	}

	public LandingSite getSite() {
		return site;
	}

	@Override
	public int getItemY(int i) {
		return super.getItemY(i) + PLANET_SIZE * 2;
	}

	@Override
	public KeyBinding getShortcutKey() {
		return KeyBinding.SHIP_SCAN;
	}

	@Override
	public void focus(Menu menu) {
		super.focus(getMenu());

		getSite().getParent().observe(ObservationLevel.SCANNED, menu.getPlayer());
	}

	@Override
	public void render() {
		super.render();

		v.pushMatrix();
		v.translate(getItemX(), getY() + PLANET_SIZE);

		v.textAlign(CENTER);
		v.textSize(36);
		v.fill(getSite().getParent().getColor());

		v.text(getSite().getParent().getName(), 0, -PLANET_SIZE - 50);

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
		for(Feature feature : site.getTerrain().getFeatures()) {
			v.text(feature.getName(), PLANET_SIZE * 1.5F, (i - (size - 1) / 2F) * 50);
			i++;
		}

		v.strokeWeight(1);
		v.popMatrix();
	}
}
