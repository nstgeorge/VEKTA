package vekta.module;

public interface Upgrader {
	Module getRelevantModule(Module module);

	void installModule(Module module);

	void uninstallModule(Module module);
}
