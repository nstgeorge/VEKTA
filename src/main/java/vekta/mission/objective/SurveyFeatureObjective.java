package vekta.mission.objective;

import vekta.menu.Menu;
import vekta.menu.handle.SurveyMenuHandle;
import vekta.object.SpaceObject;

public class SurveyFeatureObjective extends Objective {
	private final String feature;

	public SurveyFeatureObjective(String feature) {
		this.feature = feature;
	}

	public String getFeature() {
		return feature;
	}

	@Override
	public String getName() {
		return "Perform a survey (required feature: " + getFeature() + ")";
	}

	@Override
	public SpaceObject getSpaceObject() {
		return null;
	}

	@Override
	public void onMenu(Menu menu) {
		if(menu.getHandle() instanceof SurveyMenuHandle) {
			if(((SurveyMenuHandle)menu.getHandle()).getSite().getTerrain().hasFeature(feature)) {
				
			}
		}
	}
}
