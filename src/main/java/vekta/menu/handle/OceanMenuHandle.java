package vekta.menu.handle;

import vekta.terrain.location.OceanLocation;

import static processing.core.PApplet.abs;
import static processing.core.PApplet.sin;
import static vekta.Vekta.v;

public class OceanMenuHandle extends MenuHandle {
	private static final float WAVE_HEIGHT = 100;
	private static final float[][] WAVE_DATA = {
			{350, 200, 1},
			{220, 310, .2F},
			{50, 140, .15F},
			{90, -120, .1F},
			{30, 370, .05F},
	};

	private final OceanLocation location;

	public OceanMenuHandle(OceanLocation location) {
		this.location = location;
	}

	public OceanLocation getLocation() {
		return location;
	}

	@Override
	public void render() {
		super.render();

		float width = v.width * .75F;
		v.stroke(location.getColor());
		drawOcean(getItemX() - width / 2, getItemY(-2) - WAVE_HEIGHT / 2, width, WAVE_HEIGHT);
	}

	public static void drawOcean(float xLeft, float yTop, float width, float height) {
		float t = v.frameCount;

		float px = 0, py = 0;
		for(float x = 0; x <= width; x += 1F) {
			float y = 0;
			for(float[] data : WAVE_DATA) {
				y += abs(sin(x / data[0] + t / data[1]) * data[2]) * height;
			}

			float xx = xLeft + x;
			float yy = yTop + y;
			if(x > 0) {
				v.line(xx, yy, px, py);
			}
			px = xx;
			py = yy;
		}
	}
}
