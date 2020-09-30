package vekta.overlay;

import java.io.Serializable;

/**
 * Interface for rendering on-screen overlay elements
 */
public interface Overlay extends Serializable {// TODO: ease away from serializing contexts
	void render();
}
