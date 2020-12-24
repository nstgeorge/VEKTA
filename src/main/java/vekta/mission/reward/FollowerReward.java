package vekta.mission.reward;

import vekta.mission.Mission;
import vekta.person.Person;
import vekta.player.Player;

public class FollowerReward extends Reward {
	private final Person person;

	public FollowerReward(Person person) {
		this.person = person;
	}

	public Person getPerson() {
		return person;
	}

	@Override
	public String getName() {
		return "Follower (" + getPerson().getName() + ")";
	}

	@Override
	public int getColor() {
		return getPerson().getColor();
	}

	@Override
	public void onReward(Mission mission, Player player) {
		//		FollowerShip ship = register(new FollowerShip(
		//				getPerson(),
		//				player,
		//				getPerson().getFullName(),
		//				PVector.random2D(),
		//				player.getShip().getPosition().add(PVector.random2D().normalize().mult(1000)),
		//				new PVector(),
		//				player.getColor()));
		//		ship.setPersistent(true);
		getPerson().setFaction(player.getFaction());
	}
}
