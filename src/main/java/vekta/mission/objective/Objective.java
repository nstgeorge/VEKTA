package vekta.mission.objective;

import vekta.PlayerListener;
import vekta.Syncable;
import vekta.mission.Mission;
import vekta.mission.MissionListener;
import vekta.mission.MissionStatus;
import vekta.object.SpaceObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static vekta.Vekta.MISSION_COLOR;
import static vekta.Vekta.println;

public abstract class Objective extends Syncable<Objective> implements MissionListener, PlayerListener {
	private final Set<Mission> missions = new HashSet<>();

	private MissionStatus status = MissionStatus.READY;

	private boolean optional;
	private final List<ObjectiveCallback> next = new ArrayList<>();

	public boolean isOptional() {
		return optional;
	}

	public Objective optional() {
		this.optional = true;
		return this;
	}

	public Objective then(ObjectiveCallback next) {
		this.next.add(next);
		return this;
	}

	public Objective then(Objective objective) {
		return then(mission -> mission.add(objective));
	}

	public final MissionStatus getStatus() {
		return status;
	}

	public Set<Mission> getMissions() {
		return missions;
	}

	public void cancel() {
		for(Mission mission : missions) {
			setStatus(MissionStatus.CANCELLED, mission);
		}
	}

	public void complete() {
		for(Mission mission : missions) {
			setStatus(MissionStatus.COMPLETED, mission);
		}
	}

	private void setStatus(MissionStatus status, Mission mission) {
		if(this.status == status) {
			return;
		}
		this.status = status;
		if(status == MissionStatus.CANCELLED) {
			mission.getPlayer().send("Objective cancelled: " + getDisplayText())
					.withColor(MISSION_COLOR);
			if(isOptional()) {
				mission.cancel();
			}
			mission.getPlayer().removeListener(this);
			next.clear();
		}
		else if(status == MissionStatus.COMPLETED) {
			mission.getPlayer().send("Objective completed: " + getDisplayText())
					.withColor(MISSION_COLOR);
			for(ObjectiveCallback next : this.next) {
				next.callback(mission);
			}
			mission.getPlayer().removeListener(this);
			next.clear();
		}
		mission.updateCurrentObjective();

		sendChanges();
	}

	public String getDisplayText() {
		return (isOptional() ? "(Optional) " : "") + getName();
	}

	public abstract String getName();

	public abstract SpaceObject getSpaceObject();

	@Override
	public final void onStart(Mission mission) {
		if(missions.contains(mission)) {
			println("Warning: objective aleady started for mission: " + mission.getName());
			return;
		}
		missions.add(mission);
		mission.getPlayer().addListener(this);
		if(getStatus() != MissionStatus.STARTED) {
			onStartFirst(mission);
		}
		setStatus(MissionStatus.STARTED, mission);
	}

	@Override
	public final void onCancel(Mission mission) {
		mission.getPlayer().removeListener(this);
		if(getStatus() != MissionStatus.CANCELLED) {
			onCancelFirst(mission);
		}
	}

	@Override
	public final void onComplete(Mission mission) {
		mission.getPlayer().removeListener(this);
		if(getStatus() != MissionStatus.COMPLETED) {
			onCompleteFirst(mission);
		}
	}

	public void onStartFirst(Mission mission) {
	}

	public void onCancelFirst(Mission mission) {
	}

	public void onCompleteFirst(Mission mission) {
	}

	@Override
	public void onSync(Objective data) {
		super.onSync(data);

		//		// TEMP
		//		if(getSpaceObject() != null && !getSpaceObject().isDestroyed()) {
		//			register(data.getSpaceObject());
		//		}
	}

	public interface ObjectiveCallback extends Serializable {
		void callback(Mission mission);
	}
}
