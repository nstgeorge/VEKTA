package vekta.deity;

import vekta.item.category.ItemCategory;
import vekta.sync.Syncable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Deity extends Syncable<Deity> {
	private final String name;
	private final ItemCategory offerCategory;
	private final String[] traits;
	private final Map<Deity, Float> opinions = new HashMap<>();

	public Deity(String name, ItemCategory offerCategory, String[] traits) {
		this.name = name;
		this.offerCategory = offerCategory;
		this.traits = traits;
	}

	public String getName() {
		return name;
	}

	public ItemCategory getOfferCategory() {
		return offerCategory;
	}

	public String[] getTraits() {
		return traits;
	}

	public Set<Deity> getOpinionDeities() {
		return opinions.keySet();
	}

	public float getOpinion(Deity deity) {
		return opinions.getOrDefault(deity, 0F);
	}

	public void addOpinion(Deity deity, float opinion) {
		opinions.put(deity, getOpinion(deity) + opinion);
	}
}
