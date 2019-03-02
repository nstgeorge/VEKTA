package vekta.overlay.singleplayer;

import vekta.object.PlayerShip;
import vekta.overlay.TextOverlay;

import static vekta.Vekta.UI_COLOR;

/**
 * Ship stats (energy, money, etc.)
 */
public abstract class ShipStatOverlay extends TextOverlay {
	private final PlayerShip ship;

	public ShipStatOverlay(int x, int y, PlayerShip ship) {
		super(x, y);

		this.ship = ship;
	}

	public PlayerShip getShip() {
		return ship;
	}

	@Override
	public String getText() {
		return getName() + " = " + getValue();
	}

	@Override
	public int getColor() {
		return UI_COLOR;
	}

	public abstract String getName();

	public abstract String getValue();
}
