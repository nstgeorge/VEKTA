package vekta;

import vekta.economy.Economy;
import vekta.economy.EconomyContainer;
import vekta.economy.NoiseModifier;
import vekta.economy.ProductivityModifier;

import java.util.HashSet;
import java.util.Set;

import static vekta.Vekta.register;

public class Faction extends Syncable<Faction> implements Renameable, EconomyContainer, ProductivityModifier {
	private static final float BASE_PRODUCTIVITY = .2F;
	private static final float ALLY_MODIFIER = .2F;
	private static final float ENEMY_MODIFIER = -.3F;

	private @Sync String name;
	private @Sync int color;

	private final @Sync Set<Faction> allies = new HashSet<>();
	private final @Sync Set<Faction> enemies = new HashSet<>();

	private final Economy economy;

	public Faction(String name, float value, float risk, int color) {
		this.name = name;
		this.color = color;

		economy = register(new Economy(this, value));
		economy.addModifier(this); // Add faction productivity to economy
		if(risk > 0) {
			// Add economic variability/risk
			economy.addModifier(new NoiseModifier(risk * value));
		}
		economy.fillHistory();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
		syncChanges();
	}

	@Override
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
	public boolean isEconomyAlive() {
		return true;
	}

	@Override
	public String getModifierName() {
		return "Faction Productivity";
	}

	@Override
	public void updateEconomy() {
//		EconomyGenerator.updateFaction(this);
	}

	@Override
	public float updateModifier(Economy economy) {
		return BASE_PRODUCTIVITY + allies.size() * ALLY_MODIFIER - enemies.size() * ENEMY_MODIFIER;
	}
}
