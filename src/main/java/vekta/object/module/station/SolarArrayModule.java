package vekta.object.module.station;

import vekta.object.SpaceStation;
import vekta.object.module.GeneratorModule;
import vekta.object.module.Module;
import vekta.object.module.ModuleType;

import static processing.core.PConstants.*;
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
		return 10;
	}

	@Override
	public int getHeight() {
		return 5;
	}

	@Override
	public boolean hasAttachmentPoint(SpaceStation.Direction dir) {
		return dir == SpaceStation.Direction.LEFT;
	}

	@Override
	public void draw(float tileSize) {
		v.translate(-(getWidth() - 1) / 2 * tileSize, 0); // Fix center

		// Supports on station side of array
		v.rectMode(CORNERS);
		// For easier coordinates
		v.translate(.2F * tileSize, -(getHeight() / 2F) * tileSize);
		v.beginShape();
		// Flat side
		v.vertex(0, 0);
		v.vertex(-.5F * tileSize, 0);
		v.vertex(-.5F * tileSize, getHeight() * tileSize);
		v.vertex(0, getHeight() * tileSize);
		// Array-facing side
		v.vertex(0, (getHeight() - .2F) * tileSize);
		v.vertex(-.3F * tileSize, (getHeight() - .2F) * tileSize);
		v.vertex(-.3F * tileSize, ((float)Math.ceil(getHeight() / 2.0) + .2F) * tileSize);
		v.vertex(0, ((float)Math.ceil(getHeight() / 2.0) + .2F) * tileSize);
		v.vertex(0, ((float)Math.ceil(getHeight() / 2.0) * tileSize));
		v.vertex(-.3F * tileSize, ((float)Math.ceil(getHeight() / 2.0) * tileSize));
		v.vertex(-.3F * tileSize, ((float)Math.floor(getHeight() / 2.0) * tileSize));
		v.vertex(0, ((float)Math.floor(getHeight() / 2.0) * tileSize));
		v.vertex(0, ((float)Math.floor(getHeight() / 2.0) - .2F) * tileSize);
		v.vertex(-.3F * tileSize, ((float)Math.floor(getHeight() / 2.0) - .2F) * tileSize);
		v.vertex(-.3F * tileSize, .2F * tileSize);
		v.vertex(0, .2F * tileSize);
		v.endShape(CLOSE);
		// Middle connection point
		v.fill(0);
		v.rect(-.7F * tileSize, (getHeight() / 2F - .25F) * tileSize, .5F, (getHeight() / 2F + .25F) * tileSize);
		// Panel outlines
		v.rect(0, 0, (getWidth() - 2) * tileSize, (float)Math.floor(getHeight() / 2.0) * tileSize);
		v.rect(0, getHeight() * tileSize, (getWidth() - 2) * tileSize, (float)Math.ceil(getHeight() / 2.0) * tileSize);
		// Solar panels (top)
		//		v.beginShape(QUAD_STRIP);
		//		for(int i = 0; i < getWidth() * 2 - 4; i++) {
		//			v.vertex(i * tileSize,			0);
		//			v.vertex(i * tileSize,			tileSize);
		//			v.vertex((i - .5F) * tileSize,	tileSize);
		//		}
		//		v.endShape();
		v.rectMode(CENTER);
	}
}
