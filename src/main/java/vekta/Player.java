package vekta;

import vekta.item.Inventory;
import vekta.item.Item;
import vekta.item.MissionItem;
import vekta.mission.Mission;
import vekta.object.ship.ModularShip;
import vekta.overlay.singleplayer.Notification;

import java.util.ArrayList;
import java.util.List;

public final class Player {
	private final int color;

	private final List<PlayerListener> listeners = new ArrayList<>();

	private final List<Mission> missions = new ArrayList<>();
	private Mission currentMission;

	private ModularShip currentShip;

	public Player(int color) {
		this.color = color;

		addListener(new PlayerListener() {
			@Override
			public void onChangeShip(ModularShip ship) {
				if(ship == null) {
					throw new RuntimeException("Player ship cannot be null");
				}
				if(currentShip != null) {
					currentShip.setController(null);
				}
				currentShip = ship;
				ship.setController(Player.this);
			}

			@Override
			public void onMissionStatus(Mission mission) {
				switch(mission.getStatus()) {
				case READY:
				case STARTED:
					if(!missions.contains(mission)) {
						missions.add(mission);
						setCurrentMission(mission);
					}
					break;
				case CANCELLED:
				case COMPLETED:
					missions.remove(mission);
					if(getCurrentMission() == mission) {
						setCurrentMission(null);
					}
					break;
				}
			}

			@Override
			public void onKeyPress(ControlKey key) {
				if(key == ControlKey.OBJECTIVE_CYCLE && getCurrentMission() != null) {
					getCurrentMission().cycleObjective();
				}
				if(key == ControlKey.MISSION_CYCLE && missions.size() > 0) {
					int index = missions.indexOf(getCurrentMission()) + 1;
					setCurrentMission(index == getMissions().size() ? missions.get(index % missions.size()) : null);
				}
			}

			@Override
			public void onAddItem(Item item) {
				if(item instanceof MissionItem) {
					((MissionItem)item).getMission().start(Player.this);
				}
			}
		});
	}

	public int getColor() {
		return color;
	}

	public ModularShip getShip() {
		return currentShip;
	}

	public Inventory getInventory() {
		return getShip().getInventory();
	}

	public List<Mission> getMissions() {
		return missions;
	}

	public Mission getCurrentMission() {
		return currentMission;
	}

	public void setCurrentMission(Mission currentMission) {
		this.currentMission = currentMission;
	}

	public void addListener(PlayerListener listener) {
		this.listeners.add(listener);
	}

	public boolean removeListener(PlayerListener listener) {
		return this.listeners.remove(listener);
	}

	public void emit(PlayerEvent event, Object data) {
		for(PlayerListener listener : new ArrayList<>(listeners)) {
			event.handle(listener, data);
		}
	}

	public Notification send(String notification) {
		return send(new Notification(notification));
	}

	public Notification send(Notification notification) {
		emit(PlayerEvent.NOTIFICATION, notification);
		return notification;
	}
}
