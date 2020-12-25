package vekta.spawner.location;

import vekta.Resources;
import vekta.menu.Menu;
import vekta.terrain.Terrain;
import vekta.terrain.location.ProxyLocation;

import java.util.Set;

import static vekta.Vekta.v;

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
	public String chooseData(Terrain terrain) {
		return Resources.generateString("beacon");
	}

	@Override
	public String getName(ProxyLocation<String> location) {
		return corrupt(location.getData());
	}

	@Override
	public String getOverview(ProxyLocation<String> location) {
		return corrupt("You land your ship and carefully approach the signal source.");
	}

	@Override
	public void onSurveyTags(ProxyLocation<String> location, Set<String> tags) {
		tags.add(location.getData());
	}

	@Override
	public void onVisitMenu(ProxyLocation<String> location, Menu menu) {
		// TODO
	}

	public String corrupt(String text) {
		if(v.chance(.1f)) {
			for(int i = 0; i < 3; i++) {
				text = text.replace(text.charAt((int)v.random(text.length())), (char)v.random(128));
			}
		}
		return text;
	}
}
