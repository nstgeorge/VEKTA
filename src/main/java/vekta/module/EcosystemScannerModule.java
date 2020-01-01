package vekta.module;

import vekta.util.InfoGroup;
import vekta.ecosystem.Ecosystem;
import vekta.menu.Menu;
import vekta.menu.handle.LandingMenuHandle;
import vekta.menu.option.EcosystemButton;
import vekta.terrain.LandingSite;

public class EcosystemScannerModule extends ShipModule {
	private float strength;

	public EcosystemScannerModule() {
		this(1);
	}

	public EcosystemScannerModule(float strength) {
		this.strength = strength;
	}

	public float getStrength() {
		return strength;
	}

	@Override
	public String getName() {
		return "Ecological Scanner v" + getStrength();
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
	public float getValueScale() {
		return 1.25F * getStrength();
	}

	@Override
	public boolean isBetter(Module other) {
		return other instanceof EcosystemScannerModule && getStrength() > ((EcosystemScannerModule)other).getStrength();
	}

	//	@Override
	//	public Module createVariant() {
	//		return new EcosystemScannerModule(chooseInclusive(.1F, 2, .1F));
	//	}
	
	@Override
	public Module createVariant() {
		return new EcosystemScannerModule();
	}

	@Override
	public void onMenu(Menu menu) {
		if(menu.getHandle() instanceof LandingMenuHandle) {
			LandingSite site = ((LandingMenuHandle)menu.getHandle()).getSite();

			Ecosystem ecosystem = site.getTerrain().getEcosystem();

			if(!ecosystem.getSpecies().isEmpty() || !ecosystem.getExtinctions().isEmpty()) {
				menu.add(new EcosystemButton(ecosystem));
			}
		}
	}

	@Override
	public void onInfo(InfoGroup info) {
		info.addDescription("Passively survey the ecosystem of terrestrial planets.");
	}
}
