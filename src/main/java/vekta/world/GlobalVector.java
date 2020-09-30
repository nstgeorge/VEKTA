package vekta.world;

import java.io.Serializable;

public final class GlobalVector implements Serializable {
	public double x, y;

	public GlobalVector() {
		this(0, 0);
	}

	public GlobalVector(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public void set(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public void add(double x, double y) {
		this.x += x;
		this.y += y;
	}

	public void sub(double x, double y) {
		this.x -= x;
		this.y -= y;
	}
}
