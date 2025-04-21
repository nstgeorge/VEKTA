package vekta.item;

import static processing.core.PApplet.round;
import static vekta.Vekta.v;

import vekta.menu.Menu;
import vekta.module.BaseModule;
import vekta.util.InfoGroup;

public class ModuleItem extends Item {
	private final BaseModule module;

	public ModuleItem(BaseModule module) {
		this.module = module;
	}

	public BaseModule getModule() {
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
	public int randomPrice() {
		return round((super.randomPrice() + v.random(-1, 1)) * getModule().getValueScale());
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
