package vekta.object.particle;

import java.io.Serializable;

public final class ParticleStyle implements Serializable {
	// Style defaults
	private float lifetime = 10; // Particle lifetime (in seconds)
	private float drag = 0; // Particle lifetime (in seconds)
	private ColorSelector startColor = new ConstantColor(255);
	private ColorSelector endColor = new ConstantColor(255);

	public ParticleStyle() {

	}

	public float getLifetime() {
		return lifetime;
	}

	public ParticleStyle withLifetime(float lifetime) {
		this.lifetime = lifetime;
		return this;
	}

	public float getDrag() {
		return drag;
	}

	public ParticleStyle withDrag(float drag) {
		this.drag = drag;
		return this;
	}

	public ColorSelector getStartColor() {
		return startColor;
	}

	public ParticleStyle withStartColor(ColorSelector selector) {
		this.startColor = selector;
		return this;
	}

	public ColorSelector getEndColor() {
		return endColor;
	}

	public ParticleStyle withEndColor(ColorSelector selector) {
		this.endColor = selector;
		return this;
	}
}
