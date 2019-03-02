package vekta.terrain;

import vekta.Vekta;
import vekta.menu.Menu;
import vekta.object.PlayerShip;

import java.util.HashSet;
import java.util.Set;

/**
 * An abstract representation of planetary terrain.
 */
public abstract class Terrain {
	private final Set<String> properties = new HashSet<>();

	public Terrain() {

	}

	public Set<String> getProperties() {
		return properties;
	}

	public boolean has(String prop) {
		return getProperties().contains(prop);
	}

	public void add(String prop) {
		getProperties().add(prop);
	}

	public void remove(String prop) {
		getProperties().remove(prop);
	}
	
	protected boolean chance(double amount) {
		return Vekta.getInstance().random(1) < amount;
	}

	public abstract String getOverview();

	public abstract void setupLandingMenu(PlayerShip ship, Menu menu);
}
