package vekta.menu.option;

import vekta.menu.Menu;
import vekta.world.World;

import java.io.Serializable;

import static vekta.Vekta.applyContext;
import static vekta.Vekta.setContext;

public class WorldButton extends ButtonOption {
	private final String name;
	private final WorldProvider provider;

	public WorldButton(String name, WorldProvider provider) {
		this.name = name;
		this.provider = provider;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void onSelect(Menu menu) {
		setContext(provider.provide());
		applyContext();
	}

	public interface WorldProvider extends Serializable {
		World provide();
	}
}
