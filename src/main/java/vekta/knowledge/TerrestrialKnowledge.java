package vekta.knowledge;

import vekta.Player;
import vekta.object.planet.TerrestrialPlanet;
import vekta.overlay.singleplayer.TelemetryOverlay;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static vekta.Vekta.v;

public class TerrestrialKnowledge extends SpaceObjectKnowledge {
	private final TerrestrialPlanet planet;

	public TerrestrialKnowledge(KnowledgeLevel level, TerrestrialPlanet planet) {
		super(level);

		this.planet = planet;
	}

	@Override
	public TerrestrialPlanet getSpaceObject() {
		return planet;
	}

	@Override
	public void draw(Player player, float width, float height) {
		// Draw planet preview
		super.draw(player, width, height);

		// Survey info
		if(KnowledgeLevel.SCANNED.isAvailableFrom(getLevel())) {
			v.fill(100);
			
			v.text("Mass: " + TelemetryOverlay.getMassString(getSpaceObject().getMass()), 0, 0);
			v.text("Radius: " + TelemetryOverlay.getDistanceString(getSpaceObject().getRadius()), 0, SPACING);

			// Translate for next group of information
			v.translate(0, SPACING * 3);
		}

		// Landing info
		if(KnowledgeLevel.VISITED.isAvailableFrom(getLevel())) {
			v.fill(getColor(player));

			List<String> features = planet.getTerrain().getFeatures();
			List<String> settlements = player.findKnowledge(SettlementKnowledge.class).stream()
					.filter(o -> o.getSettlement().getParent() == getSpaceObject())
					.map(o -> o.getSettlement().getName())
					.collect(Collectors.toList());

			v.text(buildMultilineString(features, "Features", width), 0, 0);

			String settlementString = planet.isHabitable()
					? buildMultilineString(settlements, "Settlements", width)
					: "Not Habitable";
			v.text(settlementString, 0, SPACING * 2 + (24 * numberOfLines(buildMultilineString(features, "Features", width))));
		}
	}

	// TODO: make a helper class so we can encapsulate and reuse this logic elsewhere
	private String buildMultilineString(List<String> words, String description, float width) {
		if(words.isEmpty()) {
			words.add("(None)");
		}

		List<String> lines = new ArrayList<>();
		int currentLineIndex = 0;
		int currentWordIndex = 0;
		lines.add(description + ": ");
		for(String word : words) {
			if(v.textWidth(lines.get(currentLineIndex) + ", " + word) > width) {
				currentLineIndex++;
				lines.add("");
			}
			if(currentWordIndex != words.size() - 1) {
				lines.set(currentLineIndex, lines.get(currentLineIndex) + word + ", ");
			}
			else {
				lines.set(currentLineIndex, lines.get(currentLineIndex) + word);
			}

			currentWordIndex++;
		}
		return String.join("\n", lines);
	}

	private int numberOfLines(String in) {
		int count = 0;
		for(char c : in.toCharArray()) {
			if(c == '\n')
				count++;
		}
		return count;
	}
}
