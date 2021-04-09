package vekta.menu.option;

import vekta.knowledge.ObservationLevel;
import vekta.knowledge.OceanKnowledge;
import vekta.menu.Menu;
import vekta.menu.handle.OceanMenuHandle;
import vekta.spawner.ItemGenerator;
import vekta.spawner.SettlementGenerator;
import vekta.terrain.location.OceanLocation;
import vekta.terrain.settlement.Settlement;

import java.util.List;

import static vekta.Vekta.setContext;
import static vekta.Vekta.v;

public class ScanOceanButton extends ButtonOption {
	private final OceanLocation location;
	private final float strength;

	public ScanOceanButton(OceanLocation location, float strength) {
		this.location = location;
		this.strength = strength;
	}

	@Override
	public String getName() {
		return "Scan Ocean";
	}

	@Override
	public void onSelect(Menu menu) {
//		menu.remove(this);

		Menu sub = new Menu(menu, new OceanMenuHandle(location));

		List<OceanKnowledge> knowledgeList = menu.getPlayer().findKnowledge(OceanKnowledge.class, o -> o.getSpaceObject() == location.getPlanet());
		if(knowledgeList.isEmpty()) {
			OceanKnowledge knowledge = new OceanKnowledge(ObservationLevel.SCANNED, location);
			menu.getPlayer().addKnowledge(knowledge);

			if(v.chance(1 - .5F / strength)) {
				ItemGenerator.addLoot(knowledge.getInventory(), 1);
				sub.add(new LootMenuButton("Salvage", knowledge.getInventory()));
			}

			if(v.chance(.2F * strength)) {
				Settlement settlement = SettlementGenerator.createSettlement(location.getPlanet());
				settlement.setOverview("You land in a biodome under the ocean's surface.");
				location.addPathway(settlement.getLocation());

				sub.add(new SettlementButton(settlement));
			}

			knowledgeList.add(knowledge);
		}

		for(OceanKnowledge knowledge : knowledgeList) {
			if(knowledge.getInventory().itemCount() > 0) {
				menu.add(new LootMenuButton("Salvage", knowledge.getInventory()));
			}
		}

		sub.addDefault();
		setContext(sub);
	}
}
