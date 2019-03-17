package vekta.menu.handle;

import vekta.Faction;
import vekta.context.Context;
import vekta.menu.Menu;
import vekta.menu.option.MenuOption;

import static vekta.Vekta.v;

/**
 * Menu renderer for dialog interactions
 */
public class SecurityMenuHandle extends MenuHandle {
	private final Faction faction;
	private final Context next;

	public SecurityMenuHandle(MenuOption back, Context next, Faction faction) {
		super(back);

		this.faction = faction;
		this.next = next;
	}

	public Faction getFaction() {
		return faction;
	}

	public Context getNext() {
		return next;
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
		v.fill(getFaction().getColor());
		v.text(getFaction().getName(), getButtonX(), getButtonY(-3));

		v.textSize(24);
		v.fill(200);
		v.text("You notice a heavily guarded security checkpoint ahead.", getButtonX(), getButtonY(-2));
	}
}
