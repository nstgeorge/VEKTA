package vekta.module.station;

import vekta.Singleplayer;
import vekta.Vekta;
import vekta.context.World;
import vekta.module.GeneratorModule;
import vekta.module.Module;
import vekta.module.ModuleType;
import vekta.object.ship.ModularShip;
import vekta.object.ship.SpaceStation;

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
	public boolean isApplicable(ModularShip ship) {
		return ship instanceof SpaceStation;
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
		v.translate(-(getWidth() - 1) / 2F * tileSize, 0); // Fix center

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
		// Supports on

		// Solar panel details (bottom) - only drawn when zoomed in closer
		safeDrawSecondaryLine(0, (float)Math.floor(getHeight() / 2F) / 3F * tileSize, (getWidth() - 2) * tileSize, (float)Math.floor(getHeight() / 2F) / 3F * tileSize);
		safeDrawSecondaryLine(0, (float)Math.floor(getHeight() / 2F) / 3F * 2 * tileSize, (getWidth() - 2) * tileSize, (float)Math.floor(getHeight() / 2F) / 3F * 2 * tileSize);
		for(int i = 1; i < getWidth() * 2 - 4; i++) {
			safeDrawSecondaryLine((tileSize / 2 * i), 0, (tileSize / 2 * i), (float)Math.floor(getHeight() / 2F) * tileSize);
		}
		//Solar panel details (top) - only drawn when zoomed in closer
		safeDrawSecondaryLine(0, (float)(Math.ceil(getHeight() / 2F) + (Math.floor(getHeight() / 2F) / 3F)) * tileSize, (getWidth() - 2) * tileSize, (float)(Math.ceil(getHeight() / 2F) + (Math.floor(getHeight() / 2F) / 3F)) * tileSize);
		safeDrawSecondaryLine(0, (float)(Math.ceil(getHeight() / 2F) + (Math.floor(getHeight() / 2F) / 3F) * 2) * tileSize, (getWidth() - 2) * tileSize, (float)(Math.ceil(getHeight() / 2F) + (Math.floor(getHeight() / 2F) / 3F) * 2) * tileSize);
		for(int i = 1; i < getWidth() * 2 - 4; i++) {
			safeDrawSecondaryLine((tileSize / 2 * i), (float)(Math.ceil(getHeight() / 2F) + Math.floor(getHeight() / 2F))  * tileSize, (tileSize / 2 * i), (float)Math.ceil(getHeight() / 2F) * tileSize);
		}

		v.rectMode(CENTER);
	}

	private void safeDrawSecondaryLine(float x1, float y1, float x2, float y2) {
		if(Vekta.getContext() instanceof World) {
			Singleplayer world = (Singleplayer) Vekta.getWorld();
			world.drawSecondaryLine(x1, y1, x2, y2);
		} else {
			v.line(x1, y1, x2, y2);
		}
	}
}
