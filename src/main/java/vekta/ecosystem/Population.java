package vekta.ecosystem;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static processing.core.PApplet.max;

public class Population implements Serializable {
	private final Map<Species, Integer> amounts = new HashMap<>();

	public int count(Species species) {
		return amounts.getOrDefault(species, 0);
	}

	public void add(Species species, int amount) {
		amounts.put(species, max(0, count(species) + amount));
	}

	public void remove(Species species, int amount) {
		add(species, -amount);
	}

	public void updateEpoch() {

	}
}
