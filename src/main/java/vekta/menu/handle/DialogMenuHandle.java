package vekta.menu.handle;

import vekta.knowledge.FactionKnowledge;
import vekta.knowledge.ObservationLevel;
import vekta.knowledge.PersonKnowledge;
import vekta.menu.Menu;
import vekta.person.Dialog;
import vekta.person.Person;
import vekta.person.TemporaryPerson;

import static vekta.Vekta.v;

/**
 * Menu renderer for dialog interactions
 */
public class DialogMenuHandle extends MenuHandle {
	private final Dialog dialog;

	public DialogMenuHandle(Dialog dialog) {
		this.dialog = dialog;
	}

	public Dialog getDialog() {
		return dialog;
	}

	public Person getPerson() {
		return getDialog().getPerson();
	}

	@Override
	public int getItemWidth() {
		return v.width * 2 / 3;
	}

//	@Override
//	public int getItemY(int i) {
//		return super.getItemY(i + 1);
//	}

	@Override
	public void focus(Menu menu) {
		super.focus(getMenu());

		if(!(getPerson() instanceof TemporaryPerson)) {
			menu.getPlayer().addKnowledge(new PersonKnowledge(ObservationLevel.VISITED, getPerson()));
			menu.getPlayer().addKnowledge(new FactionKnowledge(getPerson().getFaction()));
		}
	}

	@Override
	public void render() {
		super.render();

		v.textSize(64);
		v.fill(dialog.getPerson().getColor());
		v.text(dialog.getPerson().getName(), getItemX(), getY() - (getY() / 2));

		v.textSize(24);
		v.fill(dialog.getColor());
		v.text(dialog.getMessage(), getItemX(), getY() - (getY() / 2) + 100);
	}
}
