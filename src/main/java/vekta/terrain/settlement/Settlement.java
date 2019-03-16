package vekta.terrain.settlement;

import vekta.Faction;
import vekta.Resources;
import vekta.Sync;
import vekta.Syncable;
import vekta.economy.Economy;
import vekta.economy.ProductivityModifier;
import vekta.menu.Menu;
import vekta.object.SpaceObject;
import vekta.terrain.LandingSite;
import vekta.terrain.Terrain;

import java.util.ArrayList;
import java.util.List;

import static processing.core.PApplet.ceil;

public abstract class Settlement extends Syncable<Settlement> implements SettlementPart, ProductivityModifier {
	private static final float POPULATION_PER_VALUE = 10000;
	private static final float INFLUENCE_SCALE = .1F;

	private final @Sync List<SettlementPart> parts = new ArrayList<>();

	private final String name;
	private @Sync String overview;
	private @Sync LandingSite site;
	private @Sync Faction faction;

	private final Economy economy;

	//	private int population;

	public Settlement(Faction faction, String key) {
		this(faction, Resources.generateString(key), Resources.generateString("overview_" + key));
	}

	public Settlement(Faction faction, String name, String overview) {
		this.name = name;
		this.overview = overview;

		economy = new Economy();

		setFaction(faction);
	}

	public LandingSite getSite() {
		return site;
	}

	public Terrain getTerrain() {
		return getSite().getTerrain();
	}

	public SpaceObject getParent() {
		return getSite().getParent();
	}

	public Faction getFaction() {
		return faction;
	}

	public void setFaction(Faction faction) {
		if(faction == null) {
			throw new RuntimeException("Settlement faction cannot be null");
		}
		if(this.faction != null) {
			this.faction.getEconomy().removeModifier(this);
		}
		faction.getEconomy().addModifier(this);
		this.faction = faction;
		syncChanges();
	}

	@Override
	public String getName() {
		return name;
	}

	public String getOverview() {
		return overview;
	}

	public void setOverview(String overview) {
		if(overview == null) {
			throw new RuntimeException("Settlement overview cannot be null");
		}
		this.overview = overview;
		syncChanges();
	}

	public Economy getEconomy() {
		return economy;
	}

	public float getEconomySignificance() {
		return .1F;
	}

	public boolean isInhabited() {
		return getPopulation() > 0;
	}

	public int getPopulation() {
		//		return population;
		return ceil(getEconomy().getValue() * POPULATION_PER_VALUE);
	}

	//	public void setPopulation(int population) {
	//		this.population = population;
	//	}
	//
	//	public void addPopulation(int amount) {
	//		setPopulation(getPopulation() + amount);
	//	}
	//
	//	public void removePopulation(int amount) {
	//		setPopulation(max(0, getPopulation() - amount));
	//	}

	public List<SettlementPart> getParts() {
		return parts;
	}

	public void add(SettlementPart part) {
		parts.add(part);
		syncChanges();
	}

	public void remove(SettlementPart part) {
		parts.remove(part);
		syncChanges();
	}

	public void clear() {
		getParts().clear();
		syncChanges();
	}

	@SuppressWarnings("unchecked")
	public <T extends SettlementPart> T find(Class<T> type) {
		for(SettlementPart part : getParts()) {
			if(type.isInstance(part)) {
				return (T)part;
			}
		}
		return null;
	}

	public int count(Class<? extends SettlementPart> type) {
		int ct = 0;
		for(SettlementPart part : getParts()) {
			if(type.isInstance(part)) {
				ct++;
			}
		}
		return ct;
	}

	@Override
	public final void setup(LandingSite site) {
		if(this.site != null) {
			throw new RuntimeException("Settlement has already been set up");
		}
		this.site = site;
		onSetup();
		for(SettlementPart part : getParts()) {
			part.setup(site);
		}
	}

	public void onSetup() {
	}

	@Override
	public final void setupMenu(Menu menu) {
		onSettlementMenu(menu);
		for(SettlementPart part : getParts()) {
			part.setupMenu(menu);
		}
	}

	public void onSettlementMenu(Menu menu) {
	}

	@Override
	public String getModifierName() {
		return "Settlement: " + getName();
	}

	@Override
	public float updateModifier(Economy economy) {
		if(getSite().getParent().isDestroyed()) {
			economy.removeModifier(this);
			return 0;
		}
		getEconomy().update();
		return getEconomy().getProductivity() * getEconomySignificance();
	}
}
