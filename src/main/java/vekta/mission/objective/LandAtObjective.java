package vekta.mission.objective;

import vekta.menu.Menu;
import vekta.object.SpaceObject;
import vekta.terrain.Terrain;
import vekta.terrain.location.Location;

public class LandAtObjective extends Objective {
	private final SpaceObject object;

	public LandAtObjective(SpaceObject object) {
		if(object == null) {
			throw new RuntimeException("Landing target cannot be null");
		}
		this.object = object;
	}

	@Override
	public String getName() {
		return "Land at " + getSpaceObject().getName();
	}

	@Override
	public SpaceObject getSpaceObject() {
		return object;
	}

	@Override
	public void onMenu(Menu menu) {
		// TODO: check more frequently than opening menus
		if(getSpaceObject().isDestroyed()) {
			cancel();
		}
	}

	@Override
	public void onVisit(Terrain terrain) {
		if(terrain.getPlanet() == getSpaceObject()) {
			complete();
		}
	}

	@Override
	public void onSync(Objective data) {
		super.onSync(data);
		
		onMenu(null);/////
	}
}
