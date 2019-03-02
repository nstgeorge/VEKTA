package vekta.object.module;

import vekta.object.Planet;
import vekta.object.Ship;
import vekta.object.SpaceObject;
import vekta.object.Targeter;

import static vekta.Vekta.*;

public class TargetingModule implements Module, Targeter {
	private static boolean selecting;

	/**
	 * Static access to target selection for UI
	 */
	public static boolean isUsingTargeter() {
		return selecting;
	}

	private Ship ship; // TODO: add a parent class for stateful modules
	private TargetingMode mode;
	private SpaceObject target;

	public TargetingModule() {

	}

	public TargetingMode getMode() {
		return mode;
	}

	public void setMode(TargetingMode mode) {
		this.mode = mode;
		selecting = false;
		getWorld().updateTargeters(ship);
	}

	@Override
	public SpaceObject getTarget() {
		return target;
	}

	@Override
	public void setTarget(SpaceObject target) {
		this.target = target;
	}

	@Override
	public boolean isValidTarget(SpaceObject obj) {
		if(ship.isLanding()) {
			return obj instanceof Planet || obj instanceof Ship;
		}
		if(mode == null) {
			return false;
		}

		switch(mode) {
		case PLANET:
			return obj instanceof Planet;
		case SHIP:
			return obj instanceof Ship;
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
		return ModuleType.TARGETING_COMPUTER;
	}

	@Override
	public boolean isBetter(Module other) {
		return false;
	}

	@Override
	public void onInstall(Ship ship) {
		this.ship = ship;
		setMode(null);
	}

	@Override
	public void onUninstall(Ship ship) {
		onInstall(null);
	}

	@Override
	public void onKeyPress(Ship ship, char key) {
		switch(key) {
		case 't':
			setMode(null);
			target = null;
			selecting = true;
			break;
		case '1':
			setMode(TargetingModule.TargetingMode.PLANET);
			break;
		case '2':
			setMode(TargetingModule.TargetingMode.SHIP);
			break;
		}
	}

	public enum TargetingMode {
		PLANET,
		SHIP,
	}
}
