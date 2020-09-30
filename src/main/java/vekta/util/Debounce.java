package vekta.util;

import java.io.Serializable;

import static vekta.Vekta.getWorld;
import static vekta.Vekta.v;

public final class Debounce implements Serializable {
	private final float minTime;
	private final float maxTime;

	private float nextTime;

	public Debounce(float time) {
		this(time, time);
	}

	public Debounce(float minTime, float maxTime) {
		this.minTime = minTime;
		this.maxTime = maxTime;
	}

	public float getNextTime() {
		return nextTime;
	}

	public boolean isReady() {
		return nextTime <= getWorld().getTime();
	}

	public Debounce setReady() {
		return setReady(0);
	}

	public Debounce setReady(float time) {
		nextTime = getWorld().getTime() + time;
		return this;
	}

	public Debounce reset() {
		setReady(v.random(minTime, maxTime));
		return this;
	}

	public void delay(float time) {
		nextTime += time;
	}

	public boolean check() {
		if(isReady()) {
			reset();
			return true;
		}
		return false;
	}
}
