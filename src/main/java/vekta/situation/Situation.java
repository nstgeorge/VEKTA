package vekta.situation;

import vekta.Player;

public interface Situation {
	boolean isHappening(Player player);

	void start(Player player);

	void end(Player player);
}
