package vekta.module;

import vekta.InfoGroup;
import vekta.knowledge.OceanKnowledge;
import vekta.menu.Menu;
import vekta.menu.handle.LandingMenuHandle;
import vekta.menu.option.LootMenuButton;
import vekta.menu.option.ScanOceanButton;
import vekta.terrain.LandingSite;

import java.util.List;

public class OceanScannerModule extends ShipModule {
	private float strength;

	public OceanScannerModule() {
		this(1);
	}

	public OceanScannerModule(float strength) {
		this.strength = strength;
	}

	public float getStrength() {
		return strength;
	}

	@Override
	public String getName() {
		return "Oceanic Scanner v" + getStrength();
	}

	@Override
	public ModuleType getType() {
		return ModuleType.SCANNER;
	}

	@Override
	public int getMass() {
		return 1000;
	}

	@Override
	public boolean isBetter(Module other) {
		return other instanceof OceanScannerModule && getStrength() > ((OceanScannerModule)other).getStrength();
	}

	@Override
	public Module createVariant() {
		return new OceanScannerModule(chooseInclusive(.1F, 2, .1F));
	}

	@Override
	public void onMenu(Menu menu) {
		if(menu.getHandle() instanceof LandingMenuHandle) {
			LandingSite site = ((LandingMenuHandle)menu.getHandle()).getSite();

			if(site.getTerrain().hasFeature("Oceanic")) {
				List<OceanKnowledge> knowledgeList = menu.getPlayer().findKnowledge(OceanKnowledge.class, o -> o.getSpaceObject() == site.getParent());
				if(knowledgeList.isEmpty()) {
					menu.add(new ScanOceanButton(site, getStrength()));
				}
				else {
					for(OceanKnowledge knowledge : knowledgeList) {
						if(knowledge.getInventory().itemCount() > 0) {
							menu.add(new LootMenuButton("Salvage", menu.getPlayer().getInventory(), knowledge.getInventory()));
						}
					}
				}
			}
		}
	}

	@Override
	public void onInfo(InfoGroup info) {
		info.addDescription("Detect underwater features on oceanic planets.");
	}
}
