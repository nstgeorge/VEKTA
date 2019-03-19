package vekta.overlay.singleplayer;

import vekta.Player;
import vekta.PlayerListener;
import vekta.object.ship.ModularShip;
import vekta.overlay.Overlay;

import static processing.core.PConstants.LEFT;
import static vekta.Vekta.bodyFont;
import static vekta.Vekta.v;

public class PlayerOverlay implements Overlay, PlayerListener {
	private final Player player;

	private Overlay[] overlays;
	private NotificationOverlay notifications;

	public PlayerOverlay(Player player) {
		this.player = player;
		reset();
	}

	public void reset() {
		overlays = new Overlay[] {
				new TelemetryOverlay(player),
				new MissionOverlay(player),
				new ShipComputerOverlay(50, -150, player.getShip()),
				new ShipMoneyOverlay(-300, -90, player.getShip()),
				new ShipEnergyOverlay(-300, -60, player.getShip()),
				new ShipTemperatureOverlay(-300, -30, player.getShip()),
				new DirectoryOverlay(player),
				notifications = new NotificationOverlay(-20, 80),
		};
	}

	@Override
	public void render() {
		// Set overlay text settings
		v.textFont(bodyFont);
		v.textAlign(LEFT);
		v.textSize(16);

		for(Overlay overlay : overlays) {
			overlay.render();
		}
	}

	@Override
	public void onChangeShip(ModularShip ship) {
		reset();
	}

	@Override
	public void onNotification(Notification notification) {
		notifications.add(notification);
	}
}
