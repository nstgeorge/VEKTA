package vekta.terrain;

import vekta.menu.Menu;
import vekta.menu.handle.SettlementMenuHandle;
import vekta.menu.option.BackOption;
import vekta.menu.option.BasicOption;
import vekta.terrain.settlement.Settlement;

import static vekta.Vekta.setContext;

public class HabitableTerrain extends Terrain {
	private Settlement settlement;

	public HabitableTerrain(Settlement settlement){
		this();
		
		setSettlement(settlement);
	}
	
	public HabitableTerrain() {
		addFeature("Atmosphere");
		addFeature("Habitable");
	}
	
	public Settlement getSettlement() {
		return settlement;
	}

	public void setSettlement(Settlement settlement) {
		if(settlement == null) {
			throw new RuntimeException("Settlement cannot be null");
		}
		this.settlement = settlement;
		settlement.setupTerrain(this);
	}

	@Override
	public String getOverview() {
		return getSettlement().getOverview();
	}

	@Override
	public void setupLandingMenu(Menu menu) {
		menu.add(new BasicOption("Visit " + settlement.getTypeString(), m -> {
			Menu sub = new Menu(m.getPlayer(), new SettlementMenuHandle(new BackOption(m), getSettlement()));
			getSettlement().setupSettlementMenu(sub);
			sub.addDefault();
			setContext(sub);
		}));
	}
}
