package vekta.terrain.settlement;

import vekta.menu.Menu;
import vekta.menu.handle.DistrictMenuHandle;
import vekta.menu.option.CustomOption;
import vekta.terrain.LandingSite;
import vekta.terrain.building.BuildingType;

import java.util.ArrayList;
import java.util.List;

import static vekta.Vekta.setContext;

public class District implements SettlementPart {
	private final Settlement settlement;
	private final String name;
	private final BuildingType type;

	private final List<SettlementPart> parts = new ArrayList<>();

	public District(Settlement settlement, String name, BuildingType type) {
		this.settlement = settlement;
		this.name = name;
		this.type = type;
	}

	public Settlement getSettlement() {
		return settlement;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getGenericName() {
		return "District";
	}

	@Override
	public BuildingType getType() {
		return type;
	}

	public List<SettlementPart> getParts() {
		return parts;
	}

	public void add(SettlementPart part) {
		if(!parts.contains(part)) {
			parts.add(part);
		}
	}

	@Override
	public boolean addIfRelevant(SettlementPart part) {
		if(part.getType() == getType()) {
			add(part);
			return true;
		}
		return false;
	}

	@Override
	public void setup(LandingSite site) {
		for(SettlementPart part : getParts()) {
			part.setup(site);
		}
	}

	@Override
	public void setupMenu(Menu menu) {
		if(!getParts().isEmpty()) {
			menu.add(new CustomOption(getName(), m -> {
				Menu sub = new Menu(m, new DistrictMenuHandle(this));
				for(SettlementPart part : getParts()) {
					part.setupMenu(sub);
				}
				sub.addDefault();
				setContext(sub);
			}));
		}
	}
}
