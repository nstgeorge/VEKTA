package vekta;

import java.util.function.BiConsumer;

public enum PlayerEvent {
	KEY_PRESS(PlayerListener::onKeyPress),
	KEY_RELEASE(PlayerListener::onKeyRelease),
	MENU(PlayerListener::onMenu),
	CHANGE_SHIP(PlayerListener::onChangeShip),
	DAMAGE_SHIP(PlayerListener::onDamageShip),
	NOTIFICATION(PlayerListener::onNotification),
	LAND(PlayerListener::onLand),
	DOCK(PlayerListener::onDock),
	MISSION_STATUS(PlayerListener::onMissionStatus),
	GAME_OVER(PlayerListener::onGameOver),
	INSTALL_MODULE(PlayerListener::onInstallModule),
	UNINSTALL_MODULE(PlayerListener::onUninstallModule),
	ADD_ITEM(PlayerListener::onAddItem),
	REMOVE_ITEM(PlayerListener::onRemoveItem),
	DESTROY_OBJECT(PlayerListener::onDestroyObject);

	private final BiConsumer<PlayerListener, ?> action;

	<T> PlayerEvent(BiConsumer<PlayerListener, T> action) {
		this.action = action;
	}

	@SuppressWarnings("unchecked")
	protected <T> void handle(PlayerListener listener, Object data) {
		listener.onEvent(this);
		((BiConsumer<PlayerListener, T>)action).accept(listener, (T)data);
	}
}
