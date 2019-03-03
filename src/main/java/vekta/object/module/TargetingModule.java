package vekta.object.module;

import vekta.Vekta;
import vekta.context.Context;
import vekta.context.World;
import vekta.object.Planet;
import vekta.object.Ship;
import vekta.object.SpaceObject;
import vekta.object.Targeter;

public class TargetingModule extends ShipModule implements Targeter {

	private TargetingMode mode;
	private SpaceObject target;

	public TargetingMode getMode() {
		return mode;
	}

	public void setMode(TargetingMode mode) {
		this.mode = mode;
		setTarget(null);
		
		// Immediately update the target (except in menus)
		Context context = Vekta.getContext();
		if(context instanceof World) {
			((World)context).updateTargeters(getShip());
		}
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
	public boolean shouldUpdateTarget() {
		return target == null;
	}

	@Override
	public String getName() {
		return "Targeting Computer";
	}

	@Override
	public ModuleType getType() {
		return ModuleType.TARGET_COMPUTER;
	}

	@Override
	public boolean isBetter(Module other) {
		return false;
	}

	@Override
	public void onInstall() {
		setMode(null);
	}

	@Override
	public void onKeyPress(char key) {
		switch(key) {
		case 't':
			setMode(null);
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
