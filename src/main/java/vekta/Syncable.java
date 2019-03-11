package vekta;

import java.io.Serializable;

import static vekta.Vekta.getWorld;

public interface Syncable<T extends Serializable> {
	/**
	 * Check whether this object should be synced when modified. 
	 * */
	default boolean shouldSync(){
		return true;
	}
	
	/**
	 * Return an immutable key corresponding to this object.
	 */
	String getSyncKey();

	/**
	 * Return a data object used for synchronization.
	 */
	T getSyncData();

	/**
	 * Synchromize based on the given data object.
	 */
	void onSync(T data);

	default void applyChanges() {
		getWorld().apply(this);
	}
}
