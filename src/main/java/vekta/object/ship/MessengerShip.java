package vekta.object.ship;

import processing.core.PVector;
import vekta.Player;
import vekta.RenderLevel;
import vekta.menu.Menu;
import vekta.menu.option.DialogOption;
import vekta.menu.option.LootMenuOption;
import vekta.object.SpaceObject;
import vekta.person.Dialog;

public class MessengerShip extends Ship {
	private static final float DEF_MASS = 1000;
	private static final float DEF_RADIUS = 5;
	private static final float DEF_SPEED = .2F;
	private static final float DEF_TURN = 4;
	private static final float APPROACH_FACTOR = .1F;

	private final Player player;
	private final Dialog dialog;

	private boolean delivered;

	public MessengerShip(Player player, Dialog dialog, String name, PVector heading, PVector position, PVector velocity, int color) {
		super(name, heading, position, velocity, color, DEF_SPEED, DEF_TURN);

		this.player = player;
		this.dialog = dialog;
	}

	public Player getPlayer() {
		return player;
	}

	public ModularShip getTargetShip() {
		return getPlayer().getShip();
	}

	public Dialog getDialog() {
		return dialog;
	}

	@Override
	public float getMass() {
		return DEF_MASS;
	}

	@Override
	public float getRadius() {
		return DEF_RADIUS;
	}

	@Override
	public RenderLevel getRenderLevel() {
		return RenderLevel.AROUND_PLANET; // Increase despawn radius compared to other ships
	}

	@Override
	public void onUpdate(RenderLevel level) {
		PVector offset = getTargetShip().getPosition().sub(getPosition());

		if(!delivered) {
			// Approach without creating relative velocity
			setVelocity(getTargetShip().getVelocity());
			getPositionReference().add(offset.setMag(offset.mag() * getSpeed() * APPROACH_FACTOR));
		}
		else {
			addVelocity(offset.setMag(-getSpeed()));
		}
	}

	@Override
	public boolean isDockable(SpaceObject s) {
		return s == getTargetShip();
	}

	@Override
	public void setupDockingMenu(Player player, Menu menu) {
		delivered = true;
		
		menu.setAuto(new DialogOption("Talk to Pilot", getDialog()));
		// Back and remove
		if(getInventory().size() > 0) {
			menu.add(new LootMenuOption("Collect Item" + (getInventory().size() == 1 ? "s" : ""), player.getInventory(), getInventory()));
		}
	}

	@Override
	public void drawNearby(float r) {
		drawShip(r, ShipModelType.DEFAULT);
	}
}  
