package vekta.menu.option;

import vekta.deity.Deity;
import vekta.item.Inventory;
import vekta.item.Item;
import vekta.menu.Menu;
import vekta.player.Player;
import vekta.spawner.DeityGenerator;

import static processing.core.PApplet.abs;
import static processing.core.PApplet.sqrt;

public class OfferButton extends ItemButton {
	private final Inventory inv;
	private final Deity deity;

	public OfferButton(Item item, Inventory inv, Deity deity) {
		super(item);
		this.inv = inv;
		this.deity = deity;
	}

	public Deity getDeity() {
		return deity;
	}

	@Override
	public String getName() {
		return getItem().getName();
	}

	@Override
	public String getSelectVerb() {
		return "offer";
	}

	@Override
	public void onSelect(Menu menu) {
		inv.remove(getItem());
		DeityGenerator.addFavor(menu.getPlayer(), getDeity(), chooseFavor(menu.getPlayer()));
		menu.close();
	}

	public float chooseFavor(Player player) {
		return sqrt(getItem().randomPrice()) / (1 + sqrt(abs(DeityGenerator.getFavor(player, getDeity()))));
	}
}
