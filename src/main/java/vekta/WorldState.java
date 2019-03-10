package vekta;

import vekta.object.SpaceObject;
import vekta.person.Person;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Serializable world information (.vekta format)
 * */
public final class WorldState implements Serializable {
	//	private float timeScale = 1;

	private final Player player;

	private final List<SpaceObject> objects = new ArrayList<>();
	private final List<SpaceObject> gravityObjects = new ArrayList<>();

	private final Set<SpaceObject> objectsToAdd = new HashSet<>();
	private final Set<SpaceObject> objectsToRemove = new HashSet<>();

	private final List<Person> people = new ArrayList<>();
	private final List<Faction> factions = new ArrayList<>();

	private boolean updating;

	public WorldState(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}
	//	public float getTimeScale() {
	//		return timeScale;
	//	}

	public List<SpaceObject> getObjects() {
		return objects;
	}

	public List<SpaceObject> getGravityObjects() {
		return gravityObjects;
	}

	public List<Person> getPeople() {
		return people;
	}

	public List<Faction> getFactions() {
		return factions;
	}

	public void startUpdate() {
		updating = true;
		for(SpaceObject s : objectsToAdd) {
			addImmediately(s);
		}
		objectsToAdd.clear();
	}

	public void endUpdate() {
		updating = false;
		objects.removeAll(objectsToRemove);
		gravityObjects.removeAll(objectsToRemove);
		objectsToRemove.clear();
	}

	public boolean isRemoving(SpaceObject s) {
		return objectsToRemove.contains(s);
	}

	public void addImmediately(SpaceObject s) {
		if(!objects.contains(s)) {
			objects.add(s);
		}
		if(s.impartsGravity() && !gravityObjects.contains(s)) {
			gravityObjects.add(s);
		}
	}

	public void addObject(Object object) {
		if(object instanceof SpaceObject) {
			SpaceObject s = (SpaceObject)object;
			if(updating) {
				objectsToAdd.add(s);
			}
			else {
				addImmediately(s);
			}
		}
		else if(object instanceof Person && !people.contains(object)) {
			people.add((Person)object);
		}
		else if(object instanceof Faction && !factions.contains(object)) {
			factions.add((Faction)object);
		}
		else {
			throw new RuntimeException("Cannot add object: " + object);
		}
	}

	public void removeObject(Object object) {
		if(object instanceof SpaceObject) {
			objectsToRemove.add((SpaceObject)object);
		}
		else if(object instanceof Person) {
			people.remove(object);
		}
		else if(object instanceof Faction) {
			factions.remove(object);
		}
		else {
			throw new RuntimeException("Cannot remove object: " + object);
		}
	}
}
