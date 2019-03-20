package vekta.display;

public abstract class StyledDisplay implements Display {
	private DisplayStyle style;

	private boolean custom;

	public StyledDisplay(DisplayStyle style) {
		this.style = style;
	}

	public DisplayStyle getStyle() {
		return style;
	}

	public DisplayStyle customize() {
		// Derive custom style from parent
		if(!custom) {
			custom = true;
			style = style.derive();
		}
		return style;
	}
}
