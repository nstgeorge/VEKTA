package vekta.module.station;

import vekta.module.BaseModule;
import vekta.object.ship.SpaceStation;
import vekta.world.RenderLevel;

public interface ComponentModule extends BaseModule {
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
	void draw(RenderLevel dist, float tileSize);
}
