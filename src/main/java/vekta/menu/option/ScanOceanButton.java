package vekta.menu.option;

import vekta.knowledge.ObservationLevel;
import vekta.knowledge.OceanKnowledge;
import vekta.menu.Menu;
import vekta.menu.handle.OceanMenuHandle;
import vekta.terrain.location.OceanLocation;

import static vekta.Vekta.setContext;

public class ScanOceanButton extends ButtonOption {
	private final OceanLocation location;
	private final float density;

	public ScanOceanButton(OceanLocation location, float density) {
		this.location = location;
		this.density = density;
	}

	@Override
	public String getName() {
		return "Scan Ocean";
	}

	@Override
	public void onSelect(Menu menu) {
		menu.remove(this);

		Menu sub = new Menu(menu, new OceanMenuHandle(location));

		OceanKnowledge knowledge = new OceanKnowledge(ObservationLevel.SCANNED, location);
		menu.getPlayer().addKnowledge(knowledge);

//		if(!location.isDepleted()){
////			location.
//
//			if(v.chance(.5F * density)) {
//				ItemGenerator.addLoot(knowledge.getInventory(), 1);
//				sub.add(new LootMenuButton("Salvage", knowledge.getInventory()));
//			}
//
//			if(v.chance(.2F * density)) {
//				Settlement settlement = SettlementGenerator.createSettlement(location);
//				settlement.setOverview("You land in a biodome under the ocean's surface.");
//				terrain.addSettlement(settlement);
//
//				sub.add(new SettlementButton(settlement));
//			}
//		}

		sub.addDefault();
		setContext(sub);
	}
}
