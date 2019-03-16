package vekta.module;

import vekta.menu.Menu;
import vekta.menu.handle.ObjectMenuHandle;
import vekta.menu.option.InternetMenuOption;

import static vekta.Vekta.AU_DISTANCE;

public class AntennaModule extends ShipModule {
	private static final float RANGE_SCALE = AU_DISTANCE;

	private final float range;

	public AntennaModule() {
		this(1);
	}

	public AntennaModule(float range) {
		this.range = range;
	}

	public float getRange() {
		return range;
	}

	@Override
	public String getName() {
		return "Internet Antenna v" + getRange();
	}

	@Override
	public ModuleType getType() {
		return ModuleType.ANTENNA;
	}

	@Override
	public boolean isBetter(Module other) {
		return other instanceof AntennaModule && getRange() > ((AntennaModule)other).getRange();
	}

	@Override
	public Module getVariant() {
		return new AntennaModule(chooseInclusive(.5F, 10, .5F));
	}

	@Override
	public void onMenu(Menu menu) {
		if(menu.getHandle() instanceof ObjectMenuHandle && ((ObjectMenuHandle)menu.getHandle()).getSpaceObject() == getShip()) {
			boolean connected = true; // TODO: determine whether in connection range
			menu.add(new InternetMenuOption(menu.getPlayer(), connected));
		}
	}
}
