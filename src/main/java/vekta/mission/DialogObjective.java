package vekta.mission;

import vekta.menu.Menu;
import vekta.menu.handle.DialogMenuHandle;
import vekta.menu.option.DialogOption;
import vekta.object.SpaceObject;
import vekta.person.Dialog;
import vekta.person.Person;

public class DialogObjective extends Objective {
	private final String verb;
	private final Dialog dialog;

	public DialogObjective(Dialog dialog) {
		this("Talk to", dialog);
	}

	public DialogObjective(String verb, Dialog dialog) {
		this.verb = verb;
		this.dialog = dialog;
	}

	public String getVerb() {
		return verb;
	}

	public Dialog getDialog() {
		return dialog;
	}

	public Person getPerson() {
		return getDialog().getPerson();
	}

	@Override
	public String getName() {
		return getVerb() + " " + getPerson().getShortName() + (getSpaceObject() != null ? " at " + getSpaceObject().getName() : "");
	}

	@Override
	public SpaceObject getSpaceObject() {
		return getPerson().getHomeObject();
	}

	@Override
	public void onMenu(Menu menu) {
		if(menu.getHandle() instanceof DialogMenuHandle) {
			Dialog dialog = ((DialogMenuHandle)menu.getHandle()).getDialog();
			if(dialog.getPerson() == getDialog().getPerson()) {
				menu.setAuto(new DialogOption(getMission().getName(), dialog));
			}
		}
	}
}
