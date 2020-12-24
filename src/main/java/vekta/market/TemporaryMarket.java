package vekta.market;

import vekta.item.Inventory;
import vekta.sync.ConditionalRegister;

public class TemporaryMarket extends Market implements ConditionalRegister {

	public TemporaryMarket(String name, Inventory inventory) {
		super(name, inventory);
	}

	@Override
	public boolean shouldRegister() {
		return false;
	}
}
