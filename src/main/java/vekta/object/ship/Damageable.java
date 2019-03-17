package vekta.object.ship;

public interface Damageable {
	boolean isDamageableFrom(Damager damager);
	
	void damage(float amount, Damager damager);
}
