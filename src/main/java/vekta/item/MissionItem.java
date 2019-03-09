package vekta.item;

import vekta.Player;
import vekta.mission.Mission;

import java.util.function.Function;

public class MissionItem extends Item {
	private final Function<Player, Mission> generator;

	private boolean activated;

	public MissionItem(String name, Mission mission) {
		this(name, player -> mission);
	}

	public MissionItem(String name, Function<Player, Mission> generator) {
		super(name, ItemType.MISSION);

		this.generator = generator;
	}

	public boolean isActivated() {
		return activated;
	}

	public Mission createMission(Player player) {
		activated = true;
		return generator.apply(player);
	}

	@Override
	public void onAdd(Player player) {
		createMission(player).start();
	}
}
