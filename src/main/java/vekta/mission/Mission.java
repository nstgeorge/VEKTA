package vekta.mission;

import vekta.player.Player;
import vekta.player.PlayerEvent;
import vekta.Resources;
import vekta.sync.Syncable;
import vekta.connection.message.ShareMissionMessage;
import vekta.mission.objective.Objective;
import vekta.mission.reward.Reward;
import vekta.util.RomanNumerals;

import java.util.ArrayList;
import java.util.List;

import static processing.core.PApplet.println;
import static vekta.Vekta.*;

public class Mission extends Syncable<Mission> {
	private final List<Objective> objectives = new ArrayList<>();
	private final List<Reward> rewards = new ArrayList<>();
	private final List<MissionListener> listeners = new ArrayList<>();

	private final Player player;
	private final String name;
	private final MissionIssuer issuer;
	private final int tier;

	private MissionStatus status = MissionStatus.READY;

	private Objective current;

	public Mission(Player player, String name, MissionIssuer issuer, int tier) {
		this.player = player;
		this.name = name;
		this.issuer = issuer;
		this.tier = tier;
	}

	public Player getPlayer() {
		return player;
	}

	public String getName() {
		return "(" + RomanNumerals.toRoman(tier) + ") " + name;
	}

	public MissionIssuer getIssuer() {
		return issuer;
	}

	public int getTier() {
		return tier;
	}

	public List<Objective> getObjectives() {
		return objectives;
	}

	public List<Reward> getRewards() {
		return rewards;
	}

	public void add(MissionListener listener) {
		listeners.add(listener);
		if(listener instanceof Objective) {
			Objective objective = (Objective)listener;
			getObjectives().add(objective);
			if(getStatus() == MissionStatus.STARTED) {
				objective.onStart(this);
			}
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
			if(objective.getStatus() == MissionStatus.READY) {
				objective.onStart(this);
			}

			if(objective.getStatus() == MissionStatus.STARTED) {
				if(current == null) {
					current = objective;
				}
				if(!objective.isOptional() && !objective.isCondition()) {
					return; // Return and select objective
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

	public void start() {
		if(getStatus() == MissionStatus.STARTED) {
			throw new RuntimeException("Mission has already started");
		}
		if(objectives.isEmpty()) {
			println("Mission has no objectives");
			return;
		}

		status = MissionStatus.STARTED;
		for(MissionListener listener : listeners) {
			listener.onStart(this);
		}

		current = objectives.get(0);

		getPlayer().emit(PlayerEvent.MISSION_STATUS, this);
		getPlayer().send("Mission started: " + getName())
				.withColor(getStatus().getColor())
				.withTime(2);

		syncChanges();
	}

	public void cancel() {
		if(getStatus() != MissionStatus.STARTED) {
			println("Mission cannot be cancelled from state: " + getStatus());
			return;
		}

		for(MissionListener listener : listeners) {
			listener.onCancel(this);
		}
		status = MissionStatus.CANCELLED;
		current = null;

		getPlayer().emit(PlayerEvent.MISSION_STATUS, this);
		getPlayer().send("Mission cancelled: " + getName())
				.withColor(getStatus().getColor())
				.withTime(2);

		syncChanges();
	}

	public void complete() {
		if(getStatus() != MissionStatus.STARTED) {
			println("Mission cannot be completed from state: " + getStatus());
			return;
		}
		
		println(getName(), listeners);/////
		
		for(MissionListener listener : listeners) {
			listener.onComplete(this);
		}
		status = MissionStatus.COMPLETED;
		current = null;

		getPlayer().emit(PlayerEvent.MISSION_STATUS, this);
		Resources.playSound("missionComplete");
		getPlayer().send("Mission completed: " + getName())
				.withColor(UI_COLOR);

		syncChanges();
	}

	public void share(Player player) {
		Mission mission = register(new Mission(player, this.name, getIssuer(), getTier()));
//		for(MissionListener listener : listeners) {
//			mission.add(listener);
//		}
		for(Objective objective : getObjectives()) {
//			if(!objective.getStatus().isDone()){
				mission.add(objective);
//			}
		}
		
		println(mission.getObjectives());///////
//		if(!mission.getObjectives().isEmpty()){
			getWorld().sendMessage(player, new ShareMissionMessage(mission));
//		}
	}
}
