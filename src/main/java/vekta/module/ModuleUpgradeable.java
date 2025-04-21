package vekta.module;

import java.io.Serializable;
import java.util.List;

public interface ModuleUpgradeable extends Serializable {
	List<BaseModule> getModules();

	BaseModule getModule(ModuleType type);

	List<BaseModule> findUpgrades();

	void addModule(BaseModule module);

	void removeModule(BaseModule module);
}
