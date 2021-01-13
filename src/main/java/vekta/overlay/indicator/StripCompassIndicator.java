package vekta.overlay.indicator;

import processing.core.PVector;
import vekta.object.SpaceObject;
import vekta.object.ship.Ship;

import javax.lang.model.type.NullType;
import java.io.Serializable;
import java.util.function.Function;

import static processing.core.PApplet.sin;
import static processing.core.PConstants.CENTER;
import static processing.core.PConstants.LEFT;
import static vekta.Vekta.v;

/**
 * Displays the heading of the player.
 */
public class StripCompassIndicator extends Indicator<NullType, PVector> implements Serializable {

	private static final int RESOLUTION = 36;        // Number of bars to draw
	private static final int HEIGHT = 20;            // Height of the largest indicator bars
	private static final int OFFSET = 10;            // Offset in height of the smaller bars
	private static final float WIDTH = v.width / 2f;    // Width of the indicator as a whole

	public StripCompassIndicator(String name, Function value, float locX, float locY, int color) {
		super(name, value, locX, locY, color);
	}

	@Override
	public void draw() {
		int left = (int)((v.width - WIDTH) / 2);

		// Draw arrow
		v.stroke(0, 255, 0);
		v.line(v.width / 2f + 10, 75, v.width / 2f, 65);
		v.line(v.width / 2f - 10, 75, v.width / 2f, 65);

		// Write current heading
		int headingNumber = (int)Math.toDegrees(getValue().heading());
		if(Math.signum(headingNumber) == -1)
			headingNumber += 360;
		v.color(getColor());
		v.textAlign(CENTER);
		v.text(headingNumber, v.width / 2f, 90);

		// Draw lines
		for(int i = 0; i < RESOLUTION * 3; i++) {
			int horizontalLocation = (int)(left + ((i - RESOLUTION) * (WIDTH / RESOLUTION)) - ((float)((headingNumber - 180.0) / 360.0) * (WIDTH)));
			float sinModifier = sin((float)(Math.PI * ((horizontalLocation - left - (headingNumber - 180.0) / 360.0) / WIDTH)));

			v.stroke(0, 255, 0, 255 * sinModifier);
			v.line(horizontalLocation, locY, horizontalLocation, locY + HEIGHT - (i % 4 == 0 ? 0 : OFFSET));

			if((i % 4) == 0) {
				v.fill(0, 255, 0, 255 * sinModifier);
				v.textSize(14);
				v.text((i % RESOLUTION) * 360 / RESOLUTION, horizontalLocation, locY + HEIGHT + 16);
			}
		}
		v.textAlign(LEFT);
	}
}
