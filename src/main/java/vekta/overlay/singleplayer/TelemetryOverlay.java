package vekta.overlay.singleplayer;

import processing.core.PVector;
import vekta.Vekta;
import vekta.object.Planet;
import vekta.object.PlayerShip;
import vekta.object.SpaceObject;
import vekta.object.Targeter;
import vekta.object.module.ModuleType;
import vekta.overlay.Overlay;

import static processing.core.PApplet.*;
import static processing.core.PConstants.CENTER;
import static processing.core.PConstants.LEFT;
import static vekta.Vekta.UI_COLOR;
import static vekta.Vekta.getInstance;

public class TelemetryOverlay implements Overlay {
	private final PlayerShip ship;

	private Targeter targeter;
	private String distString;

	public TelemetryOverlay(PlayerShip ship) {
		this.ship = ship;
	}

	private void updateUIInformation() {
		targeter = ((Targeter)ship.getBestModule(ModuleType.TARGET_COMPUTER));

		if(targeter != null) {
			SpaceObject target = targeter.getTarget();
			distString = String.valueOf(target != null
					? (float)round(ship.getPosition().dist(target.getPosition()) * 100) / 100
					: 0);
		}
	}

	@Override
	public void draw() {
		Vekta v = getInstance();

		if(v.frameCount % 10 == 0) {
			updateUIInformation();
		}

		// Draw telemetry bar
		v.fill(0);
		v.stroke(UI_COLOR);
		v.rectMode(CORNERS);
		v.rect(-1, v.height - 130, v.width + 1, v.height + 1);

		// Ship heading/velocity indicators
		v.fill(UI_COLOR);
		drawDial("Heading", ship.getHeading(), v.width - 370, v.height - 65, v.color(0, 255, 0));
		drawDial("Velocity", ship.getVelocity().copy(), v.width - 500, v.height - 65, v.color(0, 255, 0));

		// Targeter information
		if(targeter != null) {
			SpaceObject target = targeter.getTarget();
			String targetString;
			if(target == null) {
				v.fill(100, 100, 100);
				v.stroke(UI_COLOR);
				targetString = "planet [1], ship [2]";
			}
			else {
				float mass;
				String unit;
				if(target.getMass() / 1.989e30 < .1) {
					mass = (float)round(target.getMass() / 5.9736e24F * 1000) / 1000;
					unit = "Earths";
				}
				else {
					mass = (float)round(target.getMass() / 1.989e30F * 1000) / 1000;
					unit = "Suns";
				}
				if(target instanceof Planet) {
					Planet closestPlanet = (Planet)target;
					targetString = target.getName() + " - " + distString + " AU \nHabitable: " + (closestPlanet.isHabitable() ? "YES" : "NO") + "\nMass: " + mass + " " + unit;
				}
				else {
					targetString = target.getName() + " - " + distString + " AU \nSpeed: " + (float)round(target.getVelocity().mag() * 100) / 100 + "\nMass: " + mass + " " + unit;
				}
				// Closest object arrow
				drawDial("Direction", target.getPosition().sub(ship.getPosition()), 450, v.height - 65, target.getColor());
				v.fill(target.getColor());
				v.stroke(target.getColor());
			}
			v.text("Target: " + targetString, 50, v.height - 100);
		}
	}

	private void drawDial(String name, PVector info, int locX, int locY, int c) {
		int radius = 50;
		Vekta v = getInstance();
		v.fill(0);
		v.stroke(c);
		v.ellipse(locX, locY, radius, radius);
		v.fill(100, 100, 100);
		v.textAlign(CENTER);
		v.textSize(14);
		v.text(name, locX, locY + 25);
		v.textAlign(LEFT);
		v.textSize(16);
		v.stroke(c);
		drawArrow(info, (int)(radius * .8), locX, locY);
	}

	private void drawArrow(PVector heading, int length, int locX, int locY) {
		Vekta v = Vekta.getInstance();
		heading.normalize().mult(length);
		v.line(locX, locY, locX + heading.x, locY + heading.y);
		float angle = heading.heading();
		float x = cos(angle);
		float y = sin(angle);
		PVector endpoint = new PVector(x, y);
		PVector arms = endpoint.copy();
		endpoint.mult(length);
		arms.mult(length * .8F); // scale the arms to a certain length
		// draw the arms
		v.line(locX + endpoint.x, locY + endpoint.y, locX + cos(angle - .3F) * (length * .8F), locY + sin(angle - .3F) * (length * .8F));
		v.line(locX + endpoint.x, locY + endpoint.y, locX + cos(angle + .3F) * (length * .8F), locY + sin(angle + .3F) * (length * .8F));
	}
}
