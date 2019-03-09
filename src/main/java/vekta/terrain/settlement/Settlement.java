package vekta.terrain.settlement;

import vekta.Faction;
import vekta.Resources;
import vekta.menu.Menu;
import vekta.object.SpaceObject;
import vekta.terrain.LandingSite;
import vekta.terrain.Terrain;

import java.util.ArrayList;
import java.util.List;

public abstract class Settlement implements SettlementPart {
	private final List<SettlementPart> parts = new ArrayList<>();

	private final String name;
	private final String overview;
	private LandingSite site;
	private Faction faction;

	public Settlement(Faction faction, String key) {
		this(faction, Resources.generateString(key), Resources.generateString("overview_" + key));
	}

	public Settlement(Faction faction, String name, String overview) {
		this.name = name;
		this.overview = overview;

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
		this.faction = faction;
	}

	public List<SettlementPart> getParts() {
		return parts;
	}

	@Override
	public String getName() {
		return name;
	}

	public String getOverview() {
		return overview;
	}

	public boolean isInhabited() {
		return true;
	}

	public void add(SettlementPart part) {
		parts.add(part);
	}

	public void remove(SettlementPart part) {
		parts.remove(part);
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
	public final void setupSettlementMenu(Menu menu) {
		onSettlementMenu(menu);
		for(SettlementPart part : getParts()) {
			part.setupSettlementMenu(menu);
		}
	}

	public void onSettlementMenu(Menu menu) {
	}
}
