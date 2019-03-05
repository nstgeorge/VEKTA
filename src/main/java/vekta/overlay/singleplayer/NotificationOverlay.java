package vekta.overlay.singleplayer;

import vekta.Resources;
import vekta.overlay.PositionOverlay;

import java.util.ArrayList;
import java.util.List;

/**
 * Notification stream
 */
public class NotificationOverlay extends PositionOverlay {
	private final List<Notification> notifications = new ArrayList<>();

	public NotificationOverlay(int x, int y) {
		super(x, y);
	}

	public void add(Notification n) {
		Resources.playSound("change"); // TODO: custom notification sound
		notifications.add(0, n);
	}

	@Override
	public void render() {
		for(int i = 0; i < notifications.size(); i++) {
			Notification n = notifications.get(i);
			n.draw(getX(), getNotificationY(n, i));
			if(n.isDone()) {
				notifications.remove(i--);
			}
		}
	}

	private float getNotificationY(Notification n, int i) {
		return getY() - (30 * i + 20 * n.getProgress());
	}
}
