package vekta.ui;

import java.io.Serializable;

/**
 * A minimal interface that every UI element inherits from.
 */
public interface Element extends Serializable {
	/**
	 * Draw and update element
	 */
	void render();
}
