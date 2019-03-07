package vekta.menu.handle;

import vekta.menu.Menu;
import vekta.menu.dialog.Dialog;
import vekta.menu.option.MenuOption;

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
	public void render(Menu menu) {
		super.render(menu);

		v.textSize(32);
		v.fill(dialog.getPerson().getColor());
		v.text(dialog.getPerson().getDisplayName(), getButtonX(), getButtonY(-3));
		
		v.textSize(24);
		v.fill(200);
		v.text(dialog.getMessage(), getButtonX(), getButtonY(-1));
	}
}
