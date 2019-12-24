package vekta.spawner.item;

import vekta.ecosystem.Species;
import vekta.item.Item;
import vekta.item.ItemType;
import vekta.item.SpeciesItem;
import vekta.spawner.EcosystemGenerator;
import vekta.spawner.ItemGenerator;

import static processing.core.PApplet.ceil;
import static vekta.Vekta.v;

public class SpeciesItemSpawner implements ItemGenerator.ItemSpawner {
	@Override
	public float getWeight() {
		return .1F;
	}

	@Override
	public boolean isValid(Item item) {
		return item.getType() == ItemType.ECOSYSTEM;
	}

	@Override
	public Item create() {
		return randomSpeciesItem();
	}

	public static SpeciesItem randomSpeciesItem() {
		Species species = EcosystemGenerator.createSpecies(null);
		return new SpeciesItem(species, ceil(v.random(20, 50) / species.getMass()));
	}
}
