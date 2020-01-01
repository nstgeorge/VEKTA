package vekta.economy;

import vekta.item.category.ItemCategory;

import static vekta.Vekta.v;

public class BubbleModifier implements ProductivityModifier {
	private final ItemCategory category;
	private final float amount;
	private final float burstChance;

	public BubbleModifier(ItemCategory category, float amount, float burstChance) {
		this.category = category;
		this.amount = amount;
		this.burstChance = burstChance;
	}

	public ItemCategory getCategory() {
		return category;
	}

	public float getAmount() {
		return amount;
	}

	public float getBurstChance() {
		return burstChance;
	}

	@Override
	public String getModifierName() {
		return getCategory().getName() + " Bubble";
	}

	@Override
	public float updateModifier(Economy economy) {
		if(v.chance(getBurstChance())) {
			economy.removeModifier(this);
			economy.addModifier(new TemporaryModifier(getModifierName() + " Burst", -getAmount(), .1F));
			return -getAmount() * 2;
		}
		return getAmount();
	}
}
