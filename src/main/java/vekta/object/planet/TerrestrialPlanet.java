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

import java.util.Set;
import java.util.stream.Collectors;

import static processing.core.PApplet.print;
import static vekta.Vekta.*;

/**
 * Terrestrial (landable) planet
 */
public class TerrestrialPlanet extends Planet {
	// TODO: gradually move randomization into `StarSystemGenerator`

	//	private static final float LABEL_THRESHOLD = 5e4F;

	private static final float KARMAN_LINE = 1e5F;

	private LandingSite site;

	private SpaceObject orbitObject;

	private final Counter orbitCt = new Counter(10).randomize();

	private ObservationLevel levelCache;

	private final float atmosphereDensity;               // Atmospheric density (kg/m^3)
	private final float magneticFieldStrength;     // Strength of the magnetic field

	public TerrestrialPlanet(String name, float mass, float density, PVector position, PVector velocity, int color) {
		super(name, mass, density, position, velocity, color);

		// Set atmospheric density at "sea level"
		atmosphereDensity = chooseAtmosphereDensity();

		// 30% chance to have a magnetic field, then randomly set a normally distributed value
		magneticFieldStrength = v.chance(.3f) ? 0 : Math.abs(v.gaussian(50));

		this.site = new LandingSite(this, createTerrain());

		updateOrbitObject();
	}

	protected float chooseAtmosphereDensity() {
		return Math.abs(1 + v.gaussian(1)) * (getMass() / v.EARTH_MASS);
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

	// Squared to avoid computing the square root when possible
	public float getEscapeVelocitySquared() {
		return 2 * G * getMass() / getRadius();
	}

	public float getEscapeVelocity() {
		return v.sqrt(getEscapeVelocitySquared());
	}

	/**
	 * Returns the "sea level" density of the atmosphere relative to Earth.
	 *
	 * @return Thickness in atmospheres (1 atm = 101,325 kPa)
	 */
	public float getAtmosphereDensity() {
		return atmosphereDensity;
	}

	/**
	 * Returns an arbitrary ceiling of the atmosphere similar to Earth's Karman line
	 *
	 * @return Maximum radius of atmosphere (meters)
	 */
	public float getAtmosphereRadius() {
		// TODO: compute by solving atmosphere density for a cutoff value, such as 1e-5 kg/m^3
		return KARMAN_LINE * atmosphereDensity * getRadius() / EARTH_RADIUS;
	}

	/**
	 * Returns the density of the atmosphere at a given altitude.
	 *
	 * @return Thickness in atmospheres (1 atm = 101,325 kPa)
	 */
	public float getAtmosphereDensity(float altitude) {
		if(altitude == 0) {
			return getAtmosphereDensity();
		}
		// Equation: https://en.wikipedia.org/wiki/Density_of_air ("Exponential approximation")

		float gravityAccel = G * getMass() / sq(getRadius() + altitude);// Surface gravity
		float molarMass = .02896f;// Arbitrary molar mass of planet's air
		float lapseRate = .0065f;// Temperature lapse rate
		float tempK = getTemperatureKelvin();// Surface temperature
		tempK = 273;/////TEMP
		gravityAccel = 9.8f;//TEMP
		return atmosphereDensity * exp(-(molarMass * gravityAccel / GAS_CONSTANT - lapseRate) / tempK * altitude);
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

				/// Commenting this out for now because it might affect the new calculations. Feel free to uncomment if this is important

				//				float tempK = getTemperatureKelvin();
				//				float escapeVelocitySq = getEscapeVelocitySquared();
				//
				//				// Estimate atmospheric escape
				//				if(tempK > escapeVelocitySq) {
				//					atmosphereDensity -= 1e-7f * (tempK - escapeVelocitySq);
				//				}
				//				if(magneticFieldStrength < 0.01) {
				//					atmosphereDensity -= 1e-7f;
				//				}
				//				atmosphereDensity = Math.max(0, atmosphereDensity);
			}
			else {
				// Arbitrary temperature value for planets orbiting gas giants and black holes
				setTemperatureKelvin(orbitObject.getTemperatureKelvin());
			}
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
		if(orbitCt.cycle()) {
			updateOrbitObject();
		}
		super.onUpdate(level);
	}

	@Override
	public void draw(RenderLevel level, float r) {
		super.draw(level, r);

		// Draw atmosphere
		float atmosphereRadius = (getAtmosphereRadius() / getRadius()) * r;
		// Temporary - render only the Karman line
		v.strokeWeight(1);
		v.stroke(100, 100);
		v.fill(0, 0);
		v.ellipse(0, 0, r + atmosphereRadius, r + atmosphereRadius);

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
