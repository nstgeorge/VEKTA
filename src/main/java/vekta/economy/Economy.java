package vekta.economy;

import vekta.Sync;
import vekta.Syncable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import static processing.core.PApplet.max;

public class Economy extends Syncable<Economy> {
	private static final int HISTORY_LENGTH = 100;
	private static final float PRODUCTIVITY_EFFECT_SCALE = .05F;

	private final EconomyContainer container;

	private @Sync float value; // The overall value of the economy 
	private @Sync float productivity; // The most recent change in value of the economy

	private final @Sync List<ProductivityModifier> modifiers = new CopyOnWriteArrayList<>();
	private final @Sync Map<ProductivityModifier, Float> productivityMap = new HashMap<>();

	private final @Sync float[] history = new float[HISTORY_LENGTH];

	public Economy(EconomyContainer container) {
		this(container, 0);
	}

	public Economy(EconomyContainer container, float value) {
		this.container = container;
		this.value = value;
	}

	public EconomyContainer getContainer() {
		return container;
	}

	public boolean isAlive() {
		return getContainer().isEconomyAlive();
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public void addValue(float value) {
		this.value = max(0, getValue() + value);
	}

	public float getProductivity() {
		return productivity;
	}

	public float getProductivity(ProductivityModifier modifier) {
		return productivityMap.getOrDefault(modifier, 0F);
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
		productivityMap.remove(modifier);
	}

	public float[] getHistory() {
		return history;
	}

	private void addHistory(float value) {
		float[] history = getHistory();
		System.arraycopy(history, 0, history, 1, history.length - 1);
		history[0] = value;
	}

	public void fillHistory() {
		for(int i = 0; i < getHistory().length; i++) {
			update();
		}
	}

	public void update() {
		getContainer().updateEconomy();

		float productivity = 0;
		for(ProductivityModifier mod : getModifiers()) {
			float p = mod.updateModifier(this);
			productivity += p;
			productivityMap.put(mod, p);
		}
		this.productivity = productivity;
		value *= 1 + productivity / getValue() * PRODUCTIVITY_EFFECT_SCALE;
		addHistory(value);

		syncChanges();
	}
}
