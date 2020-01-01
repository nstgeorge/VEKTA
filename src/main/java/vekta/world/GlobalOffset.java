package vekta.world;

import processing.core.PVector;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public final class GlobalOffset implements Externalizable {
	public double px, py;
	public float vx, vy;

	public GlobalOffset() {

	}

	public void setPosition(double px, double py) {
		this.px = px;
		this.py = py;
	}

	public void addPosition(double px, double py) {
		this.px += px;
		this.py += py;
	}

	public void setVelocity(float vx, float vy) {
		this.vx = vx;
		this.vy = vy;
	}

	public void addVelocity(float vx, float vy) {
		this.vx += vx;
		this.vy += vy;
	}

	public void sub(GlobalOffset other) {
		this.px -= other.px;
		this.py -= other.py;
		this.vx -= other.vx;
		this.vy -= other.vy;
	}

	public PVector relativePosition(GlobalOffset other) {
		return new PVector((float)(this.px - other.px), (float)(this.py - other.py));
	}

	public PVector relativeVelocity(GlobalOffset other) {
		return new PVector(this.vx - other.vx, this.vy - other.vy);
	}

	public void update(float timeScale) {
		addPosition(vx * timeScale, vy * timeScale);
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeDouble(px);
		out.writeDouble(py);
		out.writeFloat(vx);
		out.writeFloat(vx);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException {
		px = in.readDouble();
		py = in.readDouble();
		vx = in.readFloat();
		vy = in.readFloat();
	}
}
