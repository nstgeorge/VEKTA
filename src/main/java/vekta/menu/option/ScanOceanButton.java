package vekta.menu.option;

import vekta.knowledge.ObservationLevel;
import vekta.knowledge.OceanKnowledge;
import vekta.menu.Menu;
import vekta.menu.handle.OceanMenuHandle;
import vekta.spawner.EcosystemGenerator;
import vekta.spawner.ItemGenerator;
import vekta.spawner.WorldGenerator;
import vekta.terrain.LandingSite;
import vekta.terrain.Terrain;
import vekta.terrain.settlement.Settlement;

import static processing.core.PApplet.round;
import static vekta.Vekta.setContext;
import static vekta.Vekta.v;

public class ScanOceanButton implements ButtonOption {
	private final LandingSite site;
	private final float density;

	public ScanOceanButton(LandingSite site, float density) {
		this.site = site;
		this.density = density;
	}

	@Override
	public String getName() {
		return "Scan Ocean";
	}

	@Override
	public void onSelect(Menu menu) {
		menu.remove(this);

		Terrain terrain = site.getTerrain();
		Menu sub = new Menu(menu, new OceanMenuHandle(site));

		OceanKnowledge knowledge = new OceanKnowledge(ObservationLevel.SCANNED, site);
		menu.getPlayer().addKnowledge(knowledge);

		if(terrain.hasFeature("Shipwrecks") || v.chance(.5F * density)) {
			terrain.addFeature("Shipwrecks");

			ItemGenerator.addLoot(knowledge.getInventory(), 1);
			sub.add(new LootMenuButton("Salvage", menu.getPlayer().getInventory(), knowledge.getInventory()));
		}
		if(v.chance(.2F * density)) {
			terrain.addFeature("Habitable");

			Settlement settlement = WorldGenerator.createSettlement();
			settlement.setOverview("You land in a biodome under the ocean's surface.");
			terrain.addSettlement(settlement);

			sub.add(new SettlementButton(settlement));
		}
		if(terrain.getEcosystem().getSpecies().isEmpty() && v.chance(.2F * density)) {
			terrain.addFeature("Deep Sea Life");

			EcosystemGenerator.populateEcosystem(terrain.getEcosystem(), round(v.random(3, 15)));
			sub.add(new EcosystemButton(terrain.getEcosystem()));
		}

		sub.addDefault();
		setContext(sub);
	}
}
