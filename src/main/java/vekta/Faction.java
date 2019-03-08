package vekta;

public final class Faction {
	private final String name;
	private final int color;

	public Faction(String name, int color) {
		this.name = name;
		this.color = color;

	}

	public String getName() {
		return name;
	}

	public int getColor() {
		return color;
	}
}
