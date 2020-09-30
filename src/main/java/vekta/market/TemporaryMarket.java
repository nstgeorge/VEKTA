package vekta.market;

import vekta.item.Inventory;
import vekta.item.Item;
import vekta.menu.Menu;
import vekta.menu.option.MarketButton;
import vekta.sync.ConditionalRegister;
import vekta.sync.Syncable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static vekta.Vekta.getWorld;

public class TemporaryMarket extends Market implements ConditionalRegister {

	public TemporaryMarket(String name, Inventory inventory) {
		super(name, inventory);
	}

	@Override
	public boolean shouldRegister() {
		return false;
	}
}
