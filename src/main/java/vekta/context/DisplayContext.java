package vekta.context;

import vekta.display.Display;

import static vekta.Vekta.v;

public class DisplayContext implements Context {

	private final Display display;

	public DisplayContext(Display display) {
		this.display = display;
	}

	@Override
	public void render() {
		display.draw(v.width, v.height);
	}
}
