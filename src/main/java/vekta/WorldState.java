package vekta;

import processing.core.PVector;
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

	private Player player;

	private final Map<Long, Syncable> syncMap = new HashMap<>(); // All client-syncable objects

	private final List<SpaceObject> objects = new ArrayList<>();
	private final List<SpaceObject> gravityObjects = new ArrayList<>();

	private final Set<SpaceObject> objectsToAdd = new HashSet<>();
	private final Set<SpaceObject> objectsToRemove = new HashSet<>();

	private final List<Person> people = new ArrayList<>();
	private final List<Faction> factions = new ArrayList<>();

	private final GlobalVector globalPosition = new GlobalVector();
	private final PVector globalVelocity = new PVector();

	private final Set<Syncable> registerScope = new HashSet<>();

	private boolean updating;

	public WorldState() {
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = register(player);//
	}

	//	public float getTimeScale() {
	//		return timeScale;
	//	}

	public Collection<Syncable> getSyncables() {
		return syncMap.values();
	}

	public Syncable getSyncable(long key) {
		return syncMap.get(key);
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

	public void updateGlobalCoords(float timeScale) {
		globalPosition.add(globalVelocity.x * timeScale, globalVelocity.y * timeScale);
	}

	public double getGlobalX(float x) {
		return globalPosition.x + x;
	}

	public double getGlobalY(float y) {
		return globalPosition.y + y;
	}

	public PVector getLocalPosition(double x, double y) {
		return new PVector((float)(x - globalPosition.x), (float)(y - globalPosition.y));
	}

	public PVector getGlobalVelocity(PVector velocity) {
		return velocity.copy().add(globalVelocity);
	}

	public PVector getLocalVelocity(PVector velocity) {
		return velocity.copy().sub(globalVelocity);
	}

	public void addRelativePosition(PVector offset) {
		PVector relative = offset.mult(-1);
		for(SpaceObject s : getObjects()) {
			s.updateOrigin(relative);
		}
		globalPosition.add(offset.x, offset.y);
	}

	public void addRelativeVelocity(PVector velocity) {
		PVector relative = velocity.mult(-1);
		for(SpaceObject s : getObjects()) {
			s.addVelocity(relative);
		}
		globalVelocity.add(velocity);
	}

	public void resetRelativeVelocity() {
		for(SpaceObject s : getObjects()) {
			s.addVelocity(globalVelocity);
		}
		globalVelocity.set(0, 0);
	}

	public boolean isRemoving(SpaceObject s) {
		return objectsToRemove.contains(s);
	}

	public <S extends Syncable> S register(S object) {
		return register(object, false);
	}

	@SuppressWarnings("unchecked")
	public <S extends Syncable> S register(S object, boolean remote) {
		if(registerScope.contains(object)) {
			return object; // Prevents infinite recursion
		}

		registerScope.add(object);
		try {
			// Find already existing object with the same state key
			long id = object.getSyncID();
			if(syncMap.containsKey(id)) {
				S other = (S)syncMap.get(id);
				other.onSync(object.getSyncData());
				println("<sync>", object.getClass().getSimpleName() + "[" + Long.toHexString(id) + "]");
				return other;
			}
			else {
				add(object, remote);
				syncMap.put(object.getSyncID(), object);
				println("<add>", object.getClass().getSimpleName() + "[" + Long.toHexString(id) + "]");
				return object;
			}
		}
		finally {
			registerScope.remove(object);
		}
	}

	private void add(Syncable object, boolean remote) {
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

		if(remote) {
			object.onAddRemote();
		}
	}

	private void addImmediately(SpaceObject s) {
		if(!objects.contains(s)) {
			objects.add(s);
		}
		if(s.impartsGravity() && !gravityObjects.contains(s)) {
			gravityObjects.add(s);
		}
	}

	public void remove(Syncable object) {
		syncMap.remove(object.getSyncID());

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

	@SuppressWarnings("unchecked")
	public <T extends Syncable> Syncable<T> find(long id) {
		Syncable<T> sync = syncMap.get(id);
		if(sync != null) {
			return sync;
		}
		else {
			return null;
		}
	}
}
