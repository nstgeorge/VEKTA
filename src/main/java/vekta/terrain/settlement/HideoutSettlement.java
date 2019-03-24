package vekta.terrain.settlement;

import vekta.Faction;
import vekta.Player;
import vekta.economy.Economy;
import vekta.economy.NoiseModifier;
import vekta.economy.TemporaryModifier;
import vekta.menu.Menu;
import vekta.menu.option.CoinMenuOption;
import vekta.spawner.FactionGenerator;
import vekta.spawner.ItemGenerator;
import vekta.spawner.item.ClothingItemSpawner;
import vekta.spawner.item.CoinItemSpawner;
import vekta.spawner.item.WeaponItemSpawner;
import vekta.terrain.building.MarketBuilding;

import static vekta.Vekta.v;

public class HideoutSettlement extends Settlement {

	public HideoutSettlement(Faction faction) {
		super(faction, "hideout");

		if(v.chance(.5F)) {
			add(new MarketBuilding(2, "Weapons", ItemGenerator.getSpawner(WeaponItemSpawner.class)));
		}
		else {
			MarketBuilding disguises = new MarketBuilding(2, "Disguises", ItemGenerator.getSpawner(CoinItemSpawner.class));
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
	public void setupEconomy(Economy economy) {
		economy.setValue(v.random(.2F, 1));
		economy.addModifier(new NoiseModifier(.1F));
		economy.addModifier(new TemporaryModifier("Criminal Jurisdiction", -.01F, 0));
	}

	@Override
	public void onSettlementMenu(Menu menu) {
		menu.add(new CoinMenuOption()); // Add street coin market
	}
}
