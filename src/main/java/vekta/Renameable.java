package vekta;

import java.io.Serializable;

public interface Renameable extends Serializable {
	String getName();

	void setName(String name);
}
