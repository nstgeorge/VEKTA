package vekta.connection.message;

import vekta.connection.MessageListener;
import vekta.connection.Peer;
import vekta.mission.Mission;

public class ShareMissionMessage implements Message {
	private final Mission mission;

	public ShareMissionMessage(Mission mission) {
		this.mission = mission;
	}

	public Mission getMission() {
		return mission;
	}

	@Override
	public void receive(Peer peer, MessageListener listener) {
		listener.onShareMission(peer, this);
	}
}
