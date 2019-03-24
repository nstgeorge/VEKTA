package vekta.person;

import vekta.ConditionalRegister;
import vekta.Faction;

public class TemporaryPerson extends Person implements ConditionalRegister {
	public TemporaryPerson(String name, Faction faction) {
		super(name, faction);
	}

	@Override
	public boolean shouldRegister() {
		return false;
	}
}
