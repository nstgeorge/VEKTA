package vekta.module;

import java.io.Serializable;

public interface ModuleUpgrader extends Serializable {
	BaseModule getRelevantModule(BaseModule module);

	void installModule(BaseModule module);

	void uninstallModule(BaseModule module);
}
