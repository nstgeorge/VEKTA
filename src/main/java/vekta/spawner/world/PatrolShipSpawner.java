package vekta.spawner.world;

import processing.core.PVector;
import vekta.Faction;
import vekta.Player;
import vekta.object.SpaceObject;
import vekta.object.ship.ModularShip;
import vekta.object.ship.PatrolShip;
import vekta.spawner.ItemGenerator;
import vekta.spawner.WorldGenerator;
import vekta.terrain.LandingSite;

import static vekta.Vekta.register;
import static vekta.Vekta.v;

public class PatrolShipSpawner extends NearPlanetSpawner {

	@Override
	public float getWeight() {
		return 1;
	}

	@Override
	public void spawn(SpaceObject center, PVector pos, LandingSite site) {
		if(center instanceof ModularShip && ((ModularShip)center).hasController() && site.getTerrain().isInhabited()) {
			Player player = ((ModularShip)center).getController();
			Faction faction = v.random(site.getTerrain().getSettlements()).getFaction();
			if(faction.isEnemy(player.getFaction())) {
				// Spawn if player is enemy

				PatrolShip s = register(new PatrolShip(faction, PVector.random2D(), pos, new PVector()));
				s.setTarget(player.getShip());
				WorldGenerator.orbit(site.getParent(), s, .5F);

				ItemGenerator.addLoot(s.getInventory(), (int)v.random(2) + 1);
			}
		}
	}
}
