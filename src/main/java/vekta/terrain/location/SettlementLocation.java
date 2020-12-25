package vekta.terrain.location;

import vekta.Resources;
import vekta.knowledge.ObservationLevel;
import vekta.menu.Menu;
import vekta.menu.handle.LocationMenuHandle;
import vekta.menu.handle.MenuHandle;
import vekta.menu.option.BackButton;
import vekta.menu.option.MenuOption;
import vekta.menu.option.PathwayButton;
import vekta.menu.option.SettlementButton;
import vekta.object.planet.TerrestrialPlanet;
import vekta.player.Player;
import vekta.player.PlayerEvent;
import vekta.sound.Tune;
import vekta.spawner.WorldGenerator;
import vekta.terrain.settlement.Settlement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static vekta.Vekta.getContext;
import static vekta.Vekta.setContext;

public class SettlementLocation extends Location {

	private Settlement settlement;

	public SettlementLocation(TerrestrialPlanet planet) {
		super(planet);
	}

	public void notifySettlement(Settlement settlement) {
		if(this.settlement != null) {
			throw new RuntimeException(getClass().getSimpleName() + " already has a settlement: " + this.settlement);
		}
		this.settlement = settlement;

		getPlanet().notifySettlement(settlement);
	}

	public Settlement getSettlement() {
		return settlement;
	}

	@Override
	public String getName() {
		return getSettlement().getName();
	}

	@Override
	public String getOverview() {
		return getSettlement().getOverview();
	}

	@Override
	protected void onSetupMenu(Menu menu) {
		menu.add(new SettlementButton(getSettlement()));
	}
}
