package vekta.object.planet;

import processing.core.PVector;
import vekta.Faction;
import vekta.economy.TemporaryModifier;
import vekta.object.SpaceObject;
import vekta.object.ship.ModularShip;
import vekta.person.Person;
import vekta.terrain.LandingSite;
import vekta.terrain.Terrain;
import vekta.terrain.settlement.Settlement;

import java.util.Set;
import java.util.stream.Collectors;

import static vekta.Vekta.getWorld;

/**
 * Terrestrial (landable) planet
 */
public class TerrestrialPlanet extends Planet {
	private final LandingSite site;

	public TerrestrialPlanet(String name, float mass, float density, Terrain terrain, PVector position, PVector velocity, int color) {
		super(name, mass, density, position, velocity, color);

		this.site = new LandingSite(this, terrain);

		//		println("[Terrestrial] mass: " + getMass() + ", radius: " + getRadius());
	}

	public LandingSite getLandingSite() {
		return site;
	}

	public Terrain getTerrain() {
		return getLandingSite().getTerrain();
	}

	public boolean isHabitable() {
		// TODO: compute based on properties
		return getTerrain().hasFeature("Habitable");
	}

	@Override
	public void onCollide(SpaceObject s) {
		if(s instanceof ModularShip) {
			ModularShip ship = (ModularShip)s;
			if(ship.isDockable(this)) {
				site.land(ship);
			}
			return; // Prevent ship from being destroyed after landing
		}
		super.onCollide(s); // Oof
	}

	@Override
	public void onDestroy(SpaceObject s) {
		super.onDestroy(s);

		// If something landed on this planet, destroy it as well
		SpaceObject landed = site.getLanded();
		if(landed != null) {
			landed.onDestroy(s);
		}

		// If player destroyed planet, immediately set enemy
		if(s instanceof ModularShip && ((ModularShip)s).hasController()) {
			for(Person person : getWorld().findObjects(Person.class)) {
				if(getTerrain().getSettlements().contains(person.findHome())) {
					person.die();
				}
			}

			Set<Faction> factions = getTerrain().getSettlements().stream()
					.map(Settlement::getFaction)
					.collect(Collectors.toSet());

			for(Faction faction : factions) {
				faction.setEnemy(((ModularShip)s).getController().getFaction());
				faction.getEconomy().addModifier(new TemporaryModifier(
						"Destruction of " + getName(),
						-faction.getEconomy().getValue() * .1F,
						.1F));
			}
		}
	}
}
