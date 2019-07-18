package vekta.menu.option;

import vekta.economy.Coin;
import vekta.economy.CoinMarket;
import vekta.menu.Menu;
import vekta.menu.handle.EconomyMenuHandle;
import vekta.spawner.item.CoinItemSpawner;

import static vekta.Vekta.setContext;

public class CoinMenuButton implements ButtonOption {
	private final CoinMarket market;

	public CoinMenuButton() {
		this(CoinItemSpawner.findCoinMarket());
	}

	public CoinMenuButton(CoinMarket market) {
		this.market = market;
	}

	@Override
	public String getName() {
		return "Coin Market";
	}

	@Override
	public void onSelect(Menu menu) {
		EconomyMenuHandle handle = new EconomyMenuHandle(menu.getPlayer().getInventory(), this::update);
		Menu sub = new Menu(menu, handle);
		update(sub, handle.isBuying());
		setContext(sub);
	}

	private void update(Menu sub, boolean buying) {
		sub.clear();
		for(Coin coin : market.getCoins()) {
			float valueChange = .1F; // Amount to raise the market value
			sub.add(new EconomyItemButton(
					sub.getPlayer().getInventory(),
					CoinItemSpawner.createCoinItem(coin, market),
					valueChange,
					buying,
					this::update));
		}
		sub.addDefault();
	}
}
