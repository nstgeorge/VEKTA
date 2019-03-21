package vekta.terrain.settlement;

import vekta.*;
import vekta.economy.Economy;
import vekta.economy.EconomyContainer;
import vekta.economy.ProductivityModifier;
import vekta.knowledge.ObservationLevel;
import vekta.knowledge.SettlementKnowledge;
import vekta.menu.Menu;
import vekta.object.SpaceObject;
import vekta.terrain.LandingSite;
import vekta.terrain.Terrain;
import vekta.terrain.building.BuildingType;

import java.util.ArrayList;
import java.util.List;

import static processing.core.PApplet.ceil;
import static vekta.Vekta.register;

public abstract class Settlement extends Syncable<Settlement> implements SettlementPart, EconomyContainer, ProductivityModifier {
	private static final float POPULATION_PER_VALUE = 10000;

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

		economy = register(new Economy(this));
		setupEconomy(economy);
		economy.fillHistory();

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
			//			getEconomy().removeModifier(this.faction);
			this.faction.getEconomy().removeModifier(this);
		}
		//		getEconomy().addModifier(faction);
		faction.getEconomy().addModifier(this);
		this.faction = faction;
		syncChanges();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getColor() {
		return getFaction().getColor();
	}

	public float getValueScale() {
		return 1;
	}

	@Override
	public BuildingType getType() {
		return BuildingType.EXTERNAL;
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
		return ceil(getEconomy().getValue() * POPULATION_PER_VALUE);
	}

	public List<SettlementPart> getParts() {
		return parts;
	}

	public void add(SettlementPart part) {
		for(SettlementPart existing : getParts()) {
			if(existing.addIfRelevant(part)) {
				syncChanges();
				return;
			}
		}
		if(!parts.contains(part)) {
			parts.add(part);
			syncChanges();
		}
	}

	@Override
	public boolean addIfRelevant(SettlementPart part) {
		add(part);
		return true;
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

	public void setupEconomy(Economy economy) {
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

	public boolean hasSecurity(Player player) {
		return getFaction().isEnemy(player.getFaction());
	}

	public void onSecurityMenu(Menu menu) {
	}

	public void observe(ObservationLevel level, Player player) {
		player.addKnowledge(new SettlementKnowledge(level, this));

		if(level == ObservationLevel.OWNED) {
			getParent().setPersistent(true);
		}
	}

	@Override
	public boolean isEconomyAlive() {
		return !getSite().getParent().isDestroyed();
	}

	@Override
	public String getModifierName() {
		return "Settlement: " + getName();
	}

	@Override
	public float updateModifier(Economy economy) {
		if(!isEconomyAlive()) {
			economy.removeModifier(this);
			return 0;
		}
		//		getEconomy().update();
		return getEconomy().getProductivity() * getEconomySignificance();
	}
}
