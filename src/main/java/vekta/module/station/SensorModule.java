package vekta.module.station;

import static processing.core.PConstants.CENTER;
import static processing.core.PConstants.CLOSE;
import static processing.core.PConstants.CORNERS;
import static vekta.Vekta.v;

import vekta.module.BaseModule;
import vekta.module.ModuleType;
import vekta.object.ship.ModularShip;
import vekta.object.ship.SpaceStation;
import vekta.util.InfoGroup;
import vekta.world.RenderLevel;

public class SensorModule implements ComponentModule {
	@Override
	public String getName() {
		return "Sensor Module";
	}

	@Override
	public ModuleType getType() {
		return ModuleType.SENSOR;
	}

	@Override
	public int getMass() {
		return 1000;
	}

	@Override
	public float getValueScale() {
		return 1;
	}

	@Override
	public boolean isBetter(BaseModule other) {
		return false;
	}

	@Override
	public boolean isApplicable(ModularShip ship) {
		return ship instanceof SpaceStation;
	}

	@Override
	public BaseModule createVariant() {
		return new SensorModule();
	}

	@Override
	public int getWidth() {
		return 1;
	}

	@Override
	public int getHeight() {
		return 1;
	}

	@Override
	public boolean hasAttachmentPoint(SpaceStation.Direction direction) {
		// return direction == SpaceStation.Direction.LEFT;
		return false;
	}

	@Override
	public void draw(RenderLevel dist, float tileSize) {
		v.rectMode(CORNERS);
		v.translate(-.5F * tileSize, -(getHeight() / 2F) * tileSize);
		v.beginShape();
		// Basic rectangle
		v.vertex(.1F * getWidth() * tileSize, 0);
		v.vertex(0, 0);
		v.vertex(0, getHeight() * tileSize);
		v.vertex(.1F * getWidth() * tileSize, getHeight() * tileSize);
		v.vertex(.1F * getWidth() * tileSize, 0);

		// Angle up to first sensor
		v.vertex(.2F * getWidth() * tileSize, .2F * getHeight() * tileSize);

		// First sensor
		v.vertex(.1F * getWidth() * tileSize, .2F * getHeight() * tileSize);
		v.vertex(.6F * getWidth() * tileSize, .2F * getHeight() * tileSize);
		v.vertex(.4F * getWidth() * tileSize, .2F * getHeight() * tileSize);
		v.vertex(.4F * getWidth() * tileSize, .3F * getHeight() * tileSize);
		v.vertex(.2F * getWidth() * tileSize, .4F * getHeight() * tileSize);
		v.vertex(.1F * getWidth() * tileSize, .4F * getHeight() * tileSize);

		// Middle area
		v.vertex(.2F * getWidth() * tileSize, .4F * getHeight() * tileSize);
		v.vertex(.2F * getWidth() * tileSize, .5F * getHeight() * tileSize);
		v.vertex(.1F * getWidth() * tileSize, .5F * getHeight() * tileSize);
		v.vertex(.2F * getWidth() * tileSize, .5F * getHeight() * tileSize);
		v.vertex(.2F * getWidth() * tileSize, .6F * getHeight() * tileSize);

		// Second sensor
		v.vertex(.1F * getWidth() * tileSize, .6F * getHeight() * tileSize);
		v.vertex(.4F * getWidth() * tileSize, .6F * getHeight() * tileSize);
		v.vertex(.4F * getWidth() * tileSize, .8F * getHeight() * tileSize);
		v.vertex(.1F * getWidth() * tileSize, .8F * getHeight() * tileSize);
		v.vertex(.2F * getWidth() * tileSize, .8F * getHeight() * tileSize);
		v.vertex(.1F * getWidth() * tileSize, 1F * getHeight() * tileSize);

		v.endShape(CLOSE);

		v.rectMode(CENTER);
	}

	@Override
	public void onInfo(InfoGroup info) {
		// TODO: perhaps add some sort of data collection functionality to SensorModule
		// info.addDescription("");
	}
}
