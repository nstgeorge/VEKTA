package vekta.overlay.indicator;

import processing.core.PShape;
import processing.core.PVector;
import vekta.object.SpaceObject;
import vekta.object.ship.Ship;
import vekta.world.Singleplayer;

import javax.lang.model.type.NullType;
import java.io.Serializable;
import java.util.function.Function;

import static processing.core.PConstants.*;
import static vekta.Vekta.v;

public class OnScreenIndicator extends Indicator<NullType, SpaceObject> implements Serializable {

	private static final int NAMELINE_OFFSET = 50; 		// Offset (px) of the name line from the box.
	private static final float NAMELINE_PADDING = 10;	// Extra length on the name line.
	private static final int INDICATOR_SIZE = 30;		// Size of the box around the target.

	private Ship ship;

	public OnScreenIndicator(Function value, Ship ship) {
		super("On-Screen Indicator", value, v.width / 2, v.height / 2, v.color(255));
		this.ship = ship;
	}

	public OnScreenIndicator(SpaceObject target, Ship ship) {
		this(t -> target,ship);
	}

	@Override
	public void draw() {
		SpaceObject target = getValue();

		if(v.getWorld() instanceof Singleplayer) {
			if(target != null) {
				PVector oppositeOfShipHeading = target.getPosition().sub(ship.getPosition()).normalize().mult(INDICATOR_SIZE / 2F);

				Singleplayer world = ((Singleplayer)v.getWorld());
				if(world.isObjectVisibleToPlayer(target)) {
					PVector screenLocation = world.getObjectScreenLocation(target);

					screenLocation.x += v.width / 2F;
					screenLocation.y += v.height / 2F;

					// Draw target indicator
					v.pushStyle();
					v.rectMode(CENTER);
					v.stroke(150);
					v.noFill();
					v.ellipse(screenLocation.x, screenLocation.y, INDICATOR_SIZE, INDICATOR_SIZE);

					// Draw the name line
					v.shape(getNameLine(INDICATOR_SIZE, oppositeOfShipHeading), screenLocation.x, screenLocation.y);

					// Write the target name
					v.color(target.getColor());
					if(v.cos(oppositeOfShipHeading.heading()) > 0) {
						v.textAlign(LEFT);
					} else {
						v.textAlign(RIGHT);
					}
					v.text(target.getName(), screenLocation.x + oppositeOfShipHeading.x + (v.cos(oppositeOfShipHeading.heading()) * NAMELINE_OFFSET), screenLocation.y + oppositeOfShipHeading.y + (v.sin(oppositeOfShipHeading.heading()) * NAMELINE_OFFSET) + 2);
					v.popStyle();
				}
			}
		}
	}

	/**
	 * Draws the line that connects the name to the indicator.
	 * @param oppositeOfShipHeading PVector containing the inverse of the vector leading from the target to the ship.
	 * @return PShape of the line from the indicator to the name and the underline.
	 */
	private PShape getNameLine(float size, PVector oppositeOfShipHeading) {

		SpaceObject target = getValue();

		// Create the name line
		PShape nameLine = v.createShape(GROUP);

		// Define the line parts
		PShape connector = v.createShape(LINE, oppositeOfShipHeading.x, oppositeOfShipHeading.y, oppositeOfShipHeading.x + (v.cos(oppositeOfShipHeading.heading()) * NAMELINE_OFFSET), oppositeOfShipHeading.y + (v.sin(oppositeOfShipHeading.heading()) * NAMELINE_OFFSET));

		connector.stroke(target.getColor());

		// Determine which direction the ship is (left/right) from the target, then draw the line extending under the object name
		PShape underline;
		if(v.cos(oppositeOfShipHeading.heading()) > 0) {
			underline = v.createShape(LINE, oppositeOfShipHeading.x + (v.cos(oppositeOfShipHeading.heading()) * NAMELINE_OFFSET), oppositeOfShipHeading.y + (v.sin(oppositeOfShipHeading.heading()) * NAMELINE_OFFSET), oppositeOfShipHeading.x + (v.cos(oppositeOfShipHeading.heading()) * NAMELINE_OFFSET) + v.textWidth(target.getName()) + NAMELINE_PADDING, oppositeOfShipHeading.y + (v.sin(oppositeOfShipHeading.heading()) * NAMELINE_OFFSET));
		} else {
			underline = v.createShape(LINE, oppositeOfShipHeading.x + (v.cos(oppositeOfShipHeading.heading()) * NAMELINE_OFFSET), oppositeOfShipHeading.y + (v.sin(oppositeOfShipHeading.heading()) * NAMELINE_OFFSET), oppositeOfShipHeading.x + (v.cos(oppositeOfShipHeading.heading()) * NAMELINE_OFFSET) - v.textWidth(target.getName()) - NAMELINE_PADDING, oppositeOfShipHeading.y + (v.sin(oppositeOfShipHeading.heading()) * NAMELINE_OFFSET));
		}

		underline.stroke(150);

		nameLine.addChild(connector);
		nameLine.addChild(underline);

		return nameLine;
	}
}
