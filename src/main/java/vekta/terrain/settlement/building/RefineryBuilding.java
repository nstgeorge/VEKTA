package vekta.terrain.settlement.building;

import vekta.item.Item;
import vekta.item.OreItem;
import vekta.menu.Menu;
import vekta.menu.handle.MenuHandle;
import vekta.menu.option.CustomButton;
import vekta.menu.option.OreRefineButton;
import vekta.terrain.settlement.SettlementPart;

import java.util.Set;

import static vekta.Vekta.setContext;

public class RefineryBuilding implements SettlementPart {
	public RefineryBuilding() {
	}

	@Override
	public String getName() {
		return getGenericName();
	}

	@Override
	public String getGenericName() {
		return "Refinery";
	}

	@Override
	public BuildingType getType() {
		return BuildingType.INDUSTRIAL;
	}

	@Override
	public void onSurveyTags(Set<String> tags) {
		tags.add("Industrial");
		tags.add("Smog");
	}

	@Override
	public void setupMenu(Menu menu) {
		menu.add(new CustomButton("Refinery", m -> {
			Menu sub = new Menu(m, new MenuHandle());
			for(Item item : m.getPlayer().getInventory()) {
				if(item instanceof OreItem && ((OreItem)item).getRefined() != null) {
					sub.add(new OreRefineButton((OreItem)item, m.getPlayer().getInventory()));
				}
			}
			sub.addDefault();
			setContext(sub);
		}).withRemoval());
	}
}
