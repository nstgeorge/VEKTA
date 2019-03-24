package vekta.item;

import vekta.InfoGroup;
import vekta.menu.Menu;
import vekta.module.Module;

public class ModuleItem extends Item {
	private final Module module;

	public ModuleItem(Module module) {
		this.module = module;
	}

	public Module getModule() {
		return module;
	}

	@Override
	public int getMass() {
		return getModule().getMass();
	}

	@Override
	public String getName() {
		return getModule().getName();
	}

	@Override
	public ItemType getType() {
		return ItemType.MODULE;
	}

	@Override
	public void onMenu(Menu menu) {
		getModule().onItemMenu(this, menu);
	}

	@Override
	public void onInfo(InfoGroup info) {
		getModule().onInfo(info);
	}
}
