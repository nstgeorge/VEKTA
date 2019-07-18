package vekta.menu.option.input;

import java.io.Serializable;

public interface InputWatcher<T> extends Serializable {
	T getValue();

	void setValue(T value);
}
