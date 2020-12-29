package vekta.module;

import vekta.KeyBinding;
import vekta.Resources;
import vekta.knowledge.ObservationLevel;
import vekta.mission.Mission;
import vekta.object.CargoCrate;
import vekta.object.RingDebris;
import vekta.object.SpaceObject;
import vekta.object.Targeter;
import vekta.object.planet.Asteroid;
import vekta.object.planet.TerrestrialPlanet;
import vekta.object.ship.Ship;
import vekta.spawner.WorldGenerator;
import vekta.util.InfoGroup;
import vekta.world.RenderLevel;

import static processing.core.PApplet.*;
import static vekta.Vekta.getWorld;

public class TargetingModule extends ShipModule implements Targeter {
	private static final float AUTO_ZOOM_POWER = 3;
	private static final float AUTO_ZOOM_SCALE = 2;

	private TargetingMode mode;
	private SpaceObject target;

	private SpaceObject prevTarget; // Target debounce

	public TargetingMode getMode() {
		return mode;
	}

	public void setMode(TargetingMode mode) {
		this.mode = mode;
		setTarget(null);

		// Immediately update ship's targeting instruments
		getShip().updateTargets();
	}

	@Override
	public SpaceObject getSpaceObject() {
		return getShip();
	}

	@Override
	public SpaceObject getTarget() {
		return target;
	}

	@Override
	public void setTarget(SpaceObject target) {
		if(this.target != null) {
			prevTarget = this.target;
		}
		this.target = target;

		if(target != null && target != prevTarget) {
			Resources.playSound("targetChange");

			if(getShip().hasController()) {
				target.observe(ObservationLevel.AWARE, getShip().getController());
			}
		}
	}

	@Override
	public boolean isValidTarget(SpaceObject obj) {
		if(mode == null) {
			return obj == getTarget() && !obj.isDestroyed(); // Don't set targeter unless destroyed
		}
		switch(mode) {
		case PLANET:
			return obj instanceof TerrestrialPlanet && !(obj instanceof Asteroid);
		case ASTEROID:
			return obj instanceof Asteroid || obj instanceof RingDebris;
		case SHIP:
			return obj instanceof Ship || obj instanceof CargoCrate;
		default:
			return false;
		}
	}

	@Override
	public String getName() {
		return "Targeting Computer";
	}

	@Override
	public ModuleType getType() {
		return ModuleType.NAVIGATION;
	}

	@Override
	public int getMass() {
		return 500;
	}

	@Override
	public float getValueScale() {
		return 1;
	}

	@Override
	public boolean isBetter(Module other) {
		return false;
	}

	@Override
	public Module createVariant() {
		return new TargetingModule();
	}

	@Override
	public void onInstall() {
		setMode(null);
	}

	@Override
	public void onUpdate() {
		if(target != null && getShip().isLanding()) {
			getWorld().setAutoZoom(getTargetZoom());
		}
	}

	public float getTargetZoom() {
		if(target != null) {
			float distSq = target.relativePosition(getShip()).magSq();
			if(!RenderLevel.SHIP.isVisibleTo(getWorld().getRenderLevel()) || distSq > sq(WorldGenerator.getRadius(RenderLevel.SHIP))) {
				return pow(distSq, 1 / AUTO_ZOOM_POWER) * AUTO_ZOOM_SCALE;
			}
		}
		return getWorld().getZoom();
	}

	@Override
	public void onKeyPress(KeyBinding key) {
		switch(key) {
		case SHIP_LAND:
			getWorld().setZoom(getTargetZoom());
			break;
		case SHIP_TARGET:
			setMode(null);
			break;
		case SHIP_TARGET_PLANET:
			setMode(TargetingModule.TargetingMode.PLANET);
			break;
		case SHIP_TARGET_ASTEROID:
			setMode(TargetingModule.TargetingMode.ASTEROID);
			break;
		case SHIP_TARGET_SHIP:
			setMode(TargetingModule.TargetingMode.SHIP);
			break;
		case SHIP_TARGET_OBJECTIVE:
			setMode(null);
			if(getShip().hasController()) {
				Mission mission = getShip().getController().getCurrentMission();
				if(mission != null) {
					setTarget(mission.getCurrentObjective().getSpaceObject());
				}
			}
			break;
		}
	}

	@Override
	public void onInfo(InfoGroup info) {
		info.addDescription("Accurate targeting is an essential feature for every modern spacecraft.");

		info.addKey(KeyBinding.SHIP_TARGET, "reset target");
		info.addKey(KeyBinding.SHIP_TARGET_PLANET, "target nearest planet");
		info.addKey(KeyBinding.SHIP_TARGET_ASTEROID, "target nearest asteroid");
		info.addKey(KeyBinding.SHIP_TARGET_SHIP, "target nearest ship");
	}

	public enum TargetingMode {
		PLANET,
		ASTEROID,
		SHIP,
	}
}
