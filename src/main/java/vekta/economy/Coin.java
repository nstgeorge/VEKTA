package vekta.economy;

import java.io.Serializable;

public class Coin implements Serializable {
	private final String name;
	private final float valueScale;

	public Coin(String name, float valueScale) {
		this.name = name;
		this.valueScale = valueScale;
	}

	public String getName() {
		return name;
	}

	public float getValueScale() {
		return valueScale;
	}
}
