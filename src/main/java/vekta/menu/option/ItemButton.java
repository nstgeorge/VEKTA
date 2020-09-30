package vekta.menu.option;

import vekta.util.InfoGroup;
import vekta.KeyBinding;
import vekta.Settings;
import vekta.display.Layout;
import vekta.display.TextDisplay;
import vekta.item.Item;

public abstract class ItemButton implements ButtonOption, LayoutAware {
	private final Item item;

	public ItemButton(Item item) {
		this.item = item;
	}

	public Item getItem() {
		return item;
	}

	@Override
	public String getName() {
		return item.getName();
	}

	@Override
	public int getColor() {
		return item.getColor();
	}

	@Override
	public String getSelectVerb() {
		return "jettison";
	}

	@Override
	public void onLayout(Layout layout) {
		layout.add(new TextDisplay(item.getName()))
				.customize().fontSize(32);

		InfoGroup info = new InfoGroup();
		info.addStat("Mass", item.getMass());
		item.onInfo(info);
		info.onLayout(layout);

		if(isEnabled()) {
			layout.add(new TextDisplay(Settings.getKeyText(KeyBinding.MENU_SELECT) + " to " + getSelectVerb()))
					.customize().color(100);
		}
	}
}
