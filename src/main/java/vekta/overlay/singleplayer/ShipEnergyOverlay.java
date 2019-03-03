package vekta.overlay.singleplayer;

import vekta.object.PlayerShip;

import static vekta.Vekta.getInstance;

/**
 * Ship stats (energy, money, etc.)
 */
public class ShipEnergyOverlay extends ShipStatOverlay {
	public ShipEnergyOverlay(int x, int y, PlayerShip ship) {
		super(x, y, ship);
	}

	@Override
	public String getName() {
		return "Energy";
	}

	@Override
	public String getValue() {
		float energy = getShip().getEnergy();
		float max = getShip().getMaxEnergy();
		return (int)energy + " / " + (int)max + " (" + (int)(energy / max * 100) + "%)";
	}

	@Override
	public int getColor() {
		float ratio = getShip().getEnergy() / getShip().getMaxEnergy();
		if(ratio <= .1F) {
			return getInstance().color(255, 0, 0);
		}
		else if(ratio <= .2F) {
			return getInstance().color(255, 255, 0);
		}
		return super.getColor();
	}
}
