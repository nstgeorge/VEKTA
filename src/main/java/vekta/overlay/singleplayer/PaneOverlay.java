package vekta.overlay.singleplayer;

import vekta.Player;
import vekta.overlay.Overlay;

import static processing.core.PConstants.*;
import static vekta.Vekta.UI_COLOR;
import static vekta.Vekta.v;

public class PaneOverlay implements Overlay {

    private static final String[] PANES = {"Navigation", "Logs", "Economies"};
    private static final int BAR_HEIGHT = 30;
    private static final int PADDING = 30;

    private final Player player;

    public PaneOverlay(Player player) {
        this.player = player;
    }

    @Override
    public void render() {
        v.stroke(UI_COLOR);
        v.fill(0);
        v.rectMode(CORNERS);
        v.rect(-1, -1, v.width + 1, BAR_HEIGHT);

        v.textAlign(LEFT, CENTER);
        v.fill(UI_COLOR);
        int x = PADDING;
        for(int i = 0; i < PANES.length; i++) {
            v.text("[" + (i+1) + "]: " + PANES[i], x, BAR_HEIGHT / 2F);
            x += v.textWidth("[x]: " + PANES[i]) + PADDING;
        }

        if(player.getCurrentMission() != null) {
            v.text("Missions", x, BAR_HEIGHT / 2F);
        }
    }
}
