package vekta.ui;

import vekta.Resources;

import static vekta.Vekta.v;

public class Typewriter implements Element {

	private static final float VOLUME = 0.2f;

	private enum Mode {

		NORMAL(50, "typewriter_normal"),
		WHITESPACE(NORMAL.outputSpeed * 2, "typewriter_quiet"),
		ELLIPSE(3, "typewriter_special"),
		NEWLINE(2, "typewriter_special");

		public final int outputSpeed;		// Characters per second to print in this output mode
		public final String sound;			// Key of the string to play for this sound

		Mode(int i, String sound) {
			outputSpeed = i;
			this.sound = sound;
		}
	}

	private final String output;	// String to be revealed
	private Mode mode;				// Mode that the Typewriter is currently in (determines speed of output)
	private int index;				// Index of the current character
	private long lastCharTime;		// Last time a character was displayed
	private boolean pause;			// Flag to temporarily pause output

	int x, y;


	public Typewriter(String output, int x, int y) {
		this.output = output;
		this.x = x;
		this.y = y;
		lastCharTime = 0;

		this.mode = Mode.NORMAL;
	}

	@Override
	public void render() {
		setStyle();
		if(isComplete()) {
			v.color(255);
			v.text(output, x, y);
		} else {
			v.color(255);
			v.text(output.substring(0, index), x, y);

			if(v.millis() >= lastCharTime + (1000 / mode.outputSpeed) && !pause) {
				lastCharTime = v.millis();

				Resources.playSound(mode.sound, VOLUME);
				index++;

				// Handle mode checking
				if(output.charAt(index - 1) == '\n') {
					mode = Mode.NEWLINE;
				}
				else if(output.charAt(index - 1) == ' ' || output.charAt(index - 1) == '\t') {
					mode = Mode.WHITESPACE;
				}
				else if(output.charAt(index - 1) == '.') {
					mode = Mode.ELLIPSE;
				} else {
					mode = Mode.NORMAL;
				}
			}
		}
	}

	public void pause() {
		pause = true;
	}

	public void unpause() {
		pause = false;
	}

	public boolean isComplete() {
		return index >= output.length() - 1;
	}

	public void setStyle() {
		v.color(v.UI_COLOR);
	}
}
