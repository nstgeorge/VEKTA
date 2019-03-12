package vekta.terrain.settlement;

import vekta.menu.Menu;
import vekta.menu.handle.DistrictMenuHandle;
import vekta.menu.option.BackOption;
import vekta.menu.option.BasicOption;
import vekta.terrain.LandingSite;

import java.util.ArrayList;
import java.util.List;

import static vekta.Vekta.setContext;

public class District implements SettlementPart {
	private final String name;

	private final List<SettlementPart> parts = new ArrayList<>();

	public District(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getTypeString() {
		return getName();
	}

	public List<SettlementPart> getParts() {
		return parts;
	}

	public void add(SettlementPart part) {
		parts.add(part);
	}

	@Override
	public void setup(LandingSite site) {
		for(SettlementPart part : getParts()) {
			part.setup(site);
		}
	}

	@Override
	public void setupSettlementMenu(Menu menu) {
		menu.add(new BasicOption(getName(), m -> {
			Menu sub = new Menu(m.getPlayer(), new DistrictMenuHandle(new BackOption(m), this));
			for(SettlementPart part : getParts()) {
				part.setupSettlementMenu(sub);
			}
			sub.addDefault();
			setContext(sub);
		}));
	}
}
