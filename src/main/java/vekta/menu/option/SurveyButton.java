package vekta.menu.option;

import vekta.menu.Menu;
import vekta.menu.handle.SurveyMenuHandle;
import vekta.terrain.LandingSite;

import static vekta.Vekta.setContext;

public class SurveyButton implements ButtonOption {
	private final LandingSite site;

	public SurveyButton(LandingSite site) {
		this.site = site;
	}

	public LandingSite getSite() {
		return site;
	}

	@Override
	public String getName() {
		return "Survey";
	}

	@Override
	public void onSelect(Menu menu) {
		Menu sub = new Menu(menu, new SurveyMenuHandle(getSite()));
		sub.addDefault();
		setContext(sub);
	}
}
