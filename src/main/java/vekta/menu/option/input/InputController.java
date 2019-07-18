package vekta.menu.option.input;

import vekta.KeyBinding;
import vekta.menu.Menu;

import java.io.Serializable;

public interface InputController<T> extends Serializable {

	String getName(T value);

	default boolean hasLeft(T value) {
		return getLeft(value) != null;
	}

	default boolean hasRight(T value) {
		return getRight(value) != null;
	}

	default T getLeft(T value) {
		return null;
	}

	default T getRight(T value) {
		return null;
	}

	String getSelectVerb();

	void select(Menu menu, InputWatcher<T> watcher);

	default boolean interceptKeyPressed(Menu menu, KeyBinding key, InputWatcher<T> watcher) {
		return false;
	}
}
