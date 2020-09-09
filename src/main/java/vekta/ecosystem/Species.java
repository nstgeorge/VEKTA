package vekta.ecosystem;

import vekta.util.InfoGroup;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Species implements Serializable {
	private final Species ancestor;
	private final String name;

	private float naturalGrowth;
	private float size = 1;

	private final Map<Species, Float> foodSources = new HashMap<>();

	public Species(String name) {
		this(null, name);
	}

	public Species(Species ancestor, String name) {
		this.ancestor = ancestor;
		this.name = name;
	}

	public boolean hasAncestor() {
		return getAncestor() != null;
	}

	public Species getAncestor() {
		return ancestor;
	}

	public String getName() {
		return name;
	}

	//	public String getFullName() {
	//		return (hasAncestor() ? getAncestor().getName() + " " : "") + getName();
	//	}
	public String getFullName() {
		return getName();
	}

	public float getNaturalGrowth() {
		return naturalGrowth;
	}

	public void setNaturalGrowth(float naturalGrowth) {
		this.naturalGrowth = naturalGrowth;
	}

	public float getMass() {
		return size;
	}

	public void setMass(float size) {
		this.size = size;
	}

	public boolean isKnown(Species species) {
		return foodSources.containsKey(species);
	}

	public Set<Species> getFoodSources() {
		return foodSources.keySet();
	}

	public float getFoodSourceWeight(Species species) {
		return foodSources.getOrDefault(species, 0F);
	}

	public void setFoodSourceWeight(Species species, float weight) {
		if(weight > 0) {
			foodSources.put(species, weight);
		}
		else if(weight < 0) {
			species.foodSources.put(this, -weight);
		}
	}

	public void onInfo(InfoGroup info) {

		info.addStat("Ancestor", hasAncestor() ? getAncestor().getFullName() : "(Unknown)");

		info.addStat("Growth Rate", String.format("%.2f", getNaturalGrowth()));
		info.addStat("Size", String.format("%.2f", getMass()));
	}
}
