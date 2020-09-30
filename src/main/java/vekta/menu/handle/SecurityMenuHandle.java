package vekta.menu.handle;

import vekta.faction.Faction;
import vekta.context.Context;
import vekta.menu.Menu;

import static vekta.Vekta.v;

/**
 * Menu renderer for security checkpoints
 */
public class SecurityMenuHandle extends MenuHandle {
	private final Faction faction;
	private final Context next;

	public SecurityMenuHandle(Context next, Faction faction) {
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
	public int getItemWidth() {
		return v.width * 2 / 3;
	}

	@Override
	public int getItemY(int i) {
		return super.getItemY(i + 1);
	}

	@Override
	public void render(Menu menu) {
		super.render(menu);

		v.textSize(64);
		v.fill(getFaction().getColor());
		v.text(getFaction().getName(), getItemX(), getItemY(-3));

		v.textSize(24);
		v.fill(200);
		v.text("You notice a heavily guarded security checkpoint ahead.", getItemX(), getItemY(-2));
	}
}
