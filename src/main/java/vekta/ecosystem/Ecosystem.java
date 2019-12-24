package vekta.ecosystem;

import vekta.InfoGroup;
import vekta.Syncable;
import vekta.spawner.EcosystemGenerator;

import java.util.*;
import java.util.stream.Collectors;

import static processing.core.PApplet.*;
import static vekta.Vekta.v;

public class Ecosystem extends Syncable<Ecosystem> {
	private static final float EPOCH_TIME_SCALE = .1F;
	private static final int MAX_EXTINCTIONS = 5;

	private final Map<Species, Float> amounts = new HashMap<>();
	private final List<Species> extinctions = new LinkedList<>();

	private float capacity;

	public Ecosystem(float capacity) {
		this.capacity = capacity;
	}

	public float getCapacity() {
		return capacity;
	}

	public void setCapacity(float capacity) {
		this.capacity = capacity;
	}

	public Set<Species> getSpecies() {
		return amounts.keySet();
	}

	public List<Species> getExtinctions() {
		return extinctions;
	}

	public boolean has(Species species) {
		return amounts.containsKey(species);
	}

	public int count(Species species) {
		return round(amounts.getOrDefault(species, 0F));
	}

	public void add(Species species, int amount) {
		float newAmount = max(0F, count(species) + amount);
		if(newAmount > 1) {
			amounts.put(species, newAmount);
		}
		else {
			amounts.remove(species);
			extinctions.add(0, species);
			if(extinctions.size() > MAX_EXTINCTIONS) {
				extinctions.remove(extinctions.size() - 1);
			}
		}
	}

	public void update() {
		Map<Species, Float> prevAmounts = new HashMap<>(amounts);

		int speciesCount = prevAmounts.size();

		float sum = 0;
		Species max = null;
		for(Species species : prevAmounts.keySet()) {
			float amount = prevAmounts.get(species);
			float weight = amount * species.getMass();
			float delta = species.getNaturalGrowth() * amount;

			for(Species other : prevAmounts.keySet()) {
				if(species != other) {
					float otherAmount = prevAmounts.get(other);
					float otherWeight = otherAmount * other.getMass();

					float weightRatio = otherWeight / weight;

					delta += species.getFoodSourceWeight(other) * amount / weightRatio / sq(speciesCount);
					delta -= other.getFoodSourceWeight(species) * otherAmount * weightRatio;

					//					if(species.getNaturalGrowth() <= 0 && v.chance(abs(species.getNaturalGrowth() * other.getNaturalGrowth() * weightRatio / (1 + species.getFoodSources().size()) * EPOCH_TIME_SCALE))) {
					//						species.setFoodSourceWeight(other, v.random(-species.getNaturalGrowth()));
					//					}
				}
			}

			delta += sq(speciesCount) * v.random(-2, 1) / species.getMass();
			add(species, round(delta * EPOCH_TIME_SCALE));

			sum += amount * species.getMass();
			if(max == null || weight > prevAmounts.get(max) * max.getMass()) {
				max = species;
			}
		}

		float scarcityFactor = sq(sum / getCapacity() / speciesCount) * EPOCH_TIME_SCALE;
		float reviveFactor = sq((1 - sum / getCapacity()) / speciesCount) * EPOCH_TIME_SCALE;

//		if(sum > getCapacity()) {
			for(Species species : amounts.keySet()) {
				if(species.getNaturalGrowth() > 0) {
					amounts.put(species, amounts.get(species) / (1 + v.random(scarcityFactor * speciesCount)));
				}
			}
//		}

		if(v.chance(scarcityFactor)) {
			// Introduce a predator when ecosystem is approaching capacity
			Species predator = EcosystemGenerator.introduceSpecies(this);
			predator.setNaturalGrowth(min(predator.getNaturalGrowth(), v.random(-.1F)));
			if(max != null) {
				predator.setFoodSourceWeight(max, abs(predator.getNaturalGrowth()));
			}
		}
		if(v.chance(reviveFactor)) {
			Species prey = EcosystemGenerator.introduceSpecies(this);
			prey.setNaturalGrowth(max(prey.getNaturalGrowth(), v.random(.1F)));
		}
	}

	public void onInfo(InfoGroup info, Species species) {
		info.addStat("Population", count(species));

		species.onInfo(info);

		String prey = species.getFoodSources().stream()
				.map(Species::getName)
				.collect(Collectors.joining(", "));
		String predators = getSpecies().stream()
				.filter(s -> s.getFoodSourceWeight(s) > 0)
				.map(Species::getName)
				.collect(Collectors.joining(", "));

		if(!prey.isEmpty()) {
			info.addStat("Prey", prey);
		}
		if(!predators.isEmpty()) {
			info.addStat("Predators", predators);
		}
	}
}
