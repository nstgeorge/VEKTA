package vekta.knowledge;

import vekta.Player;
import vekta.display.Layout;
import vekta.display.TextDisplay;
import vekta.display.VerticalLayout;
import vekta.object.planet.TerrestrialPlanet;
import vekta.overlay.singleplayer.TelemetryOverlay;
import vekta.terrain.settlement.Settlement;

import java.util.stream.Collectors;

import static vekta.Vekta.distanceString;

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

		aware.add(new TextDisplay("Mass: " + TelemetryOverlay.getMassString(getSpaceObject().getMass())));
		aware.add(new TextDisplay("Radius: " + distanceString(getSpaceObject().getRadius())));

		// Survey info
		if(ObservationLevel.SCANNED.isAvailableFrom(getLevel())) {
			Layout scanned = layout.add(new VerticalLayout());

			// Terrain features
			scanned.add(new TextDisplay(String.join(", ", planet.getTerrain().getFeatures())));
		}

		// Landing info
		if(ObservationLevel.VISITED.isAvailableFrom(getLevel())) {
			Layout landed = layout.add(new VerticalLayout());
			landed.customize().spacing(0).color(100);

			// Settlements
			landed.add(new TextDisplay(planet.isHabitable()
					? "Settlements: " + planet.getTerrain().getSettlements().stream().map(Settlement::getName).collect(Collectors.joining(", "))
					: "Not Habitable"));
		}
	}
}
