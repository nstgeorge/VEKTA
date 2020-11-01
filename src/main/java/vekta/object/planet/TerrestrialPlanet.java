package vekta.object.planet;

import processing.core.PVector;
import vekta.economy.TemporaryModifier;
import vekta.faction.Faction;
import vekta.knowledge.Knowledge;
import vekta.knowledge.KnowledgeDelta;
import vekta.knowledge.ObservationLevel;
import vekta.knowledge.TerrestrialKnowledge;
import vekta.object.SpaceObject;
import vekta.object.ship.ModularShip;
import vekta.object.ship.Ship;
import vekta.person.Person;
import vekta.player.Player;
import vekta.terrain.LandingSite;
import vekta.terrain.Terrain;
import vekta.terrain.feature.Feature;
import vekta.terrain.settlement.Settlement;
import vekta.util.Counter;
import vekta.world.RenderLevel;
import vekta.world.Singleplayer;

import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static processing.core.PApplet.print;
import static processing.core.PApplet.sq;
import static vekta.Vekta.*;

/**
 * Terrestrial (landable) planet
 */
public class TerrestrialPlanet extends Planet {
	//	private static final float LABEL_THRESHOLD = 5e4F;

	private static final float EARTH_ATMOS_ALTITUDE = 100000000F;

	private final LandingSite site;

	private SpaceObject orbitObject;

	private final Counter orbitCt = new Counter(10).randomize();

	private ObservationLevel levelCache;

	private float atmosphereDensity; 		// Thickness of the atmosphere relative to Earth's
	private float atmosphereAltitude;		// Altitude at which the atmosphere of the planet is completely gone. (Earth: approx 193121280m)
	private float magneticFieldStrength;	// Strength of the magnetic field relative to Earth's

	private float escapeVelocity;			// For reference


	public TerrestrialPlanet(String name, float mass, float density, Terrain terrain, PVector position, PVector velocity, int color) {
		super(name, mass, density, position, velocity, color);

		// Calculate escape velocity from surface of this planet
		escapeVelocity = (float)Math.sqrt((2 * v.G * mass) / getRadius());

		Random rand = new Random();

		// Set atmospheric density slightly influenced by mass
		atmosphereDensity = Math.abs((float)(rand.nextGaussian() * 10)) * (mass / v.EARTH_MASS);

		// Set atmospheric altitude distributed normally relative to Earth's atmospheric altitude
		atmosphereAltitude = (EARTH_ATMOS_ALTITUDE + (float)(rand.nextGaussian() * 1000)) * (atmosphereDensity * 0.5f);

		// 30% chance to have a magnetic field, then randomly set a normally distributed value
		magneticFieldStrength = v.chance(.3f) ? 0 : Math.abs((float)(rand.nextGaussian() * 50));

		this.site = new LandingSite(this, terrain);
	}

	public LandingSite getLandingSite() {
		return site;
	}

	public SpaceObject getOrbitObject() {
		return orbitObject;
	}

	protected void setOrbitObject(SpaceObject orbitObject) {
		this.orbitObject = orbitObject;
	}

	/**
	 * Returns the thickness of the atmosphere relative to Earth.
	 * @return Atmosphere thickness (E)
	 */
	public float getAtmosphereThickness() {
		return atmosphereDensity;
	}

	public float getAtmosphereAltitude() {
		return atmosphereAltitude;
	}

	public float getMagneticFieldStrength() {
		return magneticFieldStrength;
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

	/**
	 * Update the temperature of the planet based on expected temperature equations, as well as various other characteristics.
	 * See https://astronomy.stackexchange.com/a/10116
	 */
	public void updateCharacteristics() {
		if(orbitObject != null) {
			if(orbitObject instanceof Star) {
				Star star = (Star)orbitObject;
				setTemperature((float)Math.pow((star.getLuminosity() * (1 - .3)) / (16 * Math.PI * Math.pow(super.relativePosition(star).mag(), 2) * v.STEFAN_BOLTZMANN), .25));

				// Estimate atmospheric escape
				if(getTemperature() > Math.pow(escapeVelocity, 2)) {
					atmosphereDensity -= 0.0000001 * (getTemperature() - Math.pow(escapeVelocity, 2));
				}
				if(magneticFieldStrength < 0.01) {
					atmosphereDensity -= 0.00000001;
				}
				atmosphereDensity = Math.max(0, atmosphereDensity);
			} else {
				// Temporary temperature value for planets orbiting gas giants and black holes
				setTemperature(-1);
			}
		}

		for(Feature f : getTerrain().getFeatures()) {
			f.updateOrReplace();
		}
	}

	@Override
	public RenderLevel getDespawnLevel() {
		return RenderLevel.INTERSTELLAR;
	}

	@Override
	public String getLabel() {
		if(levelCache == null /*|| (getOrbitObject() != null &&  getOrbitObject().getMass() < sq(getWorld().getZoom() * LABEL_THRESHOLD))*/) {
			return null;
		}
		return super.getLabel();

		//		return levelCache != null ? levelCache.name() : "----";
	}

	@Override
	public int getLabelColor() {
		return ObservationLevel.VISITED.isAvailableFrom(levelCache) ? super.getLabelColor() : 100;
	}

	@Override
	public void onUpdate(RenderLevel level) {
		updateOrbitObject();
		updateCharacteristics();
		super.onUpdate(level);
	}

	@Override
	public void draw(RenderLevel level, float r) {
		super.draw(level, r);

		// Draw atmosphere
		if(level.isVisibleTo(RenderLevel.PLANET)) {
			float atmosRadius = (atmosphereAltitude / getRadius()) * r;
			// Temporary - draw only the Karman line in white
			v.strokeWeight(1);
			v.stroke(100, 255);
			v.fill(0, 0);
			v.ellipse(0, 0, r + atmosRadius, r + atmosRadius);
		}

	}

	protected void updateOrbitObject() {
		if(orbitCt.cycle()) {
			setOrbitObject(getWorld().findOrbitObject(this));
			getLandingSite().getTerrain().onOrbit(orbitObject);
		}
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
	public void onDestroyed(SpaceObject s) {
		super.onDestroyed(s);

		// If something landed on this planet, destroy it as well
		SpaceObject landed = site.getLanded();
		if(landed != null) {
			landed.onDestroyed(s);
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
				float value = faction.getEconomy().getValue();
				faction.setEnemy(((ModularShip)s).getController().getFaction());
				faction.getEconomy().addModifier(new TemporaryModifier(
						"Destruction of " + getName(), -value, value / 10));
			}
		}
	}

	@Override
	public void observe(ObservationLevel level, Player player) {
		super.observe(level, player);

		if(level.isBetter(levelCache)) {
			if(level == ObservationLevel.VISITED)
				player.changeScore(100);
			levelCache = level;
		}

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
