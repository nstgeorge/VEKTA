package vekta.mission.objective;

import vekta.PlayerListener;
import vekta.Syncable;
import vekta.mission.Mission;
import vekta.mission.MissionListener;
import vekta.mission.MissionStatus;
import vekta.object.SpaceObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static vekta.Vekta.MISSION_COLOR;
import static vekta.Vekta.println;

public abstract class Objective extends Syncable<Objective> implements MissionListener, PlayerListener {
	private final List<Mission> missions = new ArrayList<>();

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

	public List<Mission> getMissions() {
		return missions;
	}

	public void cancel() {
		setStatus(MissionStatus.CANCELLED);
	}

	public void complete() {
		setStatus(MissionStatus.COMPLETED);
	}

	private void setStatus(MissionStatus status) {
		if(this.status == status) {
			return;
		}
		this.status = status;

		for(Mission mission : getMissions()) {
			if(status == MissionStatus.CANCELLED) {
				mission.getPlayer().send("Objective cancelled: " + getDisplayText())
						.withColor(MISSION_COLOR);
				mission.getPlayer().removeListener(this);
				next.clear();
				if(!isOptional()) {
					mission.cancel();
				}
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
		}

		syncChanges();
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
			setStatus(MissionStatus.STARTED);
		}
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

	public interface ObjectiveCallback extends Serializable {
		void callback(Mission mission);
	}

	@Override
	public boolean shouldSendChanges() {
		return /*!isRemote() || */getStatus().isDone();
	}

	@Override
	public void onSync(Objective data) {
		super.onSync(data);

		println(getStatus(), data.getStatus(), getMissions());////

		setStatus(data.getStatus());
	}
}
