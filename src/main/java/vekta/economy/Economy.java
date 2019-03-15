package vekta.economy;

import vekta.Sync;
import vekta.Syncable;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Economy extends Syncable<Economy> {
	private static final float PRODUCTIVITY_EFFECT_SCALE = .05F;

	private @Sync float value; // The overall value of the economy 
	private @Sync float productivity; // The most recent change in value of the economy

	private final @Sync List<ProductivityModifier> modifiers = new CopyOnWriteArrayList<>();

	public Economy(float value) {
		this.value = value;
	}

	public float getValue() {
		return value;
	}
	
	public float getProductivity() {
		return productivity;
	}

	public List<ProductivityModifier> getModifiers() {
		return modifiers;
	}

	public void addModifier(ProductivityModifier modifier) {
		if(!modifiers.contains(modifier)) {
			modifiers.add(modifier);
		}
	}

	public void removeModifier(ProductivityModifier modifier) {
		modifiers.remove(modifier);
	}

	public void update() {
		float productivity = 0;
		for(ProductivityModifier mod : getModifiers()) {
			productivity += mod.updateModifier(this);
		}
		this.productivity = productivity;
		value *= 1 + productivity / getValue() * PRODUCTIVITY_EFFECT_SCALE;

		syncChanges();
	}
}
