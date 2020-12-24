package vekta.mission.objective;

import vekta.menu.Menu;
import vekta.menu.handle.SurveyMenuHandle;
import vekta.object.SpaceObject;

import java.util.List;
import java.util.Set;

public class SurveyObjective extends Objective {
	private final String tag;

	public SurveyObjective(String tag) {
		this.tag = tag;
	}

	public String getTag() {
		return tag;
	}

	@Override
	public String getName() {
		return "Perform a survey (" + getTag() + ")";
	}

	@Override
	public SpaceObject getSpaceObject() {
		return null;
	}

	@Override
	public void onMenu(Menu menu) {
		if(menu.getHandle() instanceof SurveyMenuHandle) {
			Set<String> tags = ((SurveyMenuHandle)menu.getHandle()).getTerrain().findSurveyTags();
			if(tags.contains(getTag())) {
				complete();
			}
		}
	}
}
