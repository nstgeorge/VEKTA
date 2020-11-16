package vekta.menu.option;

import vekta.knowledge.SpaceObjectKnowledge;
import vekta.menu.Menu;
import vekta.menu.handle.MenuHandle;
import vekta.object.planet.BlackHole;
import vekta.object.planet.Planet;

import java.util.Comparator;
import java.util.List;

import static vekta.Vekta.getWorld;
import static vekta.Vekta.setContext;

public class FastTravelMenuButton extends ButtonOption {
	private final BlackHole from;

	public FastTravelMenuButton(BlackHole from) {
		this.from = from;
	}

	@Override
	public String getName() {
		return "Wormhole Network";
	}

	@Override
	public void onSelect(Menu menu) {
		Menu sub = new Menu(menu, new MenuHandle());
		List<BlackHole> targets = getWorld().findObjects(BlackHole.class);
		targets.sort(Comparator.comparing(Planet::getName));
		for(BlackHole target : targets) {
			if(target != from && menu.getPlayer().hasKnowledge(SpaceObjectKnowledge.class, k -> k.getSpaceObject() == target)) {
				sub.add(new FastTravelButton(menu.getPlayer(), target));
			}
		}
		sub.addDefault();
		setContext(sub);
	}
}
