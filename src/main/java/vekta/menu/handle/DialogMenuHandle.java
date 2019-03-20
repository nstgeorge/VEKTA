package vekta.menu.handle;

import vekta.menu.Menu;
import vekta.knowledge.ObservationLevel;
import vekta.knowledge.PersonKnowledge;
import vekta.person.Dialog;
import vekta.person.Person;

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
	public int getButtonWidth() {
		return v.width * 2 / 3;
	}

	@Override
	public int getButtonY(int i) {
		return super.getButtonY(i + 1);
	}

	@Override
	public void focus(Menu menu) {
		super.focus(menu);

		menu.getPlayer().addKnowledge(new PersonKnowledge(ObservationLevel.VISITED, getPerson()));
	}

	@Override
	public void render(Menu menu) {
		super.render(menu);

		v.textSize(64);
		v.fill(dialog.getPerson().getColor());
		v.text(dialog.getPerson().getName(), getButtonX(), getButtonY(-3));

		v.textSize(24);
		v.fill(dialog.getTextColor());
		v.text(dialog.getMessage(), getButtonX(), getButtonY(-2));
	}
}
