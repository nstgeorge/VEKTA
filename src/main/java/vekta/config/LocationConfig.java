package vekta.config;

import vekta.logic.LocationLogic;

public class LocationConfig extends Config {

	public LocationLogic<String> name = new LocationLogic<>("??");
	public LocationLogic<String> overview = new LocationLogic<>("??");
	public LocationLogic<String> tag = new LocationLogic<>(null);

	public LocationLogic<?> enabled = new LocationLogic<>();
	public LocationLogic<?> visitable = new LocationLogic<>();
}
