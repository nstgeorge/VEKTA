package vekta.menu.option;

import vekta.world.World;
import vekta.menu.Menu;

import java.io.Serializable;

import static vekta.Vekta.applyContext;
import static vekta.Vekta.setContext;

public class WorldButton implements ButtonOption {
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
