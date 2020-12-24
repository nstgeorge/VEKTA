package vekta.terrain.location;

import vekta.config.LocationConfig;
import vekta.object.planet.TerrestrialPlanet;

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
	public boolean isVisitable() {
		return config.visitable.asBoolean(this);
	}

	@Override
	protected void onSurveyTags(Set<String> tags) {
		if(config.tag.hasValue(this)) {
			tags.add(config.tag.getValue(this));
		}
	}
}
