package vekta.overlay.singleplayer;

import vekta.overlay.TextOverlay;
import vekta.player.Player;

import static vekta.Vekta.UI_COLOR;

public class PlayerScoreOverlay extends TextOverlay {
	private final Player player;

	public PlayerScoreOverlay(int x, int y, Player player) {
		super(x, y);

		this.player = player;
	}

	public long getScore() {
		return player.getScore();
	}

	@Override
	public String getText() {
		return "Score: " + getScore();
	}

	@Override
	public int getColor() {
		return UI_COLOR;
	}
}

