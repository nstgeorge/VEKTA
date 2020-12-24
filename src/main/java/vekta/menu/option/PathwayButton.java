package vekta.menu.option;

import vekta.menu.Menu;
import vekta.terrain.location.Location;

public class PathwayButton extends ButtonOption {
	private final Location.Pathway pathway;

	public PathwayButton(Location.Pathway pathway) {
		this.pathway = pathway;
	}

	@Override
	public String getName() {
		return pathway.getName();
	}

	@Override
	public int getColor() {
		return pathway.getLocation().getColor();
	}

	@Override
	public void onSelect(Menu menu) {
		pathway.travel(menu.getPlayer());
	}
}
