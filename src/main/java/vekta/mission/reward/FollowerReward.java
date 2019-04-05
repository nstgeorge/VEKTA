package vekta.mission.reward;

import processing.core.PVector;
import vekta.Player;
import vekta.mission.Mission;
import vekta.object.ship.FollowerShip;
import vekta.person.Person;

import static vekta.Vekta.register;

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
		FollowerShip ship = register(new FollowerShip(
				getPerson(),
				player,
				getPerson().getFullName(),
				PVector.random2D(),
				PVector.random2D().normalize().mult(1000),
				new PVector(),
				player.getColor()));
		ship.setPersistent(true);
	}
}
