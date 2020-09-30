package vekta.world;

import processing.core.PVector;
import vekta.economy.Economy;
import vekta.faction.Faction;
import vekta.faction.PlayerFaction;
import vekta.object.SpaceObject;
import vekta.object.particle.Particle;
import vekta.person.Person;
import vekta.player.Player;
import vekta.sync.ConditionalRegister;
import vekta.sync.Syncable;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.*;

import static processing.core.PApplet.println;
import static vekta.Vekta.v;

/**
 * Serializable world information (.vekta format)
 */
public final class WorldState implements Serializable {
	private Player player;
	private float zoom = 1;

	private float time;

	private final List<ScheduledCallback> callbacks = new ArrayList<>();

	private transient Map<Long, Syncable> syncMap = new HashMap<>(); // All client-syncable objects

	private final List<SpaceObject> objects = new ArrayList<>();
	private final List<SpaceObject> gravityObjects = new ArrayList<>();

	private final Set<SpaceObject> objectsToAdd = new HashSet<>();
	private final Set<SpaceObject> objectsToRemove = new HashSet<>();

	private final List<Person> people = new ArrayList<>();
	private final List<Faction> factions = new ArrayList<>();
	private final List<Economy> economies = new ArrayList<>();

	private final List<ZoomController> zoomControllers = new ArrayList<>();

	private final GlobalOffset globalOffset = new GlobalOffset();

	private transient boolean updating;

	private Object readResolve() throws ObjectStreamException {
		syncMap = new HashMap<>();
		return this;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = register(player);
	}

	public float getZoom() {
		return zoom;
	}

	public void setZoom(float zoom) {
		this.zoom = zoom;
	}

	public List<ZoomController> getZoomControllers() {
		return zoomControllers;
	}

	public void addZoomController(ZoomController controller) {
		if(!zoomControllers.contains(controller)) {
			zoomControllers.add(controller);
		}
	}

	public float getTime() {
		return time;
	}

	public void schedule(float delay, World.Callback callback) {
		callbacks.add(new ScheduledCallback(getTime() + delay, callback));
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

	public List<Economy> getEconomies() {
		return economies;
	}

	public void startUpdate() {
		time += 1 / v.frameRate;

		for(int i = 0; i < callbacks.size(); i++) {
			ScheduledCallback scheduled = callbacks.get(i);
			if(scheduled.time <= time) {
				callbacks.remove(i--);
				scheduled.callback.callback();
			}
		}

		updating = true;
		for(SpaceObject s : objectsToAdd) {
			addImmediately(s);
		}
		objectsToAdd.clear();
	}

	public void endUpdate() {
		updating = false;
		if(!objectsToRemove.isEmpty()) {
			objects.removeAll(objectsToRemove);
			gravityObjects.removeAll(objectsToRemove);
			objectsToRemove.clear();
		}
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

	@SuppressWarnings("unchecked")
	public <S extends Syncable> S register(S object) {
		// Find already existing object with the same state key
		long id = object.getSyncID();
		if(syncMap.containsKey(id)) {
			S current = (S)syncMap.get(id);
			if(current.isRemote()) {
				// Sync remotely owned object
				current.onSync(object.getSyncData());
				println("<sync>", object.isRemote(), object.getClass().getSimpleName() + "[" + Long.toHexString(id) + "]");
			}
			return current;
		}
		else if(!(object instanceof ConditionalRegister) || ((ConditionalRegister)object).shouldRegister()) {
			add(object);
			syncMap.put(object.getSyncID(), object);
			println("<add>", object.isRemote(), object.getClass().getSimpleName() + "[" + Long.toHexString(id) + "]");
		}
		return object;
	}

	private void add(Syncable object) {
		if(object instanceof SpaceObject) {
			SpaceObject s = (SpaceObject)object;
			if(s.isDestroyed()) {
				return;
			}

			if(updating) {
				objectsToAdd.add(s);
				s.applyVelocity(s.getVelocity().mult(-1));///
			}
			else {
				addImmediately(s);
			}
		}
		else if(object instanceof Person && !people.contains(object)) {
			people.add((Person)object);
		}
		else if(object instanceof Faction && !factions.contains(object) && !(object instanceof PlayerFaction)) {
			factions.add((Faction)object);
		}
		else if(object instanceof Economy && !economies.contains(object)) {
			economies.add((Economy)object);
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
		else if(object instanceof Economy) {
			economies.remove(object);
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

	private static class ScheduledCallback implements Serializable {
		private final float time;
		private final World.Callback callback;

		public ScheduledCallback(float time, World.Callback callback) {
			this.time = time;
			this.callback = callback;
		}
	}
}
