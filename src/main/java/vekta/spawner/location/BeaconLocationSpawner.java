package vekta.spawner.location;

import vekta.Resources;
import vekta.menu.Menu;
import vekta.terrain.Terrain;
import vekta.terrain.location.ProxyLocation;

import java.util.Set;

public class BeaconLocationSpawner extends ProxyLocationSpawner<String> {

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
		return Resources.generateString("beacon");
	}

	@Override
	public String getName(ProxyLocation<String> location) {
		return location.getData();
	}

	@Override
	public String getOverview(ProxyLocation<String> location) {
		return "You land your ship and carefully approach the signal source.";
	}

	@Override
	public void onSurveyTags(ProxyLocation<String> location, Set<String> tags) {
		tags.add(location.getName());
	}

	@Override
	public void onVisitMenu(ProxyLocation<String> location, Menu menu) {
		// TODO
	}
}
