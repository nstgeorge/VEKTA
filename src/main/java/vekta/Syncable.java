package vekta;

import org.reflections.ReflectionUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

import static vekta.Vekta.*;

public abstract class Syncable<T extends Syncable> implements Serializable {
	private final long id = randomID();

	/**
	 * Check whether this object should be synced when modified.
	 */
	public boolean shouldSync() {
		return true;
	}

	/**
	 * Return an immutable key corresponding to this object.
	 */
	public final long getSyncID() {
		return id;
	}

	/**
	 * Return a data object used for synchronization.
	 */
	@SuppressWarnings("unchecked")
	public T getSyncData() {
		return (T)this;
	}

	@SuppressWarnings("unchecked")
	public void onSync(T data) {
		try {
			// TODO: refactor
			Field modifierField = Field.class.getDeclaredField("modifiers");
			modifierField.setAccessible(true);

			// Brute-force update for now
			for(Field field : ReflectionUtils.getAllFields(getClass())) {
				// Skip id field (convention)
				if("id".equals(field.getName())) {
					continue;
				}

				// Beat the devil out of the field
				field.setAccessible(true);
				modifierField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

				// Recursively replace Syncable objects
				Object object = field.get(data);
				if(object instanceof Syncable) {
					field.set(this, register((Syncable)object));
				}
				else if(object instanceof Collection) {
					Collection collection = (Collection)object;
					List buffer = new ArrayList(collection);
					collection.clear();
					for(Object elem : buffer) {
						collection.add(elem instanceof Syncable ? register((Syncable)elem) : elem);
					}
				}
				else if(object instanceof Map) {
					Map map = (Map)object;
					Map buffer = new HashMap<>(map);
					map.clear();
					for(Object key : map.keySet()) {
						Object value = buffer.get(key);
						map.put(key instanceof Syncable ? register((Syncable)key) : key,
								value instanceof Syncable ? register((Syncable)value) : value);
					}
				}
			}
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void syncChanges() {
		getWorld().apply(this);
	}
}
