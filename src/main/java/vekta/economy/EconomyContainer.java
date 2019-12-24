package vekta.economy;

public interface EconomyContainer {
	String getName();

	int getColor();

	boolean isEconomyAlive();

	default void updateEconomy() {
	}
}
