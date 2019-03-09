package vekta;

import java.util.HashSet;
import java.util.Set;

public final class Faction {
	private final String name;
	private final int color;

	private final Set<Faction> allies = new HashSet<>();
	private final Set<Faction> enemies = new HashSet<>();

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

	public boolean isNeutral(Faction faction) {
		return !isAlly(faction) && !isEnemy(faction);
	}

	public void setNeutral(Faction faction) {
		if(faction != null && faction != this) {
			allies.remove(faction);
			enemies.remove(faction);
			faction.allies.remove(this);
			faction.enemies.remove(this);
		}
	}

	public boolean isAlly(Faction faction) {
		return faction == this || allies.contains(faction);
	}

	public void setAlly(Faction faction) {
		setNeutral(faction);
		if(faction != null && faction != this) {
			allies.add(faction);
			faction.allies.add(this);
		}
	}

	public boolean isEnemy(Faction faction) {
		return enemies.contains(faction);
	}

	public void setEnemy(Faction faction) {
		setNeutral(faction);
		if(faction != null && faction != this) {
			enemies.add(faction);
			faction.enemies.add(this);
		}
	}
}
