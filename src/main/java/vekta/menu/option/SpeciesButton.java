package vekta.menu.option;

import vekta.util.InfoGroup;
import vekta.display.Layout;
import vekta.ecosystem.Ecosystem;
import vekta.ecosystem.Species;
import vekta.item.ItemType;
import vekta.menu.Menu;
import vekta.menu.handle.MenuHandle;

import static vekta.Vekta.quantityString;
import static vekta.Vekta.v;

public class SpeciesButton extends ButtonOption implements LayoutAware {
	private final Species species;
	private final Ecosystem ecosystem;

	public SpeciesButton(Species species, Ecosystem ecosystem) {
		this.species = species;
		this.ecosystem = ecosystem;
	}

	public int getPopulation() {
		return ecosystem.count(species);
	}

	@Override
	public String getName() {
		return species.getFullName();
	}

	@Override
	public int getColor() {
		return ItemType.ECOSYSTEM.getColor();
	}

	@Override
	public boolean isEnabled() {
		return getPopulation() > 0;
	}

	@Override
	public void render(Menu menu, int index) {
		super.render(menu, index);

		if(getPopulation() > 0) {
			MenuHandle handle = menu.getHandle();

			v.fill(200);
			v.text(quantityString(getPopulation()), handle.getItemX() + handle.getItemWidth() / 2F + 50, handle.getItemY(index));
		}
	}

	@Override
	public void onSelect(Menu menu) {

	}

	@Override
	public void onLayout(Layout layout) {
		layout.customize()
				.color(ItemType.ECOSYSTEM.getColor());

		InfoGroup info = new InfoGroup();
		ecosystem.onInfo(info, species);

		info.onLayout(layout);
	}
}
