package vekta.ecosystem;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Species implements Serializable {
	private final String name;
	private final Species parent;

	private final Map<Species, Float> foodSources = new HashMap<>();

	public Species(String name, Species parent) {
		this.name = name;
		this.parent = parent;
	}

	public String getName() {
		return name;
	}

	public Species getParent() {
		return parent;
	}

	public boolean isKnown(Species species) {
		return foodSources.containsKey(species);
	}

	public float getFoodSourceWeight(Species species) {
		return foodSources.getOrDefault(species, 0F);
	}

	public void setFoodSource(Species species, float weight) {
		if(weight > 0) {
			foodSources.put(species, weight);
		}
		else if(weight < 0) {
			species.foodSources.put(this, -weight);
		}
	}
}
