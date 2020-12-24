package vekta.menu.handle;

import vekta.economy.Estate;

import static vekta.Vekta.v;

/**
 * Menu renderer for visiting estates
 */
public class EstateMenuHandle extends MenuHandle {
	private final Estate estate;

	public EstateMenuHandle(Estate estate) {
		this.estate = estate;
	}

	public Estate getEstate() {
		return estate;
	}

	@Override
	public void render() {
		super.render();

		v.textSize(48);
		v.fill(getMenu().getPlayer().getColor());
		v.text(estate.getName(), v.width / 2F, getItemY(-2) - 64);

		v.textSize(32);
		v.fill(100);
		v.text("Estate Area: " + (float)Math.round(estate.getSize() * 10) / 10, v.width / 2F, getItemY(-2));
	}
}
