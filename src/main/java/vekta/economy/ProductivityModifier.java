package vekta.economy;

import java.io.Serializable;

public interface ProductivityModifier extends Serializable {
	String getModifierName();
	
	float updateModifier(Economy economy);
}
