package vekta.item;

import vekta.Faction;
import vekta.menu.Menu;
import vekta.menu.handle.LandingMenuHandle;
import vekta.menu.option.CustomOption;
import vekta.menu.option.MenuOption;
import vekta.menu.option.SettlementOption;
import vekta.terrain.HabitableTerrain;
import vekta.terrain.LandingSite;
import vekta.terrain.Terrain;
import vekta.terrain.building.CapitalBuilding;
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
				menu.getPlayer().getInventory().remove(this);
				Faction faction = getFaction() != null ? getFaction() : menu.getPlayer().getFaction();
				menu.add(new CustomOption("Colonize", m -> {
					// Set up colony settlement
					ColonySettlement settlement = new ColonySettlement(faction);
					settlement.setOverview("You land close to your recently established colony.");
					settlement.clear();
					settlement.add(new CapitalBuilding(settlement));
					((HabitableTerrain)terrain).changeSettlement(site, settlement);
					
					// Ensure that the colony object doesn't despawn
					site.getParent().setPersistent(true);
					
					// Remove other settlement menu options
					for(int i = 0; i < m.size(); i++) {
						MenuOption other = menu.get(i);
						if(other instanceof SettlementOption) {
							m.remove(other);
						}
					}
					
					// Add colony to menu
					MenuOption option = new SettlementOption(settlement);
					m.add(0, option);
					m.select(option);
				}).withColor(faction.getColor()).withRemoval());
			}
		}
	}
}
