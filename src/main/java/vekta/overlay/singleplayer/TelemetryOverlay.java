package vekta.overlay.singleplayer;

import processing.core.PVector;
import vekta.Player;
import vekta.mission.Mission;
import vekta.mission.Objective;
import vekta.module.ModuleType;
import vekta.object.SpaceObject;
import vekta.object.Targeter;
import vekta.object.planet.Planet;
import vekta.object.ship.ModularShip;
import vekta.overlay.Overlay;

import java.util.List;

import static processing.core.PApplet.*;
import static processing.core.PConstants.CENTER;
import static processing.core.PConstants.LEFT;
import static vekta.Vekta.*;

public class TelemetryOverlay implements Overlay {
	private static final float EARTH_MASS = 5.9736e24F;
	private static final float SUN_MASS = 1.989e30F;

	private final Player player;

	private Targeter targeter;
	private String distString;

	public TelemetryOverlay(Player player) {
		this.player = player;

		updateUIInformation();
	}

	private void updateUIInformation() {
		targeter = ((Targeter)player.getShip().getModule(ModuleType.TARGET_COMPUTER));
		if(targeter != null) {
			SpaceObject target = targeter.getTarget();
			distString = String.valueOf(target != null
					? (float)round(player.getShip().getPosition().dist(target.getPosition()) * 100) / 100
					: 0);
		}
	}

	@Override
	public void render() {
		if(v.frameCount % 10 == 0) {
			updateUIInformation();
		}

		ModularShip ship = player.getShip();

		float dialHeight = v.height - 64;

		// Draw telemetry bar
		v.fill(0);
		v.stroke(UI_COLOR);
		v.rectMode(CORNERS);
		v.rect(-1, v.height - 130, v.width + 1, v.height + 1);
		v.rectMode(CENTER);

		// Ship heading/velocity indicators
		v.fill(UI_COLOR);
		PVector heading = ship.getHeading();
		PVector velocity = ship.getVelocity();
		if(velocity.magSq() == 0) {
			velocity.set(heading);
		}
		drawDial("Heading", ship.getHeading(), v.width - 370, dialHeight, UI_COLOR);
		drawDial("Velocity", velocity, v.width - 500, dialHeight, UI_COLOR);

		// Targeter information
		if(targeter != null) {
			SpaceObject target = targeter.getTarget();
			String targetString;
			if(target == null) {
				v.fill(100, 100, 100);
				v.stroke(UI_COLOR);
				targetString = "PLANET [1], SHIP [2], ASTEROID [3]";
			}
			else {
				float mass = (float)round(target.getMass()) / 1000;
				String unit = "tonnes";
				if(mass >= SUN_MASS) {
					mass = (float)round(target.getMass() / SUN_MASS * 1000) / 1000;
					unit = "Suns";
				}
				else if(mass >= EARTH_MASS) {
					mass = (float)round(target.getMass() / EARTH_MASS * 1000) / 1000;
					unit = "Earths";
				}
				if(target instanceof Planet) {
					Planet closestPlanet = (Planet)target;
					targetString = target.getName() + " - " + distString + " AU \nHabitable: " + (closestPlanet.isHabitable() ? "YES" : "NO") + "\nMass: " + mass + " " + unit;
				}
				else {
					targetString = target.getName() + " - " + distString + " AU \nSpeed: " + (float)round(target.getVelocity().mag() * 100) / 100 + "\nMass: " + mass + " " + unit;
				}
				// Closest object arrow
				drawDial("Direction", target.getPosition().sub(ship.getPosition()), 450, dialHeight, target.getColor());
				v.fill(target.getColor());
				v.stroke(target.getColor());
			}
			v.text("Target: " + targetString, 50, v.height - 100);

			// Draw mission interface
			Mission mission = player.getCurrentMission();
			if(mission != null) {
				List<Objective> objectives = mission.getObjectives();
				Objective current = mission.getCurrentObjective();
				SpaceObject objTarget = current.getSpaceObject();

				// Draw objective direction dial
				if(objTarget != null && objTarget != targeter.getTarget()) {
					drawDial("Objective", objTarget.getPosition().sub(ship.getPosition()), 600, dialHeight, objTarget.getColor());
					//				v.fill(objTarget.getColor());
				}

				// Draw mission/objective text
				v.fill(MISSION_COLOR);
				v.textSize(24);
				v.text(mission.getName(), 700, v.height - 90);
				for(int i = 0; i < objectives.size(); i++) {
					Objective objective = objectives.get(i);
					v.fill(objective.getStatus().getColor());
					v.textSize(16);
					v.text(objective.getDisplayText(), 700, v.height - 50 + i * 20);
				}
			}
		}
	}

	private void drawDial(String name, PVector info, float locX, float locY, int c) {
		v.strokeWeight(2);
		float radius = 50;
		v.fill(0);
		v.stroke(c);
		v.ellipse(locX, locY, radius, radius);
		v.fill(100, 100, 100);
		v.textAlign(CENTER);
		v.textSize(14);
		v.text(name, locX, locY + radius / 2);
		v.textAlign(LEFT);
		v.textSize(16);
		v.stroke(c);
		drawArrow(info, (int)(radius * .8), locX, locY);
		v.strokeWeight(1);
	}

	private void drawArrow(PVector heading, float length, float locX, float locY) {
		heading.normalize().mult(length);
		v.line(locX, locY, locX + heading.x, locY + heading.y);
		float angle = heading.heading();
		float x = cos(angle);
		float y = sin(angle);
		PVector endpoint = new PVector(x, y);
		PVector arms = endpoint.copy();
		endpoint.mult(length);
		arms.mult(length * .8F); // scale the arms to a certain length
		// render the arms
		v.line(locX + endpoint.x, locY + endpoint.y, locX + cos(angle - .3F) * (length * .8F), locY + sin(angle - .3F) * (length * .8F));
		v.line(locX + endpoint.x, locY + endpoint.y, locX + cos(angle + .3F) * (length * .8F), locY + sin(angle + .3F) * (length * .8F));
	}
}
