package vekta;

import static vekta.Vekta.*;

public final class Counter {
	private final int frames;
	private int ct;

	public Counter() {
		this(0);
	}

	public Counter(int frames) {
		this.frames = frames;
	}

	public int getFrames() {
		return frames;
	}

	public int getProgress() {
		return ct;
	}

	public void setProgress(int ct) {
		this.ct = ct;
	}

	public Counter ready() {
		ct = frames;
		return this;
	}

	public Counter randomize() {
		ct = (int)v.random(frames);
		return this;
	}

	public void delay(int amount) {
		ct -= amount;
	}

	public void reset() {
		ct = 0;
	}

	public boolean step() {
		return ++ct >= frames;
	}

	public boolean cycle() {
		if(step()) {
			reset();
			return true;
		}
		return false;
	}
}
