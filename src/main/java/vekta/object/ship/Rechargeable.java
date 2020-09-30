package vekta.object.ship;

import java.io.Serializable;

public interface Rechargeable extends Serializable {
	String getName();

	float getRechargeAmount();

	void recharge(float amount);
}
