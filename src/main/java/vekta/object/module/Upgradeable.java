package vekta.object.module;

import java.util.List;

public interface Upgradeable {
	List<Module> getModules();
	
	List<Module> findUpgrades();
	
	void upgrade(Module module);
}
