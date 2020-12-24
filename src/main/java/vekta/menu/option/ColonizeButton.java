package vekta.menu.option;

import vekta.faction.Faction;
import vekta.item.ColonyItem;
import vekta.knowledge.ObservationLevel;
import vekta.menu.Menu;
import vekta.terrain.LandingSite;
import vekta.terrain.Terrain;
import vekta.terrain.location.Location;
import vekta.terrain.settlement.building.CapitalBuilding;
import vekta.terrain.settlement.ColonySettlement;

public class ColonizeButton extends ButtonOption {
	private final ColonyItem item;
	private final Location location;

	public ColonizeButton(ColonyItem item, Location location) {
		this.item = item;
		this.location = location;
	}

	public ColonyItem getItem() {
		return item;
	}

	public Location getLocation() {
		return location;
	}

	@Override
	public String getName() {
		return "Colonize" + (item.getFaction() != null ? " (" + item.getFaction().getName() + ")" : "");
	}

	@Override
	public int getColor() {
		return item.getColor();
	}

	@Override
	public void onSelect(Menu menu) {
		Faction faction = item.getFaction() != null ? item.getFaction() : menu.getPlayer().getFaction();

		// Set up colony settlement
		ColonySettlement settlement = new ColonySettlement(getLocation(), faction);
		settlement.setOverview("You arrive at your recently established colony.");
		settlement.getEconomy().setValue(.1F);
		settlement.clear();
		settlement.add(new CapitalBuilding("Governor", settlement));

		// Ensure that the colony object doesn't despawn
		settlement.observe(ObservationLevel.OWNED, menu.getPlayer());

		// Remove other settlement menu options
		for(int i = 0; i < menu.size(); i++) {
			MenuOption other = menu.get(i);
			if(other instanceof SettlementButton) {
				menu.remove(other);
			}
		}

		// Add colony to menu
		ButtonOption option = new SettlementButton(settlement);
		menu.add(0, option);
		menu.select(option);

		menu.getPlayer().getInventory().remove(item);
		menu.remove(this);
	}
}
