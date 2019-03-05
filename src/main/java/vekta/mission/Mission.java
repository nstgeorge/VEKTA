package vekta.mission;

import vekta.Player;
import vekta.PlayerEvent;

import java.util.ArrayList;
import java.util.List;

import static vekta.Vekta.UI_COLOR;

public class Mission {
	private final List<Objective> objectives = new ArrayList<>();
	private final List<Reward> rewards = new ArrayList<>();
	private final List<MissionListener> listeners = new ArrayList<>();
	private final List<Player> players = new ArrayList<>();

	private final String name;

	private MissionStatus status = MissionStatus.READY;

	private Objective current;

	public Mission(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public List<Objective> getObjectives() {
		return objectives;
	}

	public List<Reward> getRewards() {
		return rewards;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void add(MissionListener listener) {
		listeners.add(listener);
		if(listener instanceof Objective) {
			getObjectives().add((Objective)listener);
		}
		if(listener instanceof Reward) {
			getRewards().add((Reward)listener);
		}
	}

	public Objective getCurrentObjective() {
		return current;
	}

	public void updateCurrentObjective() {
		if(getStatus() != MissionStatus.STARTED) {
			return;
		}

		current = null;
		boolean hasCompleted = false;
		for(Objective objective : getObjectives()) {
			if(objective.getStatus() == MissionStatus.STARTED) {
				if(current == null) {
					current = objective;
				}
				if(!objective.isOptional()) {
					return; // Return and select non-optional objective
				}
			}
			else if(objective.getStatus() == MissionStatus.COMPLETED) {
				hasCompleted = true;
			}
		}

		if(hasCompleted) {
			complete(); // Complete mission if an optional objective completed and the rest are optional
		}
		else {
			cancel(); // Cancel mission if all optional objectives are cancelled
		}
	}

	public void cycleObjective() {
		if(objectives.size() > 0) {
			current = objectives.get((objectives.indexOf(current) + 1) % objectives.size());
		}
	}

	public MissionStatus getStatus() {
		return status;
	}

	public void start(Player player) {
		if(players.contains(player)) {
			throw new RuntimeException("Player already started the mission");
		}

		status = MissionStatus.STARTED;
		for(MissionListener listener : listeners) {
			listener.onStart(this, player);
		}

		players.add(player);

		current = objectives.get(0);

		player.emit(PlayerEvent.MISSION_STATUS, this);
		player.send("Mission started: " + getName())
				.withColor(getStatus().getColor())
				.withTime(2);
	}

	public void cancel() {
		status = MissionStatus.CANCELLED;
		for(MissionListener listener : listeners) {
			listener.onCancel(this);
		}

		current = null;

		for(Player p : players) {
			p.emit(PlayerEvent.MISSION_STATUS, this);
			p.send("Mission cancelled: " + getName())
					.withColor(getStatus().getColor())
					.withTime(2);
		}
	}

	public void complete() {
		status = MissionStatus.COMPLETED;
		for(MissionListener listener : listeners) {
			listener.onComplete(this);
		}

		current = null;

		for(Player p : players) {
			p.emit(PlayerEvent.MISSION_STATUS, this);
			p.send("Mission completed: " + getName())
					.withColor(UI_COLOR)
					.withTime(2);
		}
	}
}
