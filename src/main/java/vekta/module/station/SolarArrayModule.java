package vekta.module.station;

import static processing.core.PConstants.CENTER;
import static processing.core.PConstants.CLOSE;
import static processing.core.PConstants.CORNERS;
import static vekta.Vekta.v;

import vekta.module.BaseModule;
import vekta.module.GeneratorModule;
import vekta.module.ModuleType;
import vekta.object.ship.ModularShip;
import vekta.object.ship.SpaceStation;
import vekta.util.InfoGroup;
import vekta.world.RenderLevel;

public class SolarArrayModule extends GeneratorModule {

	public SolarArrayModule() {
		this(1);
	}

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
	public int getMass() {
		return 15000;
	}

	@Override
	public boolean isBetter(BaseModule other) {
		return other instanceof SolarArrayModule && getRate() > ((SolarArrayModule) other).getRate();
	}

	@Override
	public BaseModule createVariant() {
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
		// return dir == SpaceStation.Direction.LEFT;
		return false;
	}

	@Override
	public void draw(RenderLevel dist, float tileSize) {
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
		v.vertex(-.3F * tileSize, ((float) Math.ceil(getHeight() / 2.0) + .2F) * tileSize);
		v.vertex(0, ((float) Math.ceil(getHeight() / 2.0) + .2F) * tileSize);
		v.vertex(0, ((float) Math.ceil(getHeight() / 2.0) * tileSize));
		v.vertex(-.3F * tileSize, ((float) Math.ceil(getHeight() / 2.0) * tileSize));
		v.vertex(-.3F * tileSize, ((float) Math.floor(getHeight() / 2.0) * tileSize));
		v.vertex(0, ((float) Math.floor(getHeight() / 2.0) * tileSize));
		v.vertex(0, ((float) Math.floor(getHeight() / 2.0) - .2F) * tileSize);
		v.vertex(-.3F * tileSize, ((float) Math.floor(getHeight() / 2.0) - .2F) * tileSize);
		v.vertex(-.3F * tileSize, .2F * tileSize);
		v.vertex(0, .2F * tileSize);
		v.endShape(CLOSE);

		// Middle connection point
		v.fill(0);
		v.rect(-.7F * tileSize, (getHeight() / 2F - .25F) * tileSize, .5F, (getHeight() / 2F + .25F) * tileSize);

		// Panel outlines
		v.rect(0, 0, (getWidth() - 2) * tileSize, (float) Math.floor(getHeight() / 2.0) * tileSize);
		v.rect(0, getHeight() * tileSize, (getWidth() - 2) * tileSize, (float) Math.ceil(getHeight() / 2.0) * tileSize);
		// Supports on

		if (dist == RenderLevel.PARTICLE) {
			// Solar panel details (bottom) - only drawn when zoomed in closer
			v.line(0, (float) Math.floor(getHeight() / 2F) / 3F * tileSize, (getWidth() - 2) * tileSize,
					(float) Math.floor(getHeight() / 2F) / 3F * tileSize);
			v.line(0, (float) Math.floor(getHeight() / 2F) / 3F * 2 * tileSize, (getWidth() - 2) * tileSize,
					(float) Math.floor(getHeight() / 2F) / 3F * 2 * tileSize);
			for (int i = 1; i < getWidth() * 2 - 4; i++) {
				v.line((tileSize / 2 * i), 0, (tileSize / 2 * i), (float) Math.floor(getHeight() / 2F) * tileSize);
			}
			// Solar panel details (top) - only drawn when zoomed in closer
			v.line(0, (float) (Math.ceil(getHeight() / 2F) + (Math.floor(getHeight() / 2F) / 3F)) * tileSize,
					(getWidth() - 2) * tileSize,
					(float) (Math.ceil(getHeight() / 2F) + (Math.floor(getHeight() / 2F) / 3F)) * tileSize);
			v.line(0, (float) (Math.ceil(getHeight() / 2F) + (Math.floor(getHeight() / 2F) / 3F) * 2) * tileSize,
					(getWidth() - 2) * tileSize,
					(float) (Math.ceil(getHeight() / 2F) + (Math.floor(getHeight() / 2F) / 3F) * 2) * tileSize);
			for (int i = 1; i < getWidth() * 2 - 4; i++) {
				v.line((tileSize / 2 * i), (float) (Math.ceil(getHeight() / 2F) + Math.floor(getHeight() / 2F)) * tileSize,
						(tileSize / 2 * i), (float) Math.ceil(getHeight() / 2F) * tileSize);
			}
		}

		v.rectMode(CENTER);
	}

	@Override
	public void onInfo(InfoGroup info) {
		info.addDescription("Rule #1 of space exploration: if the nukes aren't quick enough, go solar.");
	}
}
