package vekta.menu.handle;

import vekta.menu.Menu;
import vekta.terrain.LandingSite;

import java.util.List;

import static vekta.Vekta.*;

/**
 * Main inject renderer
 */
public class SurveyMenuHandle extends MenuHandle {
	private static final int PLANET_SIZE = 200;
	private static final int PLANET_RES = 32;
	private static final float ROTATE_SPEED = 1F / 2000;
	private static final float SCAN_SPEED = 1F / 100;

	private final LandingSite site;

	private final float perspective;

	public SurveyMenuHandle(LandingSite site) {
		this.site = site;

		perspective = v.random(PI);
	}

	public LandingSite getSite() {
		return site;
	}

	@Override
	public int getButtonY(int i) {
		return super.getButtonY(i) + PLANET_SIZE;
	}

	@Override
	public void render(Menu menu) {
		super.render(menu);

		float rotate = v.frameCount * ROTATE_SPEED;
		float scan = v.frameCount * SCAN_SPEED;

		int color = site.getParent().getColor();

		v.pushMatrix();
		v.translate(getButtonX(), getButtonY(-1) - PLANET_SIZE);

		v.shapeMode(CENTER);
		v.strokeWeight(2);
		v.noFill();

		// Draw scanner arc
		float scanScale = cos(scan);
		v.stroke(v.lerpColor(0, color, sq(cos(scan / 2 + perspective))));
		v.arc(0, 0, PLANET_SIZE * scanScale, PLANET_SIZE, -HALF_PI, HALF_PI);

		// Draw planet
		for(float r = 0; r < TWO_PI; r += TWO_PI / PLANET_RES) {
			float angle = r + rotate;
			float xScale = cos(angle);
			v.stroke(v.lerpColor(0, color, sq(cos(r / 2 + perspective))));
			v.arc(0, 0, PLANET_SIZE * xScale, PLANET_SIZE, -HALF_PI, HALF_PI);
		}

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
