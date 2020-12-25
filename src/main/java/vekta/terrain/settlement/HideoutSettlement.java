package vekta.terrain.settlement;

import vekta.economy.Economy;
import vekta.economy.NoiseModifier;
import vekta.economy.TemporaryModifier;
import vekta.faction.Faction;
import vekta.menu.Menu;
import vekta.menu.option.CoinMenuButton;
import vekta.object.planet.TerrestrialPlanet;
import vekta.player.Player;
import vekta.spawner.FactionGenerator;
import vekta.spawner.item.ClothingItemSpawner;
import vekta.spawner.item.WeaponItemSpawner;
import vekta.terrain.location.Location;
import vekta.terrain.settlement.building.MarketBuilding;

import static vekta.Vekta.v;

public class HideoutSettlement extends Settlement {

	public HideoutSettlement(TerrestrialPlanet planet, Faction faction) {
		super(planet, faction, "hideout");

		if(v.chance(.25F)) {
			// Mercenaries
		}

		if(v.chance(.5F)) {
			add(new MarketBuilding(2, "Weapons", WeaponItemSpawner.class));
		}
		else {
			MarketBuilding disguises = new MarketBuilding(2, "Disguises", ClothingItemSpawner.class);
			add(disguises);
			disguises.getInventory().clearItems();
			int disguiseCt = (int)v.random(4, 8);
			for(int i = 0; i < disguiseCt; i++) {
				disguises.getInventory().add(ClothingItemSpawner.createDisguiseItem(FactionGenerator.randomFaction()));
			}
		}
	}

	@Override
	public String getGenericName() {
		return "Hideout";
	}

	@Override
	public float getValueScale() {
		return 2;
	}

	@Override
	public boolean hasSecurity(Player player) {
		return false;
	}

	@Override
	public void initEconomy(Economy economy) {
		economy.setValue(v.random(.2F, 1));
		economy.addModifier(new NoiseModifier(.1F));
		economy.addModifier(new TemporaryModifier("Criminal Jurisdiction", -.01F, 0));
	}

	@Override
	public void onSettlementMenu(Menu menu) {
		menu.add(new CoinMenuButton()); // Add street coin market
	}
}
