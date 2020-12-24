package vekta.spawner.location;

import vekta.menu.Menu;
import vekta.menu.handle.MenuHandle;
import vekta.sound.Tune;
import vekta.spawner.LocationGenerator;
import vekta.terrain.Terrain;
import vekta.terrain.location.Location;
import vekta.terrain.location.ProxyLocation;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class ProxyLocationSpawner<T extends Serializable> implements LocationGenerator.LocationSpawner {

	private static final Map<Class<? extends ProxyLocationSpawner>, ProxyLocationSpawner> PROXY_MAP = new HashMap<>();

	static {
		// Initialize all subclass instances
		LocationGenerator.ensureLoaded();
	}

	@SuppressWarnings("unchecked")
	public static <T extends Serializable> ProxyLocationSpawner<T> findProxy(Class<? extends ProxyLocationSpawner<T>> cls) {
		return PROXY_MAP.get(cls);
	}

	public ProxyLocationSpawner() {
		PROXY_MAP.put(getClass(), this);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void spawn(Terrain terrain) {
		ProxyLocation<T> location = new ProxyLocation<>(terrain.getPlanet(), (Class<? extends ProxyLocationSpawner<T>>)getClass(), chooseData());
		setup(location);
		terrain.addPathway(location);
	}

	public void setup(ProxyLocation<T> location) {
	}

	public abstract T chooseData();

	public abstract String getName(ProxyLocation<T> location);

	public abstract String getOverview(ProxyLocation<T> location);

	public int getColor(ProxyLocation<T> location) {
		return 0;
	}

	public boolean isEnabled(ProxyLocation<T> location) {
		return isValid(location.getPlanet().getTerrain());
	}

	public boolean isVisitable(ProxyLocation<T> location) {
		return true;
	}

	public void onSurveyTags(ProxyLocation<T> location, Set<String> tags) {
	}

	public MenuHandle overrideMenuHandle(ProxyLocation<T> location) {
		return null;
	}

	public void onVisitMenu(ProxyLocation<T> location, Menu menu) {
	}
}
