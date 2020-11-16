package vekta.menu.option;

import vekta.ecosystem.Ecosystem;
import vekta.menu.Menu;
import vekta.menu.handle.EcosystemMenuHandle;

import java.util.Comparator;
import java.util.stream.Stream;

import static vekta.Vekta.setContext;

public class EcosystemButton extends ButtonOption {
	private final Ecosystem ecosystem;

	public EcosystemButton(Ecosystem ecosystem) {
		this.ecosystem = ecosystem;
	}

	@Override
	public String getName() {
		return "Ecosystem";
	}

	@Override
	public void onSelect(Menu menu) {
		EcosystemMenuHandle handle = new EcosystemMenuHandle(ecosystem);
		Menu sub = new Menu(menu, handle);

		Stream.concat(ecosystem.getSpecies().stream().sorted(Comparator.comparingInt(ecosystem::count).reversed()), ecosystem.getExtinctions().stream())
				.forEach(species -> sub.add(new SpeciesButton(species, ecosystem)));

		sub.addDefault();
		setContext(sub);
	}
}
