package vekta.overlay.singleplayer;

import processing.core.PVector;
import vekta.mission.Mission;
import vekta.mission.objective.Objective;
import vekta.module.ModuleType;
import vekta.object.SpaceObject;
import vekta.object.Targeter;
import vekta.object.planet.TerrestrialPlanet;
import vekta.object.ship.ModularShip;
import vekta.overlay.Overlay;
import vekta.overlay.indicator.*;
import vekta.player.Player;

import static vekta.Vekta.*;

public class StatusOverlay implements Overlay {

	private static final float DIAL_HEIGHT = v.height * (14 / 15F);

	private static final int MIN_TEMP = 0;
	private static final int MAX_TEMP = 40;

	private final Player player;

	private Targeter targeter;
	private String distString;

	private final DialIndicator velocityDial;
	private final DialIndicator objectiveDial;

	private final StripCompassIndicator compass;

	private final MeterIndicator temperatureMeter;
	private final MeterIndicator energyMeter;

	public StatusOverlay(Player player) {
		this.player = player;
		ModularShip ship = player.getShip();

		// The locations for each dial are temporary until we get a better UI layout system working.

		velocityDial = new DialIndicator("Velocity", t -> ship.getVelocity(), v.width * (17 / 20F), DIAL_HEIGHT, UI_COLOR);
		// ObjectiveDial's value function is temporarily set to return a zero vector here. It is updated when a player finds an objective.
		objectiveDial = new DialIndicator("Objective", t -> new PVector(), v.width / 2F, DIAL_HEIGHT, UI_COLOR);
		compass = new StripCompassIndicator("Compass", t -> ship.getHeading(), v.width / 2F, 40, UI_COLOR);
		temperatureMeter = new MeterIndicator("Temp", MeterIndicator.TYPE.VERTICAL, t -> ship.getTemperatureKelvin(), MIN_TEMP, MAX_TEMP, v.width * (39 / 40F), DIAL_HEIGHT, v.height / 20F, v.height / 80F, UI_COLOR);
		energyMeter = new MeterIndicator("Energy", MeterIndicator.TYPE.RADIAL, t -> ship.getEnergy(), 0, player.getShip().getMaxEnergy(), v.width * (37 / 40F), DIAL_HEIGHT, v.height / 20F, v.height / 90F, UI_COLOR);

		updateUIInformation();
	}

	private void updateUIInformation() {
		targeter = ((Targeter)player.getShip().getModule(ModuleType.NAVIGATION));
		if(targeter != null && targeter.getTarget() != null) {
			SpaceObject target = targeter.getTarget();
			distString = distanceString(player.getShip().relativePosition(target).mag());
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

		// Ship velocity indicator
		v.fill(UI_COLOR);
		PVector velocity = ship.getVelocity();
		if(velocity.magSq() >= 1) {
			velocityDial.draw();
		}

		// Compass
		compass.draw();

		// Temperature
		temperatureMeter.draw();

		// Energy
		energyMeter.setMax(ship.getMaxEnergy());
		energyMeter.draw();

		// Targeter information
		if(targeter != null) {
			SpaceObject target = targeter.getTarget();
			String targetString;
			if(target == null) {
				v.fill(100, 100, 100);
				v.stroke(UI_COLOR);
				// TODO: change depending on key binding
				targetString = "PLANET [1], ASTEROID [2], SHIP [3]" + (player.getCurrentMission() != null ? ", OBJECTIVE [4]" : "");
			}
			else {
				if(target instanceof TerrestrialPlanet) {
					TerrestrialPlanet planet = (TerrestrialPlanet)target;
					targetString = target.getName() + " - " + distString
							+ "\nMass: " + massString(target.getMass())
							+ "\nTemperature: " + temperatureStringCelsius(v.roundEpsilon(planet.getTemperatureCelsius()))
							+ "\nAtmosphere: " + atmosphereString(planet.getAtmosphereDensity());

					SpaceObject orbit = planet.getOrbitObject();
					if(orbit != null) {
						targetString += "\nOrbiting: " + orbit.getName() + " (" + distanceString(planet.relativePosition(orbit).mag()) + ")";
					}
				}
				else {
					targetString = target.getName() + " - " + distString
							+ " \n\nMass: " + massString(target.getMass());
				}

				// Closest object arrow
				//drawDial("Direction", target.getPosition().sub(ship.getPosition()), 450, dialHeight, target.getColor());
				v.fill(target.getColor());
				v.stroke(target.getColor());
			}
			v.text("Target: " + targetString, 50, v.height - 100);

			// Draw mission objective
			Mission mission = player.getCurrentMission();
			if(mission != null) {
				Objective current = mission.getCurrentObjective();
				SpaceObject objTarget = current.getSpaceObject();
				// Draw objective direction dial
				if(objTarget != null && objTarget != targeter.getTarget()) {
					objectiveDial.setValue(t -> objTarget.getPosition().sub(ship.getPosition()));
					objectiveDial.setColor(objTarget.getColor());
					objectiveDial.draw();
				}
			}
		}
	}
}
