package vekta.terrain;

import vekta.ecosystem.Ecosystem;
import vekta.menu.Menu;
import vekta.menu.option.SurveyButton;
import vekta.object.SpaceObject;
import vekta.object.planet.TerrestrialPlanet;
import vekta.spawner.TuneGenerator;
import vekta.terrain.location.Location;

import static vekta.Vekta.v;

/**
 * A location representing the overall visible terrain of a planet.
 */
public abstract class Terrain extends Location {
	private final Ecosystem ecosystem;

	public Terrain(TerrestrialPlanet planet) {
		super(planet);

		ecosystem = new Ecosystem(planet.getAtmosphereDensity() * v.random(1e7F, 1e9F));

		setWittyText("Welcome to");
		setTune(TuneGenerator.randomTune());
	}

	public Ecosystem getEcosystem() {
		return ecosystem;
	}

	@Override
	public String getName() {
		return getPlanet().getName();
	}

	@Override
	protected final void onAddPathway(Location location, String name) {
		if(location.isVisitable()) {
			LandingSite site = new LandingSite(location);
			getPlanet().addLandingSite(site);
		}
	}

	@Override
	protected final void onSetupMenu(Menu menu) {
		onVisitTerrainMenu(menu);

		menu.add(new SurveyButton(getPlanet()));
	}

	protected void onVisitTerrainMenu(Menu menu) {
	}

	public final void updateOrbit(SpaceObject orbitObject) {
		setColor(getPlanet().getColor());

		onOrbit(orbitObject);
	}

	protected void onOrbit(SpaceObject orbitObject) {
	}
}
