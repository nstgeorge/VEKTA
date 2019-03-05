package vekta.overlay;

import vekta.Counter;

/**
 * General-purpose overlay with external state management.
 */
public abstract class CachingOverlay extends TextOverlay {
	private final Counter counter;

	private String text;
	private int color;

	public CachingOverlay(int x, int y, int interval) {
		super(x, y);

		this.counter = new Counter(interval).ready();
	}

	public Counter getCounter() {
		return counter;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public int getColor() {
		return color;
	}

	@Override
	public void render() {
		if(getCounter().cycle()) {
			text = getTextCache();
			color = getColorCache();
		}
		super.render();
	}

	public abstract String getTextCache();

	public abstract int getColorCache();
}
