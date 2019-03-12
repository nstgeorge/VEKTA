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
	private Player player;

	private final Map<Long, Syncable> syncMap = new HashMap<>(); // All client-syncable objects

	private final List<SpaceObject> objects = new ArrayList<>();
	private final List<SpaceObject> gravityObjects = new ArrayList<>();

	private final Set<SpaceObject> objectsToAdd = new HashSet<>();
	private final Set<SpaceObject> objectsToRemove = new HashSet<>();

	private final List<Person> people = new ArrayList<>();
	private final List<Faction> factions = new ArrayList<>();

	//	private final GlobalVector globalPosition = new GlobalVector();
	//	private final PVector globalVelocity = new PVector();

	private final GlobalOffset globalOffset = new GlobalOffset();

	private final Set<Syncable> registerScope = new HashSet<>();
	private boolean remoteFlag;

	private boolean updating;

	public WorldState() {
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = register(player);///
	}

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

	public GlobalOffset getGlobalOffset() {
		return globalOffset;
	}

	public double getGlobalX(float x) {
		return x - globalOffset.px;
	}

	public double getGlobalY(float y) {
		return y - globalOffset.py;
	}

	public PVector getLocalPosition(double x, double y) {
		return new PVector((float)(x + globalOffset.px), (float)(y + globalOffset.py));
	}

	public PVector getGlobalVelocity(PVector velocity) {
		return velocity.copy().sub(globalOffset.vx, globalOffset.vy);
	}

	public PVector getLocalVelocity(PVector velocity) {
		return velocity.copy().add(globalOffset.vx, globalOffset.vy);
	}

	public void updateGlobalCoords(float timeScale) {
		globalOffset.update(timeScale);
	}

	public void addRelativePosition(PVector offset) {
		PVector relative = offset.mult(-1);
		for(SpaceObject s : getObjects()) {
			s.updateOrigin(relative);
		}
		globalOffset.addPosition(offset.x, offset.y);
	}

	public void addRelativeVelocity(PVector velocity) {
		PVector relative = velocity.mult(-1);
		for(SpaceObject s : getObjects()) {
			s.addVelocity(relative);
		}
		globalOffset.addVelocity(velocity.x, velocity.y);
	}

	public void resetRelativeVelocity() {
		PVector velocity = new PVector(globalOffset.vx, globalOffset.vy);
		for(SpaceObject s : getObjects()) {
			s.addVelocity(velocity);
		}
		globalOffset.setVelocity(0, 0);
	}

	public boolean isRemoving(SpaceObject s) {
		return objectsToRemove.contains(s);
	}

	public <S extends Syncable> S register(S object) {
		return register(object, false);
	}

	@SuppressWarnings("unchecked")
	public <S extends Syncable> S register(S object, boolean remote) {
		// Check if an object's sync logic has a circular reference
		if(registerScope.contains(object)) {
			return object;
		}
		registerScope.add(object);

		// If the objects are being registered remotely, set a flag to resolve recursively
		boolean flagging = remote && !remoteFlag;
		if(flagging) {
			remoteFlag = true;
		}
		try {
			// Find already existing object with the same state key
			long id = object.getSyncID();
			if(syncMap.containsKey(id)) {
				S other = (S)syncMap.get(id);
				other.onSync(object.getSyncData());
				println("<sync>", object.getClass().getSimpleName() + "[" + Long.toHexString(id) + "]", remoteFlag);
				return other;
			}
			else {
				add(object);
				syncMap.put(object.getSyncID(), object);
				println("<add>", object.getClass().getSimpleName() + "[" + Long.toHexString(id) + "]", remoteFlag);
				return object;
			}
		}
		finally {
			registerScope.remove(object);
			if(flagging) {
				remoteFlag = false;
			}
		}
	}

	private void add(Syncable object) {
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
		else if(object instanceof Faction && !factions.contains(object) && ((Faction)object).getType() != FactionType.PLAYER) {
			factions.add((Faction)object);
		}

		if(remoteFlag) {
			object.setRemote(true);
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
//		else {
//			throw new RuntimeException("Cannot remove object: " + object);
//		}
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
