package vekta.display;

public class Toggle<T extends Display> implements Display {
	private T display;
	private boolean visible;

	public Toggle(T display) {
		this(display, true);
	}

	public Toggle(T display, boolean visible) {
		setDisplay(display);
		setVisible(visible);
	}

	public T getDisplay() {
		return display;
	}

	public void setDisplay(T display) {
		this.display = display;
	}

	public boolean isVisible() {
		return visible;
	}

	public boolean setVisible(boolean visible) {
		return this.visible = visible;
	}

	public boolean toggle() {
		return setVisible(!isVisible());
	}

	@Override
	public float getWidth(float width, float height) {
		return isVisible() ? getDisplay().getWidth(width, height) : 0;
	}

	@Override
	public float getHeight(float width, float height) {
		return isVisible() ? getDisplay().getHeight(width, height) : 0;
	}

	@Override
	public void draw(float width, float height) {
		if(isVisible()) {
			getDisplay().draw(width, height);
		}
	}
}
