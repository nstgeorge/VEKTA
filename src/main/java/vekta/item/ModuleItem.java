package vekta.item;

import vekta.module.Module;

public class ModuleItem extends Item {
	private final Module module;

	public ModuleItem(Module module) {
		super(module.getName(), ItemType.MODULE);
		
		this.module = module;
	}

	public Module getModule() {
		return module;
	}
}
