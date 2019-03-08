package vekta.menu.handle;

import vekta.menu.Menu;
import vekta.menu.option.MenuOption;
import vekta.person.Dialog;

import static vekta.Vekta.v;

/**
 * Menu renderer for dialog interactions
 */
public class DialogMenuHandle extends MenuHandle {
	private final Dialog dialog;

	public DialogMenuHandle(MenuOption def, Dialog dialog) {
		super(def);

		this.dialog = dialog;
	}

	public Dialog getDialog() {
		return dialog;
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
	public void render(Menu menu) {
		super.render(menu);

		v.textSize(64);
		v.fill(dialog.getPerson().getColor());
		v.text(dialog.getPerson().getShortName(), getButtonX(), getButtonY(-3));

		v.textSize(24);
		v.fill(200);
		v.text(dialog.getMessage(), getButtonX(), getButtonY(-2));
	}
}
