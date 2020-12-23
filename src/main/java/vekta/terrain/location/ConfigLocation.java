package vekta.terrain.location;

import com.sun.org.apache.xpath.internal.operations.Bool;
import vekta.config.LocationConfig;
import vekta.object.planet.TerrestrialPlanet;
import vekta.player.Player;

import java.util.Set;

public class ConfigLocation extends Location {

	private final LocationConfig config;

	public ConfigLocation(TerrestrialPlanet planet, LocationConfig config) {
		super(planet);

		this.config = config;
	}

	@Override
	public String getName() {
		return config.name.getValue(this);
	}

	@Override
	public String getOverview() {
		return config.overview.getValue(this);
	}

	@Override
	public boolean isEnabled() {
		return config.enabled.asBoolean(this);
	}

	@Override
	public boolean isVisitable(Player player) {
		return config.visitable.asBoolean(this);
	}

	@Override
	protected void onSurveyTags(Set<String> tags) {
		if(config.tag.hasValue(this)) {
			tags.add(config.tag.getValue(this));
		}
	}
}
