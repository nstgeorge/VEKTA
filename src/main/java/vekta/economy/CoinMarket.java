package vekta.economy;

import vekta.item.ItemType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static vekta.Vekta.register;

public class CoinMarket implements Serializable, Economy.Container, ProductivityModifier {
	private final Economy economy;
	private final List<Coin> coins = new ArrayList<>();

	public CoinMarket() {
		this.economy = register(new Economy(this, 10));
		economy.addModifier(new NoiseModifier(2));
		economy.addModifier(this);
		economy.fillHistory();
	}

	public Economy getEconomy() {
		return economy;
	}

	public List<Coin> getCoins() {
		return coins;
	}

	@Override
	public String getName() {
		return "Coin Market";
	}

	@Override
	public int getColor() {
		return ItemType.ECONOMY.getColor();
	}

	@Override
	public boolean isEconomyAlive() {
		return true;
	}

	@Override
	public String getModifierName() {
		return getName();
	}

	@Override
	public float updateModifier(Economy economy) {
		return 0;
	}
}
