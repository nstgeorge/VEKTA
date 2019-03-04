package vekta.object.module;

import vekta.object.SpaceStation;

import static vekta.Vekta.v;

public class SolarArrayModule extends GeneratorModule {
	public SolarArrayModule(float rate) {
		super(rate);
	}

	public float getRate() {
		// TODO: increase when closer to stars using Targeter
		return super.getRate();
	}

	@Override
	public String getName() {
		return "Solar Array v" + getRate();
	}

	@Override
	public ModuleType getType() {
		return ModuleType.UTILITY;
	}

	@Override
	public boolean isBetter(Module other) {
		return other instanceof SolarArrayModule && getRate() > ((SolarArrayModule)other).getRate();
	}

	@Override
	public Module getVariant() {
		return new SolarArrayModule(chooseInclusive(.5F, 5, .5F));
	}

	@Override
	public int getWidth() {
		return 3;
	}

	@Override
	public int getHeight() {
		return 2;
	}

	@Override
	public boolean hasAttachmentPoint(SpaceStation.Direction dir) {
		return dir == SpaceStation.Direction.LEFT;
	}

	@Override
	public void draw(float tileSize) {
		// TODO: draw proper solar array
		// Note: I switched the Singelplayer rect rendering mode to CENTER rather than CORNERS as before
		v.rect(tileSize, 0, tileSize * getWidth(), tileSize * getHeight());
	}
}
