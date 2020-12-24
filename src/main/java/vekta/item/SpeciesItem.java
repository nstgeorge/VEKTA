package vekta.item;

import vekta.ecosystem.Species;
import vekta.util.InfoGroup;

import static processing.core.PApplet.ceil;

public class SpeciesItem extends Item {
	private final int count;
	private Species species;

	public SpeciesItem(Species species, int count) {
		this.species = species;
		this.count = count;
	}

	public Species getSpecies() {
		return species;
	}

	public int getCount() {
		return count;
	}

	@Override
	public String getName() {
		return getSpecies().getFullName() + " x" + getCount();
	}

	@Override
	public ItemType getType() {
		return ItemType.ECOSYSTEM;
	}

	@Override
	public int randomPrice() {
		return ceil(super.randomPrice() * getSpecies().getMass() * getCount());
	}

	@Override
	public int getMass() {
		return 5 + ceil(getSpecies().getMass() * getCount());
	}

	@Override
	public void onInfo(InfoGroup info) {
		info.addStat("Quantity", getCount());

		species.onInfo(info);
	}
}
