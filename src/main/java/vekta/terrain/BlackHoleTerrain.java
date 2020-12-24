package vekta.terrain;

import vekta.knowledge.ObservationLevel;
import vekta.knowledge.SpaceObjectKnowledge;
import vekta.menu.Menu;
import vekta.menu.option.FastTravelMenuButton;
import vekta.menu.option.RenameButton;
import vekta.object.planet.BlackHole;
import vekta.object.planet.TerrestrialPlanet;
import vekta.player.Player;

import java.util.Set;

public class BlackHoleTerrain extends Terrain {

	public BlackHoleTerrain(TerrestrialPlanet planet) {
		super(planet);
	}

	@Override
	public String getOverview() {
		return "You carefully approach the black hole.";
	}

	@Override
	public boolean isHabitable() {
		return false;
	}

	@Override
	public void onSurveyTags(Set<String> tags) {
		tags.add("Treacherous");
	}

	@Override
	public void onVisitTerrainMenu(Menu menu) {
		// TODO convert items to energy by mass (Penrose Process)
		// TODO timewarp

		if(getPlanet() instanceof BlackHole) {
			BlackHole hole = (BlackHole)getPlanet();

			menu.add(new FastTravelMenuButton(hole));
			if(menu.getPlayer().hasKnowledge(SpaceObjectKnowledge.class, k -> k.getSpaceObject() == hole && k.getLevel() == ObservationLevel.OWNED)) {
				menu.add(new RenameButton(hole));
			}
		}
	}
}
