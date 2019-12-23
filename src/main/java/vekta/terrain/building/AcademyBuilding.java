package vekta.terrain.building;

import vekta.item.Inventory;
import vekta.menu.Menu;
import vekta.menu.handle.MenuHandle;
import vekta.menu.option.CustomButton;
import vekta.menu.option.SellDataMenuButton;
import vekta.terrain.LandingSite;
import vekta.terrain.settlement.Settlement;
import vekta.terrain.settlement.SettlementPart;

import static vekta.Vekta.setContext;
import static vekta.Vekta.v;

public class AcademyBuilding implements SettlementPart {
	private final Settlement settlement;

	private final Inventory inventory = new Inventory();

	public AcademyBuilding(Settlement settlement) {
		this.settlement = settlement;

		getInventory().add((int)v.random(10, 100));
	}

	public Settlement getSettlement() {
		return settlement;
	}

	public Inventory getInventory() {
		return inventory;
	}

	@Override
	public String getName() {
		return getSettlement().getName() + " " + getGenericName();
	}

	@Override
	public String getGenericName() {
		return "Academy";
	}

	@Override
	public BuildingType getType() {
		return BuildingType.KNOWLEDGE;
	}

	@Override
	public void setup(LandingSite site) {
	}

	@Override
	public void cleanup() {
	}

	@Override
	public void setupMenu(Menu menu) {
		menu.add(new CustomButton(getGenericName(), m -> {
			Menu sub = new Menu(m, new MenuHandle());
			sub.add(new SellDataMenuButton(getSettlement(), getInventory()));
			sub.addDefault();
			setContext(sub);
		}));
	}
}
