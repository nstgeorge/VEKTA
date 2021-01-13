package vekta.terrain.settlement;

import vekta.Renameable;
import vekta.Resources;
import vekta.economy.Economy;
import vekta.economy.ProductivityModifier;
import vekta.faction.Faction;
import vekta.knowledge.FactionKnowledge;
import vekta.knowledge.ObservationLevel;
import vekta.knowledge.SettlementKnowledge;
import vekta.menu.Menu;
import vekta.object.SpaceObject;
import vekta.object.planet.TerrestrialPlanet;
import vekta.player.Player;
import vekta.spawner.SettlementGenerator;
import vekta.sync.Sync;
import vekta.sync.Syncable;
import vekta.terrain.location.SettlementLocation;
import vekta.terrain.settlement.building.BuildingType;
import vekta.terrain.visual.SettlementVisual;
import vekta.terrain.visual.Visual;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static processing.core.PApplet.ceil;
import static processing.core.PApplet.pow;
import static vekta.Vekta.register;

public abstract class Settlement extends Syncable<Settlement> implements SettlementPart, Economy.Container, ProductivityModifier, Renameable {
	private static final float POPULATION_SCALE = 1000;

	private final SettlementLocation location;
	private final @Sync List<SettlementPart> parts = new ArrayList<>();

	private @Sync String name;
	private @Sync String overview;
	private @Sync Faction faction;

	private final Economy economy;

	private final Visual visual;

	public Settlement(TerrestrialPlanet planet, Faction faction, String key) {
		this(planet, faction, Resources.generateString(key), Resources.generateString("overview_" + key));
	}

	public Settlement(TerrestrialPlanet planet, Faction faction, String name, String overview) {
//		this.planet = planet;
		this.name = name;
		this.overview = overview;

		// TEMP
		location = new SettlementLocation(planet);
		location.notifySettlement(this);

		economy = register(new Economy(this));
		initEconomy(economy);
		economy.fillHistory();

		visual = new SettlementVisual(0, 0, economy.getValue());

		setFaction(faction);

		SettlementGenerator.populateSettlement(this);
	}

	public SettlementLocation getLocation() {
		return location;
	}

	public SpaceObject getParent() {
		return getLocation().getPlanet();
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

	@Override
	public void setName(String name) {
		this.name = name;
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

	public Visual getVisual() {
		return visual;
	}

	public boolean isInhabited() {
		return getPopulation() > 0;
	}

	public int getPopulation() {
		return ceil(pow(getEconomy().getValue(), 4) * POPULATION_SCALE);
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

	//	//	@Override
	//	public final void setup(LandingSite site) {
	////		if(this.site != null) {
	////			throw new RuntimeException("Settlement has already been set up");
	////		}
	////		this.site = site;
	////		onSetup();
	////		//		for(SettlementPart part : getParts()) {
	////		//			part.setup(site);
	////		//		}
	////		SettlementGenerator.populateSettlement(this);
	//	}

	//	@Override
	//	public void cleanup() {
	//		for(SettlementPart part : getParts()) {
	//			part.cleanup();
	//		}
	//	}

	@Override
	public final void onSurveyTags(Set<String> tags) {
		for(SettlementPart part : getParts()) {
			part.onSurveyTags(tags);
		}
		onSettlementSurveyTags(tags);
	}

	protected void onSettlementSurveyTags(Set<String> tags) {
	}

	protected void initEconomy(Economy economy) {
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
		player.addKnowledge(new FactionKnowledge(getFaction()));

		if(level == ObservationLevel.OWNED) {
			getParent().setPersistent(true);
		}
	}

	@Override
	public boolean isEconomyAlive() {
		return !getParent().isDestroyed();
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
		//		getEconomy().setValue();
		return getEconomy().getProductivity() * getEconomySignificance();
	}
}
