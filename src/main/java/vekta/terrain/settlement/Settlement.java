package vekta.terrain.settlement;

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

	private final String name;
	private final String overview;

	public Settlement(String key) {
		this(Resources.generateString(key), Resources.generateString("overview_" + key));
	}

	public Settlement(String name, String overview) {
		this.name = name;
		this.overview = overview;
	}

	@Override
	public String getName() {
		return name;
	}

	public String getOverview() {
		return overview;
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
