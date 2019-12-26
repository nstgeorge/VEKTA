package vekta;

import java.io.Serializable;

import static vekta.Vekta.v;

public final class Counter implements Serializable {
	private final int interval;
	private int ct;

	public Counter() {
		this(0);
	}

	public Counter(int interval) {
		this.interval = interval;
	}

	public int getInterval() {
		return interval;
	}

	public int getProgress() {
		return ct;
	}

	public Counter setProgress(int ct) {
		this.ct = ct;
		return this;
	}

	public Counter setReady() {
		ct = interval;
		return this;
	}

	public Counter randomize() {
		ct = (int)v.random(interval);
		return this;
	}

	public void delay(int amount) {
		ct -= amount;
	}

	public void reset() {
		ct = 0;
	}

	public boolean step() {
		return ++ct >= interval;
	}

	public boolean cycle() {
		if(step()) {
			reset();
			return true;
		}
		return false;
	}
}
