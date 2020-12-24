package vekta.knowledge;

import vekta.player.Player;
import vekta.display.Layout;
import vekta.display.TextDisplay;
import vekta.display.VerticalLayout;
import vekta.object.planet.TerrestrialPlanet;
import vekta.terrain.settlement.Settlement;

import java.util.stream.Collectors;

import static processing.core.PApplet.round;
import static vekta.Vekta.*;

public class TerrestrialKnowledge extends SpaceObjectKnowledge {
	private final TerrestrialPlanet planet;

	public TerrestrialKnowledge(ObservationLevel level, TerrestrialPlanet planet) {
		super(level);

		this.planet = planet;
	}

	@Override
	public int getArchiveValue() {
		return (int)(getLevel().ordinal() * getSpaceObject().getValueScale());
	}

	@Override
	public TerrestrialPlanet getSpaceObject() {
		return planet;
	}

	@Override
	public boolean isSimilar(ObservationKnowledge other) {
		return other instanceof TerrestrialKnowledge && getSpaceObject() == ((SpaceObjectKnowledge)other).getSpaceObject();
	}

	@Override
	public void onLayout(Player player, Layout layout) {
		// Draw planet preview
		super.onLayout(player, layout);

		Layout aware = layout.add(new VerticalLayout());
		aware.customize().color(100).spacing(layout.getStyle().spacing() / 2);

		TerrestrialPlanet s = getSpaceObject();

		aware.add(new TextDisplay("Mass: " + massString(s.getMass())));
		aware.add(new TextDisplay("Radius: " + radiusString(s.getRadius())));

		// Survey info
		if(ObservationLevel.SCANNED.isAvailableFrom(getLevel())) {
			aware.add(new TextDisplay("Density: " + densityString(s.getDensity())));
			aware.add(new TextDisplay("Atmosphere: " + atmosphereString(s.getAtmosphereDensity())));
			aware.add(new TextDisplay("Temperature: " + temperatureStringCelsius(s.getTemperatureCelsius())));
			aware.add(new TextDisplay("Orbit: " + distanceString(s.relativePosition(s.getOrbitObject()).mag())));

			Layout scanned = layout.add(new VerticalLayout());

			// Terrain features
			scanned.add(new TextDisplay(String.join(", ", planet.getTerrain().findSurveyTags())));
		}

		// Landing info
		if(ObservationLevel.VISITED.isAvailableFrom(getLevel())) {
			Layout landed = layout.add(new VerticalLayout());
			landed.customize().spacing(0).color(100);

			// Settlements
			landed.add(new TextDisplay(planet.getTerrain().isHabitable()
					? planet.getTerrain().isInhabited() ?
					"Settlements: " + planet.getTerrain().findVisitableSettlements().stream().map(Settlement::getName).collect(Collectors.joining(", "))
					: "Habitable"
					: "Not Habitable"));
		}
	}
}
