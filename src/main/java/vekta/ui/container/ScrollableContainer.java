package vekta.ui.container;

import static processing.core.PConstants.CORNERS;
import static vekta.Vekta.v;

public class ScrollableContainer extends Container {

	private static final int ITEMS_BEFORE_SCROLL = 8; // Number of items before menu starts scrolling
	private static final int DEFAULT_SCROLL_RATE = 10;
	private static final int BLOCKING_ELEMENT_PADDING = 20;	// Amount of padding on blocking elements - helps cover letters that go below the baseline (p, g, y, etc)

	private int scrollIndex;	// Container member to scroll to
	private float scrollOffset;	// Offset (px) for the current scroll index

	public ScrollableContainer(int x, int y, int sizeX, int sizeY) {
		super(x, y, sizeX, sizeY);
		scrollIndex = 1;
		scrollOffset = 0;
	}

	public ScrollableContainer() {
		this(0, v.height / 2 - 64, v.width, v.height);
	}

	/**
	 * Scroll given amount
	 */
	public void scroll(int amount) {
		onScroll(amount);
		scrollIndex = Math.max(1, Math.min(getElementCount(), scrollIndex += amount));
	}

	/**
	 * Code that runs any time a scroll event occurs
	 */
	public void onScroll(int amount) { }

	/**
	 * Get the scroll offset of the container in px
	 * @return
	 */
	public float getScrollOffset() {
		return scrollOffset;
	}

	/**
	 * Get the scroll rate for this scrollable container
	 * @return
	 */
	private int getScrollRate() {
		return DEFAULT_SCROLL_RATE;
	}

	/**
	 * Offset the following drawing logic by the calculated scroll amount for this container.
	 * Should be followed by drawing, then endScrolledContext().
	 * This allows inheriting classes to define their own rendering logic if necessary.
	 */
	public void beginScrolledContext() {
		// Calculate scroll location
		int extraOptions = getElementCount() - ITEMS_BEFORE_SCROLL;
		float targetOffset = 0;
		if(extraOptions > 0) {
			for(int i = 0; i < scrollIndex; i++) {
				targetOffset += getElement(i).getSizeY() + getElement(i).getYPadding();
			}
			// Try to keep the selected item in the middle of screen, unless it would move the contents of the container out of bounds
			targetOffset -= getSizeY() / 2f;
			targetOffset = Math.max(0, targetOffset);

			scrollOffset += (targetOffset - scrollOffset) * getScrollRate() / 60;
		}

		// Offset current drawing by the calculated translation amount
		v.pushMatrix();
		v.translate(getX(),  -scrollOffset);
	}

	/**
	 * Ends the context in which the elements have been scrolled.
	 */
	public void endScrolledContext() {
		// Reset the translation
		v.popMatrix();
	}

	/**
	 * Draws elements which block visibility of elements outside of bounds of the scrolled menu.
	 * Anything drawn before this may be covered by the blocking elements. Anything after is safe.
	 */
	public void drawBlockingElements() {
		// Draw rectangles to cover scrolled elements
		v.pushStyle();
		v.fill(v.g.backgroundColor);
		v.stroke(v.g.backgroundColor);
		v.rectMode(CORNERS);
		v.rect(getX(), 0, getX() + getSizeX(), getY() + BLOCKING_ELEMENT_PADDING);
		v.rect(getX(), getSizeY() + getY() - BLOCKING_ELEMENT_PADDING, getSizeX() + getX(), v.height);
		v.popStyle();
	}

	@Override
	public void render() {
		beginScrolledContext();
		renderElements();
		endScrolledContext();
	}
}
