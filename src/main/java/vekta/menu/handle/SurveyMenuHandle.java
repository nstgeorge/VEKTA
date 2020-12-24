package vekta.menu.handle;

import vekta.KeyBinding;
import vekta.knowledge.ObservationLevel;
import vekta.menu.Menu;
import vekta.terrain.Terrain;

import java.util.List;
import java.util.stream.Collectors;

import static vekta.Vekta.*;

/**
 * Menu renderer for surveying/scanning an object
 */
public class SurveyMenuHandle extends MenuHandle {
	private static final int PLANET_SIZE = 200;

	private final Terrain terrain;

	public SurveyMenuHandle(Terrain terrain) {
		super(0, PLANET_SIZE, v.width, v.height - PLANET_SIZE);
		this.terrain = terrain;
	}

	public Terrain getTerrain() {
		return terrain;
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

		getTerrain().getPlanet().observe(ObservationLevel.SCANNED, menu.getPlayer());
	}

	@Override
	public void render() {
		super.render();

		v.pushMatrix();
		v.translate(getItemX(), getY() + PLANET_SIZE);

		v.textAlign(CENTER);
		v.textSize(36);
		v.fill(getTerrain().getColor());

		v.text(getTerrain().getName(), 0, -PLANET_SIZE - 50);

		v.shapeMode(CENTER);
		v.strokeWeight(2);
		v.noFill();

		// Draw planet
		getTerrain().getPlanet().drawPreview(PLANET_SIZE);

		// TODO: render object info (mass, radius, etc.)

		// Draw features
		v.textAlign(LEFT, CENTER);
		v.textSize(24);
		v.fill(100);

		int i = 0;
		List<String> tags = getTerrain().findSurveyTags().stream()
				.sorted()
				.collect(Collectors.toList());

		int size = tags.size();
		for(String tag : tags) {
			v.text(tag, PLANET_SIZE * 1.5F, (i - (size - 1) / 2F) * 50);
			i++;
		}

		v.strokeWeight(1);
		v.popMatrix();
	}
}
