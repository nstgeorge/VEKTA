package vekta.menu.option.input;

import java.io.Serializable;

public interface InputNamer<T> extends Serializable {
	String getName(T value);
}
