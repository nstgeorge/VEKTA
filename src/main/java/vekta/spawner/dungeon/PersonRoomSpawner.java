package vekta.spawner.dungeon;

import vekta.dungeon.DungeonRoom;
import vekta.dungeon.PersonRoom;
import vekta.person.Person;
import vekta.person.TemporaryPerson;
import vekta.spawner.DungeonGenerator;
import vekta.spawner.FactionGenerator;
import vekta.spawner.PersonGenerator;

public class PersonRoomSpawner implements DungeonGenerator.DungeonSpawner {
	@Override
	public float getWeight() {
		return .5F;
	}

	@Override
	public boolean isValid(DungeonRoom parent, int depth) {
		return depth >= 2;
	}

	@Override
	public DungeonRoom create(DungeonRoom parent, int depth) {
		Person person = new TemporaryPerson(PersonGenerator.randomPersonName(), FactionGenerator.randomFaction());
		person.setTitle("of " + parent.getDungeon().getName());
		return new PersonRoom(parent, person);
	}
}
