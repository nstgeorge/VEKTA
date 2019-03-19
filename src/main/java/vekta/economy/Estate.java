package vekta.economy;

import vekta.terrain.settlement.Settlement;

import java.io.Serializable;

public class Estate implements Serializable {
	private final String name;
	private final Settlement settlement;
	private final float size;
	private float value;

	public Estate(String name, Settlement settlement, float size, float value) {
		this.name = name;
		this.settlement = settlement;
		this.size = size;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public Settlement getSettlement() {
		return settlement;
	}

	public float getSize() {
		return size;
	}

	public float getValue() {
		return value;
	}
}
