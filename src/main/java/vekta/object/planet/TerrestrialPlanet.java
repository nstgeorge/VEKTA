package vekta.object.planet;

import processing.core.PVector;
import vekta.economy.TemporaryModifier;
import vekta.faction.Faction;
import vekta.knowledge.ObservationLevel;
import vekta.knowledge.TerrestrialKnowledge;
import vekta.object.SpaceObject;
import vekta.object.ship.ModularShip;
import vekta.object.ship.Ship;
import vekta.person.Person;
import vekta.player.Player;
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

	public float getValueScale() {
		return 1;
	}

	@Override
	public void onCollide(SpaceObject s) {
		if(s instanceof Ship) {
			Ship ship = (Ship)s;
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

		// Clean up settlements
		for(Settlement settlement : getTerrain().getSettlements()) {
			settlement.cleanup();
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

	@Override
	public void observe(ObservationLevel level, Player player) {
		super.observe(level, player);

		player.addKnowledge(new TerrestrialKnowledge(level, this));

		if(ObservationLevel.SCANNED.isAvailableFrom(level)) {
			for(Settlement settlement : getLandingSite().getTerrain().getSettlements()) {
				// Observe settlements at the next-down observation level
				settlement.observe(level.decreased(), player);
			}
		}

		/// TODO: consider lowering threshold for persistence
		if(ObservationLevel.OWNED.isAvailableFrom(level)) {
			setPersistent(true);
		}
	}
}
