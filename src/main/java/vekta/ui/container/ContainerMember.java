package vekta.ui.container;

import vekta.ui.Element;

public interface ContainerMember extends Element {

	/**
	 * If this member is selectable, returns true, false otherwise
	 */
	default boolean isSelectable() {
		return true;
	}

	float getSizeY();

	float getYPadding();

}
