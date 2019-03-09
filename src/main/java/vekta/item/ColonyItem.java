package vekta.item;

import vekta.Faction;
import vekta.menu.Menu;
import vekta.menu.handle.LandingMenuHandle;
import vekta.menu.option.BasicOption;
import vekta.menu.option.MenuOption;
import vekta.menu.option.SettlementOption;
import vekta.terrain.HabitableTerrain;
import vekta.terrain.LandingSite;
import vekta.terrain.Terrain;
import vekta.terrain.settlement.ColonySettlement;

public class ColonyItem extends Item {
	private final Faction faction;

	public ColonyItem() {
		this(null);
	}

	public ColonyItem(Faction faction) {
		super("Colony Kit" + (faction != null ? " (" + faction + ")" : ""), ItemType.COLONY);

		this.faction = faction;
	}

	public Faction getFaction() {
		return faction;
	}

	@Override
	public void onMenu(Menu menu) {
		if(menu.getHandle() instanceof LandingMenuHandle) {
			LandingSite site = ((LandingMenuHandle)menu.getHandle()).getSite();
			Terrain terrain = site.getTerrain();
			if(terrain instanceof HabitableTerrain && !terrain.isInhabited()) {
				Faction faction = getFaction() != null ? getFaction() : menu.getPlayer().getFaction();
				menu.add(new BasicOption("Colonize", m -> {
					ColonySettlement settlement = new ColonySettlement(faction);
					((HabitableTerrain)terrain).changeSettlement(settlement);
					for(int i = 0; i < m.size(); i++) {
						MenuOption other = menu.get(i);
						if(other instanceof SettlementOption) {
							m.remove(other);
						}
					}
					MenuOption option = new SettlementOption(settlement);
					m.add(0, option);
					option.select(m);
				}).withColor(faction.getColor()).withRemoval());
			}
		}
	}
}
