package vekta;

import vekta.economy.Economy;
import vekta.economy.ProductivityModifier;
import vekta.economy.NoiseModifier;

import java.util.HashSet;
import java.util.Set;

public final class Faction extends Syncable<Faction> implements Renameable, ProductivityModifier {
	private static final float BASE_PRODUCTIVITY = 1;
	private static final float ALLY_MODIFIER = 1;
	private static final float ENEMY_MODIFIER = -1;

	private final FactionType type;
	private @Sync String name;
	private @Sync int color;

	private final @Sync Set<Faction> allies = new HashSet<>();
	private final @Sync Set<Faction> enemies = new HashSet<>();

	private final Economy economy;

	public Faction(FactionType type, String name, float value, int color) {
		this.type = type;
		this.name = name;
		this.color = color;
		
		economy = new Economy(value);
		economy.addModifier(this); // Add faction productivity to economy
		economy.addModifier(new NoiseModifier(1)); // Add random noise to economy
	}

	public FactionType getType() {
		return type;
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

	public Economy getEconomy() {
		return economy;
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

	@Override
	public String getModifierName() {
		return "Faction Productivity";
	}

	/**
	 * Return the productivity modifier of the faction.
	 */
	@Override
	public float updateModifier(Economy economy) {
		return BASE_PRODUCTIVITY + allies.size() * ALLY_MODIFIER - enemies.size() * ENEMY_MODIFIER;
	}
}
