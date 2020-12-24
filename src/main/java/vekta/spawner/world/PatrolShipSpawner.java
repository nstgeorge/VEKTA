package vekta.spawner.world;

import processing.core.PVector;
import vekta.faction.Faction;
import vekta.object.SpaceObject;
import vekta.object.planet.TerrestrialPlanet;
import vekta.object.ship.ModularShip;
import vekta.object.ship.PatrolShip;
import vekta.player.Player;
import vekta.spawner.ItemGenerator;
import vekta.spawner.WorldGenerator;
import vekta.terrain.settlement.Settlement;

import static vekta.Vekta.register;
import static vekta.Vekta.v;

public class PatrolShipSpawner extends NearTerrestrialPlanetSpawner {

	@Override
	public float getWeight() {
		return 1;
	}

	@Override
	public void spawn(SpaceObject center, PVector pos, TerrestrialPlanet planet) {
		if(center instanceof ModularShip && ((ModularShip)center).hasController()) {
			Player player = ((ModularShip)center).getController();

			if(planet.isInhabited()) {
				Settlement settlement = v.random(planet.findInhabitedSettlements());

				Faction faction = settlement.getFaction();
				if(faction.isEnemy(player.getFaction())) {
					// Spawn if player is enemy

					PatrolShip s = register(new PatrolShip(faction, PVector.random2D(), pos, new PVector()));
					s.setTarget(player.getShip());
					WorldGenerator.orbit(planet, s, .5F);

					ItemGenerator.addLoot(s.getInventory(), (int)v.random(2) + 1);
				}
			}
		}
	}
}
