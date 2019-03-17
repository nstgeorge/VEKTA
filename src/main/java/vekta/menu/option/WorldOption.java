package vekta.menu.option;

import vekta.context.World;
import vekta.menu.Menu;

import java.io.Serializable;

import static vekta.Vekta.applyContext;
import static vekta.Vekta.setContext;

public class WorldOption implements MenuOption {
	private final String name;
	private final WorldProvider provider;

	public WorldOption(String name, WorldProvider provider) {
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
