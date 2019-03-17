package vekta.object.ship;

public interface Rechargeable {
	String getName();

	float getRechargeAmount();

	void recharge(float amount);
}
