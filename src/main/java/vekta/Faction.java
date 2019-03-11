package vekta;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static java.lang.Float.max;

public final class Faction extends Syncable<Faction> implements Serializable, Renameable {
	private String name;
	private int color;

	private final Set<Faction> allies = new HashSet<>();
	private final Set<Faction> enemies = new HashSet<>();

	public Faction(String name, int color) {
		this.name = name;
		this.color = color;
	}

	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
		syncChanges();
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
		syncChanges();
	}

	public boolean isNeutral(Faction faction) {
		return !isAlly(faction) && !isEnemy(faction);
	}

	public void setNeutral(Faction faction) {
		if(faction != this) {
			allies.remove(faction);
			enemies.remove(faction);
			faction.allies.remove(this);
			faction.enemies.remove(this);
			syncChanges();
		}
	}

	public Set<Faction> getAllies() {
		return allies;
	}

	public boolean isAlly(Faction faction) {
		return faction == this || allies.contains(faction);
	}

	public void setAlly(Faction faction) {
		setNeutral(faction);
		if(faction != this) {
			allies.add(faction);
			faction.allies.add(this);
			syncChanges();
		}
	}

	public Set<Faction> getEnemies() {
		return enemies;
	}

	public boolean isEnemy(Faction faction) {
		return enemies.contains(faction);
	}

	public void setEnemy(Faction faction) {
		if(!isEnemy(faction)) {
			setNeutral(faction);
			if(faction != this) {
				enemies.add(faction);
				faction.enemies.add(this);
			}

			for(Faction ally : getAllies()) {
				ally.setEnemy(faction);
			}
			for(Faction ally : faction.getAllies()) {
				ally.setEnemy(this);
			}

			syncChanges();
		}
	}

	public float getValue() {
		return max(1, allies.size() - enemies.size() * .5F);
	}

//	@Override
//	public void onSync(Faction data) {
//		this.name = data.name;
//		this.color = data.color;
//		syncAll(allies, data.allies);
//		syncAll(allies, data.enemies);
//	}
}
