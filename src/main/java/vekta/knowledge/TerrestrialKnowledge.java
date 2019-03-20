package vekta.knowledge;

import vekta.Player;
import vekta.display.*;
import vekta.object.planet.TerrestrialPlanet;
import vekta.overlay.singleplayer.TelemetryOverlay;
import vekta.terrain.settlement.Settlement;

import java.util.stream.Collectors;

public class TerrestrialKnowledge extends SpaceObjectKnowledge {
	private final TerrestrialPlanet planet;

	private final DisplayStyle style = new DisplayStyle();
	private final Layout layout = new VerticalLayout(style);

	// Scanner display items
	private final Toggle<Layout> scanToggle = layout.add(new Toggle<>(new VerticalLayout(style)));
	private final TextDisplay mass = scanToggle.getDisplay().add(new TextDisplay(style));
	private final TextDisplay radius = scanToggle.getDisplay().add(new TextDisplay(style));

	// Landing display items
	private final Toggle<Layout> landToggle = layout.add(new Toggle<>(new VerticalLayout(style)));
	private final TextDisplay features = landToggle.getDisplay().add(new TextDisplay(style));
	private final TextDisplay settlements = landToggle.getDisplay().add(new TextDisplay(style));

	public TerrestrialKnowledge(ObservationLevel level, TerrestrialPlanet planet) {
		super(level);

		this.planet = planet;
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
	public void draw(Player player, float width, float height) {
		// Draw planet preview
		super.draw(player, width, height);

		// Configure style
		style.color(getColor(player));
		landToggle.getDisplay().customize().spacing(0).color(100);
		
		// Survey info
		//		if(scanToggle.setVisible(ObservationLevel.SCANNED.isAvailableFrom(getLevel()))) {
		mass.customize().spacing(0);
		mass.setText("Mass: " + TelemetryOverlay.getMassString(getSpaceObject().getMass()));
		radius.customize().spacing(0);
		radius.setText("Radius: " + TelemetryOverlay.getDistanceString(getSpaceObject().getRadius()));
		//		}

		// Landing info
		//		if(landToggle.setVisible(ObservationLevel.VISITED.isAvailableFrom(getLevel()))) {
		features.setText(String.join(", ", planet.getTerrain().getFeatures()));

		settlements.setText(planet.isHabitable()
				? "Settlements: " + planet.getTerrain().getSettlements().stream().map(Settlement::getName).collect(Collectors.joining(", "))
				: "Not Habitable");
		//		}

		layout.draw(width, height);
	}
}
