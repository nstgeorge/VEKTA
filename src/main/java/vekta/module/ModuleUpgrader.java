package vekta.module;

import java.io.Serializable;

public interface ModuleUpgrader extends Serializable {
	Module getRelevantModule(Module module);

	void installModule(Module module);

	void uninstallModule(Module module);
}
