package vekta.spawner.location;

import vekta.Resources;
import vekta.menu.Menu;
import vekta.menu.option.RechargeButton;
import vekta.spawner.location.FeatureLocationSpawner;
import vekta.terrain.Terrain;
import vekta.terrain.location.ProxyLocation;

import java.util.Set;

import static vekta.Vekta.v;

public class GeyserLocationSpawner extends ProxyLocationSpawner<String> {

	@Override
	public boolean isValid(Terrain terrain) {
		return true;
	}

	@Override
	public float getChance(Terrain terrain) {
		return .05F;
	}

	@Override
	public String chooseData() {
		return Resources.generateString("geyser_prefix");
	}

	@Override
	public String getName(ProxyLocation<String> location) {
		return location.getData() + " " + (location.tempC() <= -20 ? "Cryogeyser" : "Geyser");
	}

	@Override
	public String getOverview(ProxyLocation<String> location) {
		return "You fly toward an absolutely magnificent geyser.";
	}

	@Override
	public void onVisitMenu(ProxyLocation<String> location, Menu menu) {
		menu.add(new RechargeButton(menu.getPlayer(), 0));
	}

	@Override
	public void onSurveyTags(ProxyLocation<String> location, Set<String> tags) {
		tags.add(getName(location) + "s");
	}
}
