package vekta.situation;

import vekta.Player;

public interface Situation {
	boolean isHappening(Player player);

	default void start(Player player) {
	}

	default void during(Player player) {
	}

	default void end(Player player) {
	}
}
