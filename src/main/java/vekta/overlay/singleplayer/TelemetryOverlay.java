package vekta.overlay.singleplayer;

import processing.core.PVector;
import vekta.Player;
import vekta.mission.Mission;
import vekta.mission.objective.Objective;
import vekta.module.ModuleType;
import vekta.object.SpaceObject;
import vekta.object.Targeter;
import vekta.object.planet.TerrestrialPlanet;
import vekta.object.ship.ModularShip;
import vekta.overlay.Overlay;

import static processing.core.PApplet.*;
import static processing.core.PConstants.CENTER;
import static processing.core.PConstants.LEFT;
import static vekta.Vekta.*;

public class TelemetryOverlay implements Overlay {
	private final Player player;

	private Targeter targeter;
	private String distString;

	public TelemetryOverlay(Player player) {
		this.player = player;

		updateUIInformation();
	}

	private void updateUIInformation() {
		targeter = ((Targeter)player.getShip().getModule(ModuleType.TARGET_COMPUTER));
		if(targeter != null && targeter.getTarget() != null) {
			SpaceObject target = targeter.getTarget();

			float dist = player.getShip().getPosition().dist(target.getPosition());
			String unit = "meters";
			if(dist > AU_DISTANCE * .01F) {
				dist /= AU_DISTANCE;
				unit = "AU";
			}
			else if(dist > 100) {
				dist /= 1000;
				unit = "km";
			}
			distString = ((float)round(dist * 100) / 100) + " " + unit;
		}
		else {
			distString = "--";
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
				targetString = "PLANET [1], SHIP [2], ASTEROID [3]" + (player.getCurrentMission() != null ? ", OBJECTIVE [4]" : "");
			}
			else {
				float mass = (float)round(target.getMass() * 1000) / 1000;
				String unit = "tonnes";
				if(mass >= SUN_MASS * .1F) {
					mass = (float)round(target.getMass() / SUN_MASS * 1000) / 1000;
					unit = "Suns";
				}
				else if(mass >= EARTH_MASS * .1F) {
					mass = (float)round(target.getMass() / EARTH_MASS * 1000) / 1000;
					unit = "Earths";
				}
				if(target instanceof TerrestrialPlanet) {
					TerrestrialPlanet closestPlanet = (TerrestrialPlanet)target;
					targetString = target.getName() + " - " + distString + " \nHabitable: " + (closestPlanet.isHabitable() ? "YES" : "NO") + "\nMass: " + mass + " " + unit;
				}
				else {
					targetString = target.getName() + " - " + distString + " \nSpeed: " + (float)round(target.getVelocity().mag() * 100) / 100 + "\nMass: " + mass + " " + unit;
				}
				// Closest object arrow
				drawDial("Direction", target.getPosition().sub(ship.getPosition()), 450, dialHeight, target.getColor());
				v.fill(target.getColor());
				v.stroke(target.getColor());

				//				println(ship.getPosition().div(SCALE), target.getPosition().div(SCALE));////
			}
			v.text("Target: " + targetString, 50, v.height - 100);

			// Draw mission objective
			Mission mission = player.getCurrentMission();
			if(mission != null) {
				Objective current = mission.getCurrentObjective();
				SpaceObject objTarget = current.getSpaceObject();
				// Draw objective direction dial
				if(objTarget != null && objTarget != targeter.getTarget()) {
					drawDial("Objective", objTarget.getPosition().sub(ship.getPosition()), v.width / 2F, dialHeight, objTarget.getColor());
				}
			}
		}
	}

	private void drawDial(String name, PVector info, float locX, float locY, int c) {
		v.strokeWeight(2);
		float radius = 50;
		v.noFill();
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
		arms.mult(length * .9F); // scale the arms to a certain length
		// render the arms
		v.line(locX + endpoint.x, locY + endpoint.y, locX + cos(angle - .3F) * (length * .8F), locY + sin(angle - .3F) * (length * .8F));
		v.line(locX + endpoint.x, locY + endpoint.y, locX + cos(angle + .3F) * (length * .8F), locY + sin(angle + .3F) * (length * .8F));
	}
}
