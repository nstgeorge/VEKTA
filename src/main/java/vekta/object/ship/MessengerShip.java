package vekta.object.ship;

import processing.core.PVector;
import vekta.Player;
import vekta.RenderLevel;
import vekta.menu.Menu;
import vekta.menu.option.DialogOption;
import vekta.menu.option.LootMenuOption;
import vekta.object.SpaceObject;
import vekta.person.Dialog;

import static vekta.Vekta.getWorld;

public class MessengerShip extends Ship {
	private static final float DEF_MASS = 1000;
	private static final float DEF_RADIUS = 5;
	private static final float DEF_SPEED = .2F;
	private static final float DEF_TURN = 4;
	private static final float APPROACH_FACTOR = .2F;

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
		return RenderLevel.PLANET; // Increase despawn radius compared to other ships
	}

	@Override
	public void onUpdate(RenderLevel level) {
		PVector offset = getTargetShip().getPosition().sub(getPosition());

		if(!delivered) {
			// Approach target
			setVelocity(getTargetShip().getVelocity().add(offset.mult(getSpeed() * APPROACH_FACTOR / getWorld().getTimeScale())));
		}
		else {
			// Fly away
			accelerate(-1, offset);
		}
	}

	@Override
	public boolean isDockable(SpaceObject s) {
		return !delivered && s == getTargetShip();
	}

	@Override
	public void setupDockingMenu(Player player, Menu menu) {
		delivered = true;

		menu.setAuto(new DialogOption("Talk to Pilot", getDialog(), menu.getDefault()));
		// Back and remove
		if(getInventory().itemCount() > 0) {
			menu.add(new LootMenuOption("Collect Item" + (getInventory().itemCount() == 1 ? "s" : ""), player.getInventory(), getInventory()));
		}
	}

	@Override
	public void drawNearby(float r) {
		drawShip(r, ShipModelType.DEFAULT);
	}
}  
