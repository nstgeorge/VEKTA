package vekta.terrain;

import vekta.knowledge.ObservationLevel;
import vekta.knowledge.SpaceObjectKnowledge;
import vekta.menu.Menu;
import vekta.menu.option.FastTravelMenuButton;
import vekta.menu.option.RenameButton;
import vekta.object.planet.BlackHole;

public class BlackHoleTerrain extends Terrain {

	@Override
	public String getOverview() {
		return "You carefully approach the black hole.";
	}

	@Override
	public void setupLandingMenu(LandingSite site, Menu menu) {
		// TODO convert items to energy by mass (Penrose Process)
		// TODO timewarp

		if(site.getParent() instanceof BlackHole) {
			BlackHole hole = (BlackHole)site.getParent();

			menu.add(new FastTravelMenuButton(hole));
			if(menu.getPlayer().hasKnowledge(SpaceObjectKnowledge.class, k -> k.getSpaceObject() == hole && k.getLevel() == ObservationLevel.OWNED)) {
				menu.add(new RenameButton(hole));
			}
		}
	}
}
