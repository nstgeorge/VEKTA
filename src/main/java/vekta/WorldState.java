package vekta;

import vekta.object.SpaceObject;
import vekta.person.Person;

import java.io.Serializable;
import java.util.*;

import static processing.core.PApplet.println;

/**
 * Serializable world information (.vekta format)
 */
public final class WorldState implements Serializable {
	//	private float timeScale = 1;

	private final Player player;

	private final Map<String, Syncable> syncMap = new HashMap<>();

	private final List<SpaceObject> objects = new ArrayList<>();
	private final List<SpaceObject> gravityObjects = new ArrayList<>();

	private final Set<SpaceObject> objectsToAdd = new HashSet<>();
	private final Set<SpaceObject> objectsToRemove = new HashSet<>();

	private final List<Person> people = new ArrayList<>();
	private final List<Faction> factions = new ArrayList<>();

	private boolean updating;
	private int nextID;

	public WorldState(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}
	//	public float getTimeScale() {
	//		return timeScale;
	//	}

	public Collection<Syncable> getSyncableObjects() {
		return syncMap.values();
	}

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

	@SuppressWarnings("unchecked")
	public <S extends Syncable> S register(S object) {
		// Find already existing object with the same state key
		String key = getKey(object);
		if(syncMap.containsKey(key)) {
			S other = (S)syncMap.get(key);
			other.onSync(object.getSyncData());
			println("<sync>", key);
			return other;
		}
		else {
			add(object);
			syncMap.put(getKey(object), object);
			println("<add>", key);
			return object;
		}
	}

	private void add(Syncable object) {
		if(object instanceof SpaceObject) {
			SpaceObject s = (SpaceObject)object;
			s.setID(nextID++);
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
	}

	public void remove(Syncable object) {
		syncMap.remove(getKey(object));

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

	public String getKey(Syncable<?> object) {
		return object.getClass().getSimpleName() + "[" + object.getSyncKey() + "]";
	}

	@SuppressWarnings("unchecked")
	public <T extends Serializable> Syncable<T> find(String key) {
		Syncable<T> sync = syncMap.get(key);
		if(sync != null) {
			return sync;
		}
		else {
			return null;
		}
	}
}
