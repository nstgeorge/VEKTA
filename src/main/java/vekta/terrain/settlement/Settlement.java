package vekta.terrain.settlement;

import vekta.Faction;
import vekta.Resources;
import vekta.menu.Menu;
import vekta.terrain.Terrain;

import java.util.ArrayList;
import java.util.List;

public abstract class Settlement implements SettlementPart {
	private final List<SettlementPart> parts = new ArrayList<>();

	public List<SettlementPart> getParts() {
		return parts;
	}

	private Faction faction;
	private final String name;
	private final String overview;

	public Settlement(Faction faction, String key) {
		this(faction, Resources.generateString(key), Resources.generateString("overview_" + key));
	}

	public Settlement(Faction faction, String name, String overview) {
		this.faction = faction;
		this.name = name;
		this.overview = overview;
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

	@Override
	public final void setupTerrain(Terrain terrain) {
		onTerrain(terrain);
		for(SettlementPart part : getParts()) {
			part.setupTerrain(terrain);
		}
	}

	public void onTerrain(Terrain terrain) {
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
