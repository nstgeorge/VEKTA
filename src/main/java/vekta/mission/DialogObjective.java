package vekta.mission;

import vekta.object.SpaceObject;
import vekta.person.Dialog;
import vekta.person.Person;
import vekta.terrain.LandingSite;

public class DialogObjective extends Objective {
	private final Dialog dialog;

	public DialogObjective(Dialog dialog) {
		this.dialog = dialog;
	}

	public Dialog getDialog() {
		return dialog;
	}

	public Person getPerson() {
		return getDialog().getPerson();
	}

	@Override
	public String getName() {
		return "Talk to " + getPerson().getBirthName() + (getSpaceObject() != null ? " at " + getSpaceObject().getName() : "");
	}

	@Override
	public SpaceObject getSpaceObject() {
		return getPerson().getHomeObject();
	}

	@Override
	public void onLand(LandingSite site) {
		if(site.getParent() == getSpaceObject()) {
			complete();
		}
	}
}
