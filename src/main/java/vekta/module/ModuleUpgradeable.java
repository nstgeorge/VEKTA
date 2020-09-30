package vekta.module;

import java.io.Serializable;
import java.util.List;

public interface ModuleUpgradeable extends Serializable {
	List<Module> getModules();

	Module getModule(ModuleType type);

	List<Module> findUpgrades();

	void addModule(Module module);

	void removeModule(Module module);
}
