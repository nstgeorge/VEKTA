package vekta.module.station;

import vekta.InfoGroup;
import vekta.RenderLevel;
import vekta.module.Module;
import vekta.module.ModuleType;
import vekta.module.ShipModule;
import vekta.object.ship.ModularShip;
import vekta.object.ship.SpaceStation;

import static processing.core.PConstants.CENTER;
import static processing.core.PConstants.CORNERS;
import static vekta.Vekta.v;

public class StructuralModule extends ShipModule {
	private final int width, height;

	public StructuralModule() {
		this(1, 1);
	}

	public StructuralModule(int width, int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public String getName() {
		return "Structural Beam " + getWidth() + "x" + getHeight();
	}

	@Override
	public ModuleType getType() {
		return ModuleType.STRUCTURAL;
	}

	@Override
	public int getMass() {
		return 500;
	}

	@Override
	public boolean isBetter(Module other) {
		return false;
	}

	@Override
	public Module createVariant() {
		return new StructuralModule(chooseInclusive(1, 3), chooseInclusive(3, 10));
	}

	@Override
	public boolean isApplicable(ModularShip ship) {
		return ship instanceof SpaceStation;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public void draw(RenderLevel dist, float tileSize) {

		v.rectMode(CORNERS);
		v.fill(0);
		v.translate(-(getWidth() / 2F) * tileSize, -(getHeight() / 2F) * tileSize);

		// Cross supports
		for(int i = 0; i < getWidth(); i++) {
			v.line(i * tileSize, 2, (i + 1) * tileSize, getHeight() * tileSize - 2);
			v.line(i * tileSize, getHeight() * tileSize - 2, (i + 1) * tileSize, 2);
		}

		// Endcaps
		v.rect(0, 0, .1F * tileSize, getHeight() * tileSize);
		v.rect(getWidth() * tileSize, 0, (getWidth() - .1F) * tileSize, getHeight() * tileSize);

		// Long supports
		v.rect(0, 0, getWidth() * tileSize, (getHeight() * .1F) * tileSize);
		v.rect(0, getHeight() * tileSize, getWidth() * tileSize, (getHeight() - (getHeight() * .1F)) * tileSize);

		v.rectMode(CENTER);
	}

	@Override
	public void onInfo(InfoGroup info) {
		info.addDescription("If you need some extra space on your station, this is the module for you.");
	}
}
