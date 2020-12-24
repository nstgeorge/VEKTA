package vekta.person;

import vekta.faction.Faction;
import vekta.sync.ConditionalRegister;

public class TemporaryPerson extends Person implements ConditionalRegister {
	public TemporaryPerson(String name, Faction faction) {
		super(name, faction);
	}

	@Override
	public boolean shouldRegister() {
		return false;
	}
}
