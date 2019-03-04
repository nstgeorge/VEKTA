package vekta.object.module;

public interface Upgrader {
	Module getRelevantModule(Module module);

	void addModule(Module module);

	void removeModule(Module module);
}
