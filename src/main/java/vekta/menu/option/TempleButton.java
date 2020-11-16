package vekta.menu.option;

import vekta.deity.Deity;
import vekta.menu.Menu;
import vekta.menu.handle.TempleMenuHandle;

import static vekta.Vekta.setContext;

public class TempleButton extends ButtonOption {
	private final String name;
	private final Deity deity;

	public TempleButton(String name, Deity deity) {
		this.name = name;
		this.deity = deity;
	}

	@Override
	public String getName() {
		return name;
	}

	public Deity getDeity() {
		return deity;
	}

	@Override
	public void onSelect(Menu menu) {
		Menu sub = new Menu(menu, new TempleMenuHandle());

		sub.add(new OfferMenuButton(getDeity()));
		sub.addDefault();

		setContext(sub);
	}
}
