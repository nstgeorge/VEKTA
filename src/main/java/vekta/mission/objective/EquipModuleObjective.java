package vekta.mission.objective;

import vekta.module.BaseModule;
import vekta.object.SpaceObject;

public class EquipModuleObjective extends Objective {
	private final BaseModule module;
	private final boolean exact;

	public EquipModuleObjective(BaseModule module) {
		this(module, false);
	}

	public EquipModuleObjective(BaseModule module, boolean exact) {
		this.module = module;
		this.exact = exact;
	}

	public BaseModule getModule() {
		return module;
	}

	public boolean isExact() {
		return exact;
	}

	@Override
	public String getName() {
		return "Equip " + getModule().getName() + (isExact() ? "" : " or similar");
	}

	@Override
	public SpaceObject getSpaceObject() {
		return null;
	}

	@Override
	public void onInstallModule(BaseModule module) {
		if (isExact() ? module == getModule() : getModule().getClass().isInstance(module)) {
			complete();
		}
	}
}
