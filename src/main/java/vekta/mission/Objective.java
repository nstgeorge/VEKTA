package vekta.mission;

import vekta.Player;
import vekta.PlayerListener;
import vekta.object.SpaceObject;

import java.util.ArrayList;
import java.util.List;

import static vekta.Vekta.MISSION_COLOR;

public abstract class Objective implements MissionListener, PlayerListener {
	private Mission mission;
	private Player player;

	private MissionStatus status = MissionStatus.READY;

	private boolean optional;
	private final List<Runnable> next = new ArrayList<>();

	public boolean isOptional() {
		return optional;
	}

	public Objective optional() {
		this.optional = true;
		return this;
	}

	public Objective then(Runnable next) {
		this.next.add(next);
		return this;
	}

	public Objective then(Objective next) {
		this.then(() -> getMission().add(next));
		return this;
	}

	public final Mission getMission() {
		return mission;
	}

	public Player getPlayer() {
		return player;
	}

	public final MissionStatus getStatus() {
		return status;
	}

	public void start() {
		setStatus(MissionStatus.STARTED);
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
		if(status == MissionStatus.CANCELLED) {
			getPlayer().send("Objective cancelled: " + getDisplayText())
					.withColor(MISSION_COLOR);
			if(isOptional()) {
				getMission().cancel();
			}
		}
		else if(status == MissionStatus.COMPLETED) {
			getPlayer().send("Objective completed: " + getDisplayText())
					.withColor(MISSION_COLOR);
			for(Runnable next : this.next) {
				next.run();
			}
		}
		getMission().updateCurrentObjective();
	}

	public String getDisplayText() {
		return (isOptional() ? "(Optional) " : "") + getName();
	}

	public abstract String getName();

	public abstract SpaceObject getSpaceObject();

	@Override
	public final void onStart(Mission mission, Player player) {
		if(this.mission != null) {
			throw new RuntimeException("Objective already started");
		}
		this.mission = mission;
		this.player = player;
		status = MissionStatus.STARTED;
		player.addListener(this);
		onStart();
	}

	@Override
	public final void onCancel(Mission mission) {
		player.removeListener(this);
		onCancel();
	}

	@Override
	public final void onComplete(Mission mission) {
		player.removeListener(this);
		onComplete();
	}

	public void onStart() {
	}

	public void onCancel() {
	}

	public void onComplete() {
	}
}
