package vekta.object.module;

import vekta.object.Planet;
import vekta.object.Ship;
import vekta.object.SpaceObject;
import vekta.object.Targeter;

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
		switch(mode) {
		case NEAREST_PLANET:
			return obj instanceof Planet;
		case NEAREST_SHIP:
			return obj instanceof Ship && obj != ship;
		default:
			return false;
		}
	}

	@Override
	public boolean shouldResetTarget() {
		return mode != null;
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
	public void onUninstall() {
		onInstall(null);
	}

	@Override
	public void onKeyPress(Ship ship, char key) {
		switch(key) {
		case 't':
			selecting = true;
			break;
		case 'p':
			setMode(TargetingModule.TargetingMode.NEAREST_PLANET);
			break;
		case 'h':
			setMode(TargetingModule.TargetingMode.NEAREST_SHIP);
			break;
		}
	}

	public enum TargetingMode {
		NEAREST_PLANET,
		NEAREST_SHIP,
	}
}
