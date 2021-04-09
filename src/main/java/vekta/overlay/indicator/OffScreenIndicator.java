package vekta.overlay.indicator;

import processing.core.PShape;
import processing.core.PVector;
import vekta.object.SpaceObject;
import vekta.player.Player;
import vekta.world.Singleplayer;

import java.io.Serializable;

import static processing.core.PApplet.cos;
import static processing.core.PConstants.*;
import static vekta.Vekta.getWorld;
import static vekta.Vekta.v;

/**
 * An indicator that shows an arrow along the edge of the screen where the provided object is.
 * The value function is expected to resolve the object that is being pointed to.
 */
public class OffScreenIndicator extends Indicator<SpaceObject> {

	// Arrow settings
	private static final float ARROW_WIDTH = 40;
	private static final float ARROW_HEIGHT = 20;

	// Location settings
	private static final float PADDING = 200;    // Padding on each side of the screen
	private static final float DISTANCE_TO_FADE = v.height / 2f;    // Distance from screen edge before the arrow starts to fade

	private transient PShape arrow;
	private final Player player;

	public OffScreenIndicator(DynamicValue<SpaceObject> value, Player player) {
		super("Offscreen Indicator", value, v.width / 2f, v.height / 2f, v.color(255));

		this.player = player;
	}

	@Override
	public void draw() {
		// TODO: Generalize this to any world type
		if(getTarget() != null) {
			SpaceObject target = getTarget();

			if(getWorld() instanceof Singleplayer) {
				Singleplayer world = ((Singleplayer)getWorld());
				if(!world.isVisibleOnScreen(target)) {

					if(arrow == null) {
						arrow = getArrow(ARROW_WIDTH, ARROW_HEIGHT);
					}

					PVector position = getRelativePosition();
					arrow.rotate(position.heading() + PI / 2);

					position.normalize();
					position.mult((Math.min(v.height, v.width) - PADDING) / 2);

					// Calculate fade, if applicable
					float dist = world.getScreenDistanceFromEdge(target);
					float opacity = dist < DISTANCE_TO_FADE ? dist / DISTANCE_TO_FADE : 1;

					// Draw arrow
					v.pushStyle();
					v.strokeWeight(2);
					v.stroke(getTarget().getColor(), opacity);
					//v.fill(0, opacity);
					v.noFill();
					v.shape(arrow, position.x + (v.width) / 2F, position.y + (v.height) / 2F);
					arrow.rotate(-position.heading() - PI / 2);

					// Draw label offset from arrow
					position.x -= v.textWidth(target.getName()) * cos(position.heading());
					position.y *= 0.9F;
					v.textAlign(CENTER);
					v.text(target.getName(), position.x + (v.width) / 2F, position.y + (v.height) / 2F);
					v.popStyle();
				}
			}
		}
	}

	public SpaceObject getTarget() {
		return getValue();
	}

	private PVector getRelativePosition() {
		return getTarget().getPosition().sub(player.getShip().getPositionReference());
	}

	private static PShape getArrow(float width, float height) {
		PShape shape = v.createShape();
		shape.beginShape();
		shape.vertex(0, height);
		shape.vertex(width / 2, 0);
		shape.vertex(width, height);
		shape.vertex(width / 2, height / 3);
		shape.endShape(CLOSE);
		return shape;
	}
}
