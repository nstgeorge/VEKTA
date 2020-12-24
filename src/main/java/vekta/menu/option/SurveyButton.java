package vekta.menu.option;

import vekta.menu.Menu;
import vekta.menu.handle.SurveyMenuHandle;
import vekta.terrain.LandingSite;
import vekta.terrain.Terrain;

import static vekta.Vekta.setContext;

public class SurveyButton extends ButtonOption {
	private final Terrain terrain;

	public SurveyButton(Terrain terrain) {
		this.terrain = terrain;
	}

	public Terrain getTerrain() {
		return terrain;
	}

	@Override
	public String getName() {
		return "Survey";
	}

	@Override
	public void onSelect(Menu menu) {
		Menu sub = new Menu(menu, new SurveyMenuHandle(getTerrain()));
		sub.addDefault();
		setContext(sub);
	}
}
