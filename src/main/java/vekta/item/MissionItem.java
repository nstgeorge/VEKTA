package vekta.item;

import vekta.Player;
import vekta.mission.Mission;

import java.io.Serializable;

public class MissionItem extends Item {
	private final MissionProvider provider;

	private boolean activated;

	public MissionItem(String name, Mission mission) {
		this(name, player -> mission);
	}

	public MissionItem(String name, MissionProvider provider) {
		super(name, ItemType.MISSION);

		this.provider = provider;
	}

	public boolean isActivated() {
		return activated;
	}

	public Mission createMission(Player player) {
		activated = true;
		return provider.createMission(player);
	}

	@Override
	public void onAdd(Player player) {
		createMission(player).start();
	}
	
	public interface MissionProvider extends Serializable {
		Mission createMission(Player player);
	}
}
