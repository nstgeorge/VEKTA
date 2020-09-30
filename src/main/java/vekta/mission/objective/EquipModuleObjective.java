package vekta.mission.objective;

import vekta.module.Module;
import vekta.object.SpaceObject;

public class EquipModuleObjective extends Objective {
	private final Module module;
	private final boolean exact;

	public EquipModuleObjective(Module module) {
		this(module, false);
	}

	public EquipModuleObjective(Module module, boolean exact) {
		this.module = module;
		this.exact = exact;
	}

	public Module getModule() {
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
	public void onInstallModule(Module module) {
		if(isExact() ? module == getModule() : getModule().getClass().isInstance(module)) {
			complete();
		}
	}
}
