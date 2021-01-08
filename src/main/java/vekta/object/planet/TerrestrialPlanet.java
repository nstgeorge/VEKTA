package vekta.object.planet;

import processing.core.PVector;
import sun.font.SunFontManager;
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
import vekta.terrain.location.Location;
import vekta.terrain.settlement.Settlement;
import vekta.util.Counter;
import vekta.world.RenderLevel;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static vekta.Vekta.*;

/**
 * Terrestrial (landable) celestial body
 */
public class TerrestrialPlanet extends Planet {
	// TODO: gradually move randomization into `StarSystemGenerator`

	private static final int SURFACE_DETAIL = 200; // Number of lines used to draw a planet's surface
	private static final float MAX_LOCATION_ICON_SCALE = 40;
	private static final float MAX_ATMOSPHERE_RADIUS = 2;
	private static final float LANDING_SITE_ANGLE = .1f; // Max deviation from the landing site's angle around the planet (radians)

	private static final float LANDING_SITE_RADIUS = 2 * sin(LANDING_SITE_ANGLE / 2); // Chord length of landing angle, precomputed

	private final Terrain terrain;
	private final LandingSite terrainSite;
	private final Map<LandingSite, Float> landingMap = new HashMap<>();
	private final List<Settlement> settlements = new ArrayList<>();

	private final float[] surfaceCache = new float[SURFACE_DETAIL];
	private float surfaceAverage;

	private final Counter orbitCt = new Counter(10).randomize();

	private SpaceObject orbitObject;
	private ObservationLevel levelCache;

	private float atmosphereDensity;    // Atmospheric density (kg/m^3)
	private float magneticStrength;    // Strength of the magnetic field
	private float rotationHours;        // Rotation period (hours)

	private float direction; // Current rotation direction

	public TerrestrialPlanet(String name, float mass, float density, PVector position, PVector velocity, int color) {
		super(name, mass, density, position, velocity, color);

		atmosphereDensity = chooseAtmosphereDensity();
		magneticStrength = chooseMagneticStrength();
		rotationHours = chooseRotationHours();

		terrain = chooseTerrain();
		terrainSite = new LandingSite(terrain);

		updateOrbitObject();
		notifySurfaceChanged();

		// TODO: tidally lock if close to orbited object
	}

	protected float chooseAtmosphereDensity() {
		// Normally distributed and proportional to mass
		return Math.abs(v.gaussian(1)) * (getMass() / EARTH_MASS);
	}

	protected float chooseMagneticStrength() {
		// 30% chance to have a magnetic field, then set a normally distributed value
		return v.chance(.3f) ? 0 : Math.abs(v.gaussian(50));
	}

	protected float chooseRotationHours() {
		// Randomize based on typical rotations in the Solar System
		return (24 + v.gaussian(4)) * (v.chance(.2f) ? -1 : 1);
	}

	protected Terrain chooseTerrain() {
		return TerrainGenerator.createTerrain(this);
	}

	public Terrain getTerrain() {
		return terrain;
	}

	public LandingSite getDefaultLandingSite() {
		return terrainSite;
	}

	private Stream<LandingSite> streamAllLandingSites() {
		return Stream.concat(Stream.of(terrainSite), landingMap.keySet().stream());
	}

	public List<LandingSite> findVisitableLandingSites() {
		return streamAllLandingSites()
				.filter(site -> site.getLocation().isEnabled() && site.getLocation().isVisitable())
				.collect(Collectors.toList());
	}

	/**
	 * Find a suitable landing site for a global position in space.
	 *
	 * @param position Position in global coordinates
	 * @return The nearest accessible landing site, otherwise the default terrain
	 */
	public LandingSite findLandingSiteGlobal(PVector position) {
		return findLandingSite(position.copy().sub(getPositionReference()).heading());
	}

	public LandingSite findLandingSite(float angle) {
		angle -= getDirection();

		float bestDist = LANDING_SITE_ANGLE * 2; // Extra landing space around visual radius
		LandingSite site = getDefaultLandingSite();
		for(Map.Entry<LandingSite, Float> entry : landingMap.entrySet()) {
			Location location = entry.getKey().getLocation();
			if(location.isEnabled()) {
				float dist = abs(v.deltaAngle(angle, entry.getValue()));
				if(dist < bestDist) {
					bestDist = dist;
					site = entry.getKey();
				}
			}
		}
		return site;
	}

	public void addLandingSite(LandingSite site) {
		addLandingSite(site, v.random(TWO_PI));
	}

	public void addLandingSite(LandingSite site, float angle) {
		landingMap.put(site, v.normalizeAngle(angle - getDirection()));
		notifySurfaceChanged();
	}

	public void removeLandingSite(LandingSite site) {
		landingMap.remove(site);
		notifySurfaceChanged();
	}

	public boolean isInhabited() {
		// TODO: optimize if we start using this often
		return !findInhabitedSettlements().isEmpty();
	}

	public final List<Settlement> getAllSettlements() {
		return settlements;
	}

	public List<Settlement> findVisitableSettlements() {
		return settlements.stream()
				.filter(settlement -> settlement.getLocation().isEnabled() && settlement.getLocation().isVisitable() && settlement.getLocation().isHabitable())
				.collect(Collectors.toList());
	}

	public List<Settlement> findInhabitedSettlements() {
		return settlements.stream()
				.filter(settlement -> settlement.getLocation().isEnabled() && settlement.getLocation().isVisitable() && settlement.isInhabited())
				.collect(Collectors.toList());
	}

	public void notifySettlement(Settlement settlement) {
		if(!settlements.contains(settlement)) {
			settlements.add(settlement);
		}
	}

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

	@Override
	public float getOnScreenRadius(float r) {
		return r * getAtmosphereRadius() / getRadius();
	}

	/**
	 * Return the "sea level" density of the atmosphere relative to Earth.
	 *
	 * @return Thickness in atmospheres (1 atm = 101,325 kPa)
	 */
	public float getAtmosphereDensity() {
		return atmosphereDensity;
	}

	/**
	 * Return the density of the atmosphere at a given altitude.
	 *
	 * @return Thickness in atmospheres (1 atm = 101,325 kPa)
	 */
	public float getAtmosphereDensity(float altitude) {
		if(altitude <= 0) {
			return atmosphereDensity;
		}
		// Equation: https://en.wikipedia.org/wiki/Density_of_air ("Exponential approximation")

		double gravityAccel = G * getMass() / sq(getRadius() + altitude); // Surface gravity
		double molarMass = .02896f; // Arbitrary molar mass of planet's air
		double lapseRate = .0065f; // Temperature lapse rate
		double tempK = getTemperatureKelvin(); // Surface temperature
		float atmDensity = (float)(atmosphereDensity * Math.exp(-(molarMass * gravityAccel / GAS_CONSTANT - lapseRate) / tempK * altitude));
		return Float.isNaN(atmDensity) ? 0 : min(atmosphereDensity, atmDensity);
	}

	public void setAtmosphereDensity(float atmosphereDensity) {
		this.atmosphereDensity = atmosphereDensity;
	}

	/**
	 * Return an arbitrary ceiling of the atmosphere used for glide mode.
	 * Note that this is relative to the center of the planet instead of the surface.
	 *
	 * @return Maximum radius of atmosphere (meters)
	 */
	public float getAtmosphereRadius() {
		return getRadius() * min(MAX_ATMOSPHERE_RADIUS, (1 + (EARTH_ATMOSPHERE_LIMIT / EARTH_RADIUS) * atmosphereDensity));
	}

	public float getMagneticStrength() {
		return magneticStrength;
	}

	public void setMagneticStrength(float magneticStrength) {
		this.magneticStrength = magneticStrength;
	}

	public float getRotationSeconds() {
		return rotationHours * 3600;
	}

	public float getRotationHours() {
		return rotationHours;
	}

	public void setRotationHours(float rotationHours) {
		this.rotationHours = rotationHours;
	}

	public float getValueScale() {
		return 1;
	}

	/**
	 * Calculate the current rotational angle of the planet.
	 */
	public float getDirection() {
		return direction;
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
				setTemperatureKelvin((float)Math.pow((star.getLuminosity() * (1 - .3)) / (16 * Math.PI * relativePosition(star).magSq() * STEFAN_BOLTZMANN), .25));

				/// Commenting this out for now. Feel free to uncomment if everything still makes sense

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

		// Update rotation (right-hand-rule convention flips the sign)
		direction -= TWO_PI / getRotationSeconds() * getWorld().getTimeScale() / v.frameRate; // TODO: something like deltaTime()
	}

	protected void updateOrbitObject() {
		setOrbitObject(getWorld().findOrbitObject(this));
		getTerrain().updateOrbit(orbitObject);
	}

	public float getDisplacementScale() {
		//		return min(.2f, 1 / log(getMass()));
		return min(.2f, 5 / log(getMass())); // Exaggerate a bit for now
	}

	// Accurate but potentially slow
	public float getRadius(float angle) {
		return getRadius() * (1 + (getTerrain().getDisplacement(angle - getDirection()) - surfaceAverage) * getDisplacementScale());
	}

	// Less accurate but always very efficient
	public float getApproximateRadius(float angle) {
		return getRadius() * (1 + surfaceCache[Math.round(v.normalizeAngle(angle - getDirection()) * surfaceCache.length / TWO_PI)]);
	}

	@Override
	public boolean collidesWith(RenderLevel level, SpaceObject s) {
		if(s instanceof Ship) {
			PVector offset = s.relativePosition(this);
			return offset.magSq() < sq(getRadius(offset.heading()) + s.getRadius());
		}
		return super.collidesWith(level, s);
	}

	public void notifySurfaceChanged() {
		if(terrain == null) {
			return;
		}
		int len = surfaceCache.length;
		float sum = 0;
		for(int i = 0; i < len; i++) {
			float displacement = getTerrain().getDisplacement(TWO_PI * i / len);
			surfaceCache[i] = displacement;
			sum += displacement;
		}
		surfaceAverage = sum / surfaceCache.length;
		for(int i = 0; i < len; i++) {
			// Ensure that average displacement is zero
			surfaceCache[i] = (surfaceCache[i] - surfaceAverage) * getDisplacementScale();
		}
	}

	@Override
	public void drawNearby(float r) {
		float direction = getDirection();
		if(r > 10) {
			int len = surfaceCache.length;
			float x = r * (1 + surfaceCache[0]);
			float y = 0;
			for(int i = 1; i <= len; i++) {
				float angle = TWO_PI * i / len;
				float elevation = r * (1 + surfaceCache[i % len]);
				float x1 = cos(angle) * elevation;
				float y1 = sin(angle) * elevation;
				v.line(x, y, x1, y1);
				x = x1;
				y = y1;
			}
			drawLabel(r);
		}
		else {
			super.drawNearby(r);
		}

		// Draw atmosphere
		float atmosphereRadius = r * (getAtmosphereRadius() / getRadius());
		v.strokeWeight(1);
		v.stroke(100, 100);
		v.noFill();
		v.ellipse(0, 0, atmosphereRadius, atmosphereRadius);

		// Draw landing locations
		float rSite = min(MAX_LOCATION_ICON_SCALE, r * LANDING_SITE_RADIUS); // Chord length
		float radiusScale = r / getRadius();
		for(Map.Entry<LandingSite, Float> entry : landingMap.entrySet()) {
			Location location = entry.getKey().getLocation();
			float angle = entry.getValue();

			v.pushMatrix();
			v.rotate(angle + direction - HALF_PI); // Y axis
			v.translate(0, getRadius(angle) * radiusScale);
			v.stroke(location.getColor());
			location.draw(rSite);
			v.popMatrix();
		}
	}

	@Override
	public void onCollide(SpaceObject s) {
		if(s instanceof Ship) {
			Ship ship = (Ship)s;
			if(ship.isDockable(this)) {
				LandingSite site = findLandingSiteGlobal(ship.getPositionReference());
				if(site != null) {
					site.land(ship);
				}
			}
			return; // Prevent ship from being destroyed after landing
		}
		super.onCollide(s); // Splat
	}

	@Override
	public void onDestroyed(SpaceObject s) {
		super.onDestroyed(s);

		streamAllLandingSites().forEach(site -> {
			// If something landed on this planet, destroy it as well
			SpaceObject landed = site.getLanded();
			if(landed != null) {
				landed.onDestroyed(s);
			}
		});

		// If player destroyed planet, immediately set enemy
		if(s instanceof ModularShip && ((ModularShip)s).hasController()) {
			for(Person person : getWorld().findObjects(Person.class)) {
				Settlement home = person.getCurrentHome();
				if(home != null && home.getLocation().getPlanet() == this) {
					person.die();
				}
			}

			Set<Faction> factions = settlements.stream()
					.map(Settlement::getFaction)
					.collect(Collectors.toSet());

			for(Faction faction : factions) {
				float value = faction.getEconomy().getValue();
				faction.setEnemy(((ModularShip)s).getController().getFaction());
				faction.getEconomy().addModifier(
						new TemporaryModifier("Destruction of " + getName(), -value, value / 10));
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
			for(Settlement settlement : findVisitableSettlements()) {
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
