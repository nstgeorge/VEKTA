package vekta.object.ship;

import processing.core.PVector;
import vekta.Player;
import vekta.menu.Menu;
import vekta.menu.option.DialogOption;
import vekta.object.SpaceObject;
import vekta.person.Person;

public class FollowerShip extends FighterShip {
	private final Person person;
	private final Player player;

	public FollowerShip(Person person, Player player, String name, PVector heading, PVector position, PVector velocity, int color) {
		super(name, heading, position, velocity, color);

		this.person = person;
		this.player = player;
	}

	public Person getPerson() {
		return person;
	}

	public Player getPlayer() {
		return player;
	}

	@Override
	public boolean isValidTarget(SpaceObject obj) {
		return obj instanceof Ship && (obj == getPlayer().getShip() || obj.getColor() != getColor());
	}

	@Override
	public boolean shouldUpdateTarget() {
		return getTarget() == getPlayer().getShip() || super.shouldUpdateTarget();
	}

	@Override
	public void setTarget(SpaceObject obj) {
		if(obj == null) {
			obj = getPlayer().getShip();
		}

		super.setTarget(obj);
	}

	@Override
	public float getSpeed() {
		return super.getSpeed() * 1.5F;
	}

	@Override
	public float getAttackScale() {
		return 2;
	}

	@Override
	public void setupDockingMenu(Menu menu) {
		menu.setAuto(new DialogOption("Talk to " + getPerson().getName(), getPerson().createDialog("greeting")));
	}
}  
