package vekta.overlay.indicator;

import processing.core.PShape;
import processing.core.PVector;
import vekta.object.SpaceObject;
import vekta.object.ship.Ship;
import vekta.world.Singleplayer;

import javax.lang.model.type.NullType;
import java.io.Serializable;
import java.util.function.Function;

import static processing.core.PConstants.CLOSE;
import static processing.core.PConstants.PI;
import static vekta.Vekta.v;

/**
 * An indicator that shows an arrow along the edge of the screen where the provided object is.
 * The value function is expected to resolve the object that is being pointed to.
 */
public class OffScreenIndicator extends Indicator<NullType, SpaceObject> implements Serializable {

	// Arrow settings
	private static final float ARROW_WIDTH = 40;
	private static final float ARROW_HEIGHT = 20;

	// Location settings
	private static final float HORIZ_PADDING = 100;	// Padding on each side of the screen
	private static final float VERT_PADDING = 400;	// Padding on the top/bottom of the screen

	private PShape arrow = getArrow(ARROW_WIDTH, ARROW_HEIGHT);
	private Ship ship;

	public OffScreenIndicator(Function value, Ship ship) {
		super("Offscreen Indicator", value, v.width / 2, v.height / 2, v.color(255));
		this.ship = ship;
	}

	public OffScreenIndicator(SpaceObject target, Ship ship) {
		super("Offscreen Indicator", t -> target, v.width / 2, v.height / 2, v.color(255));
		this.ship = ship;
	}

	@Override
	public void draw() {
		// TODO: Generalize this to any world type
		if(v.getWorld() instanceof Singleplayer) {
			if(getTarget() != null) {
				if(!((Singleplayer)v.getWorld()).isObjectVisibleToPlayer(getTarget())) {
					PVector position = getRelativePosition();
					arrow.rotate(position.heading() + PI / 2);

					// Draw arrow
					v.stroke(getTarget().getColor());
					v.noFill();
					v.shape(arrow, v.cos(position.heading()) * (v.width / 2 - HORIZ_PADDING) + (HORIZ_PADDING + v.width) / 2,v.sin(position.heading()) * (v.height / 2 - VERT_PADDING) + (VERT_PADDING + v.height) / 2);
					arrow.rotate(-position.heading() - PI / 2);
				}
			}
		}
	}

	public SpaceObject getTarget() {
		return value.apply(null);
	}

	private PVector getRelativePosition() {
		return getTarget().getPosition().sub(ship.getPosition());
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
