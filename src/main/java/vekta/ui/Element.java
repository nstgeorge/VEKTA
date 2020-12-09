package vekta.ui;

import java.io.Serializable;	// Allows classes that implement Element interface to be serializable

/**
 * A minimal interface that every UI element inherits from.
 */
public interface Element extends Serializable {
	/**
	 * Draw and update element
	 */
	void render();
}
