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

	private final Ship ship;

	public OnScreenIndicator(Function value, Ship ship) {
		super("On-Screen Indicator", value, v.width / 2F, v.height / 2F, v.color(255));
		this.ship = ship;
	}

	public OnScreenIndicator(SpaceObject target, Ship ship) {
		this(t -> target, ship);
	}

	@Override
	public void draw() {
		SpaceObject target = getValue();

		if(v.getWorld() instanceof Singleplayer) {
			if(target != null) {
				PVector oppositeOfShipHeading = target.getPosition().sub(ship.getPosition()).setMag(INDICATOR_SIZE);

				Singleplayer world = ((Singleplayer)v.getWorld());
				if(world.isObjectVisibleToPlayer(target) && (target.getRadius() / world.getZoom()) < (INDICATOR_SIZE / 2F)) {
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

					v.line(screenLocation.x + oppositeOfShipHeading.x, screenLocation.y + oppositeOfShipHeading.y, screenLocation.x + oppositeOfShipHeading.x + (v.cos(oppositeOfShipHeading.heading()) * NAMELINE_OFFSET), screenLocation.y + oppositeOfShipHeading.y + (v.sin(oppositeOfShipHeading.heading()) * NAMELINE_OFFSET));

					// Determine which direction the ship is (left/right) from the target, then draw the line extending under the object name
					// I'm sorry for my sins
					if(v.cos(oppositeOfShipHeading.heading()) > 0) {
						v.line(screenLocation.x + oppositeOfShipHeading.x + (v.cos(oppositeOfShipHeading.heading()) * NAMELINE_OFFSET), screenLocation.y + oppositeOfShipHeading.y + (v.sin(oppositeOfShipHeading.heading()) * NAMELINE_OFFSET), screenLocation.x + oppositeOfShipHeading.x + (v.cos(oppositeOfShipHeading.heading()) * NAMELINE_OFFSET) + v.textWidth(target.getName()) + NAMELINE_PADDING, screenLocation.y + oppositeOfShipHeading.y + (v.sin(oppositeOfShipHeading.heading()) * NAMELINE_OFFSET));
					} else {
						v.line( screenLocation.x + oppositeOfShipHeading.x + (v.cos(oppositeOfShipHeading.heading()) * NAMELINE_OFFSET), screenLocation.y + oppositeOfShipHeading.y + (v.sin(oppositeOfShipHeading.heading()) * NAMELINE_OFFSET), screenLocation.x + oppositeOfShipHeading.x + (v.cos(oppositeOfShipHeading.heading()) * NAMELINE_OFFSET) - v.textWidth(target.getName()) - NAMELINE_PADDING, screenLocation.y + oppositeOfShipHeading.y + (v.sin(oppositeOfShipHeading.heading()) * NAMELINE_OFFSET));
					}

					// Write the target name
					v.color(target.getColor());
					if(v.cos(oppositeOfShipHeading.heading()) > 0) {
						v.textAlign(LEFT);
					} else {
						v.textAlign(RIGHT);
					}
					v.text(target.getName(), screenLocation.x + oppositeOfShipHeading.x + (v.cos(oppositeOfShipHeading.heading()) * (NAMELINE_OFFSET + 4)), screenLocation.y + oppositeOfShipHeading.y + (v.sin(oppositeOfShipHeading.heading()) * NAMELINE_OFFSET) - 2);
					v.popStyle();
				}
			}
		}
	}
}
