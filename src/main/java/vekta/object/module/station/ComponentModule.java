package vekta.object.module.station;

import vekta.object.SpaceStation;
import vekta.object.module.Module;

public interface ComponentModule extends Module {
	/**
	 * Get the component width (in tiles)
	 */
	int getWidth();

	/**
	 * Get the component height (in tiles)
	 */
	int getHeight();

	/**
	 * Check if the attachment direction is valid
	 */
	boolean hasAttachmentPoint(SpaceStation.Direction direction);

	/**
	 * Draw the component
	 */
	void draw(float tileSize);
}
