package vekta.module;

import java.util.List;

public interface Upgradeable {
	List<Module> getModules();

	Module getModule(ModuleType type);

	List<Module> findUpgrades();

	void addModule(Module module);

	void removeModule(Module module);
}
