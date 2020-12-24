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
import vekta.spawner.TerrainGenerator;
import vekta.terrain.LandingSite;
import vekta.terrain.Terrain;
import vekta.terrain.settlement.Settlement;
import vekta.util.Counter;
import vekta.world.RenderLevel;

import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static processing.core.PApplet.print;
import static vekta.Vekta.*;

/**
 * Terrestrial (landable) planet
 */
public class TerrestrialPlanet extends Planet {
	// TODO: gradually move randomization into StarSystemGenerator

	//	private static final float LABEL_THRESHOLD = 5e4F;

	private static final float EARTH_ATMOS_ALTITUDE = 1e8F;

	private LandingSite site;

	private SpaceObject orbitObject;

	private final Counter orbitCt = new Counter(10).randomize();

	private ObservationLevel levelCache;

	private float atmosphereDensity;        // Thickness of the atmosphere relative to Earth's
	private final float atmosphereAltitude;        // Altitude at which the atmosphere of the planet is completely gone. (Earth: approx 193121280m)
	private final float magneticFieldStrength;    // Strength of the magnetic field relative to Earth's

	private final float escapeVelocity;            // For reference

	public TerrestrialPlanet(String name, float mass, float density, PVector position, PVector velocity, int color) {
		super(name, mass, density, position, velocity, color);

		// Calculate escape velocity from surface of this planet
		escapeVelocity = (float)Math.sqrt((2 * v.G * mass) / getRadius());

		// Set atmospheric density slightly influenced by mass
		atmosphereDensity = Math.abs(v.gaussian(10)) * (mass / v.EARTH_MASS);

		// Set atmospheric altitude distributed normally relative to Earth's atmospheric altitude
		atmosphereAltitude = (EARTH_ATMOS_ALTITUDE + (v.gaussian(1000))) * (atmosphereDensity * 0.5f);

		// 30% chance to have a magnetic field, then randomly set a normally distributed value
		magneticFieldStrength = v.chance(.3f) ? 0 : Math.abs((float)(v.gaussian(50)));

		// TODO: ensure the initialization order works properly
		this.site = new LandingSite(this, createTerrain());

		updateOrbitObject();
	}

	protected Terrain createTerrain() {
		return TerrainGenerator.createTerrain(this);
	}

	public LandingSite getLandingSite() {
		return site;
	}

	public Terrain getTerrain() {
		//		if(site == null) {
		//			throw new RuntimeException("Terrain not initialized for " + getClass().getSimpleName());
		//		}
		return site.getTerrain();
	}

	public void setTerrain(Terrain terrain) {
		if(site != null) {
			throw new RuntimeException(getClass().getSimpleName() + " already has " + site.getTerrain().getClass().getSimpleName());
		}
		site = new LandingSite(this, terrain);
	}

	//		public void setTerrain(Terrain terrain) {
	//			if(this.terrain != null) {
	//				throw new RuntimeException(getClass().getSimpleName() + " already has " + this.terrain.getClass().getSimpleName());
	//			}
	//			this.terrain = terrain;
	//		}

	public SpaceObject getOrbitObject() {
		return orbitObject;
	}

	protected void setOrbitObject(SpaceObject orbitObject) {
		this.orbitObject = orbitObject;
		updateCharacteristics();
	}

	public float getEscapeVelocity() {
		return escapeVelocity;
	}

	/**
	 * Returns the thickness of the atmosphere relative to Earth.
	 *
	 * @return Atmosphere thickness (E)
	 */
	public float getAtmosphereDensity() {
		return atmosphereDensity;
	}

	public float getAtmosphereAltitude() {
		return atmosphereAltitude;
	}

	public float getMagneticFieldStrength() {
		return magneticFieldStrength;
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

			SpaceObject object = orbitObject;
			while(object != null && !(object instanceof Star)) {
				object = getWorld().findOrbitObject(object);
			}

			if(object != null) {
				Star star = (Star)object;
				setTemperatureKelvin((float)Math.pow((star.getLuminosity() * (1 - .3)) / (16 * Math.PI * relativePosition(star).magSq() * v.STEFAN_BOLTZMANN), .25));

				float temp = getTemperatureKelvin();

				// Estimate atmospheric escape
				if(temp > sq(escapeVelocity)) {
					atmosphereDensity -= 0.0000001 * (temp - sq(escapeVelocity));
				}
				if(magneticFieldStrength < 0.01) {
					atmosphereDensity -= 0.00000001;
				}
				atmosphereDensity = Math.max(0, atmosphereDensity);
			}
			else {
				// Arbitrary temperature value for planets orbiting gas giants and black holes
				setTemperatureKelvin(orbitObject.getTemperatureKelvin());
			}
		}

		//		for(Feature feature : getTerrain().getFeatures()) {
		//			feature.updateOrReplace();
		//		}
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
		if(orbitCt.cycle()) {
			updateOrbitObject();
		}
		super.onUpdate(level);
	}

	@Override
	public void draw(RenderLevel level, float r) {
		super.draw(level, r);

		// Draw atmosphere
		float atmosRadius = (atmosphereAltitude / getRadius()) * r;
		// Temporary - render only the Karman line in white
		v.strokeWeight(1);
		v.stroke(100, 100);
		v.fill(0, 0);
		v.ellipse(0, 0, r + atmosRadius, r + atmosRadius);

	}

	protected void updateOrbitObject() {
		setOrbitObject(getWorld().findOrbitObject(this));
		getTerrain().onOrbit(orbitObject);
	}

	@Override
	public void onCollide(SpaceObject s) {
		if(s instanceof Ship) {
			Ship ship = (Ship)s;
			if(isSafeToLand(ship) && ship.isDockable(this)) {
				site.land(ship);
			}
			return; // Prevent ship from being destroyed after landing
		}
		super.onCollide(s); // Oof
	}

	public boolean isSafeToLand(Ship ship) {
		return true;
	}

	@Override
	public void onDestroyed(SpaceObject s) {
		super.onDestroyed(s);

		// If something landed on this planet, destroy it as well
		SpaceObject landed = site.getLanded();
		if(landed != null) {
			landed.onDestroyed(s);
		}

		//		// Clean up settlements
		//		for(Settlement settlement : getTerrain().findVisitableSettlements()) {
		//			settlement.cleanup();
		//		}

		// If player destroyed planet, immediately set enemy
		if(s instanceof ModularShip && ((ModularShip)s).hasController()) {
			for(Person person : getWorld().findObjects(Person.class)) {
				if(getTerrain().findVisitableSettlements().contains(person.findHome())) {
					person.die();
				}
			}

			Set<Faction> factions = getTerrain().findVisitableSettlements().stream()
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
			for(Settlement settlement : getTerrain().findVisitableSettlements()) {
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
