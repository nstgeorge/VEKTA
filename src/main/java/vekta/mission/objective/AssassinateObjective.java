package vekta.mission.objective;

import vekta.menu.Menu;
import vekta.menu.handle.DialogMenuHandle;
import vekta.object.SpaceObject;
import vekta.person.Dialog;
import vekta.person.Person;

public class AssassinateObjective extends Objective {
	private static final String HELP_DIALOG_TYPE = "offer_weapon";

	private final Person person;

	private boolean askedForWeapon;

	public AssassinateObjective(Person person) {
		this.person = person;
	}

	public Person getPerson() {
		return person;
	}

	@Override
	public String getName() {
		return "Assassinate " + getPerson().getFullName() + " (" + getSpaceObject().getName() + ")";
	}

	@Override
	public SpaceObject getSpaceObject() {
		return getPerson().findHomeObject();
	}

	@Override
	public void onMenu(Menu menu) {
		if(!checkDead() && menu.getHandle() instanceof DialogMenuHandle) {
			Dialog dialog = ((DialogMenuHandle)menu.getHandle()).getDialog();
			Person speaker = dialog.getPerson();
			if(HELP_DIALOG_TYPE.equals(dialog.getType())) {
				askedForWeapon = true;
			}
			else if(speaker == getPerson()) {
				dialog.then("nervous_assassin");
			}
			else if(!askedForWeapon && speaker.getOpinion(menu.getPlayer().getFaction()).isPositive()) {
				dialog.add("Hypothetically speaking, how can I assassinate " + getPerson().getName() + "?",
						person.createDialog(HELP_DIALOG_TYPE));
			}
		}
	}

	@Override
	public void onDestroyObject(SpaceObject object) {
		checkDead();
	}

	private boolean checkDead() {
		if(getPerson().isDead()) {
			complete();
			return true;
		}
		return false;
	}
}
