package vekta.object.ship;

import processing.core.PVector;
import vekta.faction.Faction;
import vekta.menu.Menu;
import vekta.menu.option.LootMenuButton;
import vekta.util.Counter;
import vekta.world.RenderLevel;

import static vekta.Vekta.getWorld;
import static vekta.Vekta.v;

public class CargoShip extends Ship {
	private static final float DEF_MASS = 1000;
	private static final float DEF_RADIUS = 15;
	private static final float DEF_SPEED = .01F;
	private static final float DEF_TURN = 1;

	private float thrust;
	private float turn;

	private final Counter steerCt = new Counter();

	private final Faction faction;

	public CargoShip(String name, PVector heading, PVector position, PVector velocity, Faction faction) {
		super(name, heading, position, velocity, faction.getColor(), DEF_SPEED, DEF_TURN);

		this.faction = faction;
	}

	public Faction getFaction() {
		return faction;
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
	public void onUpdateShip() {
		if(steerCt.cycle()) {
			steerCt.delay((int)v.random(10, 500));
			thrust = v.random(-1, 1);
			turn = v.random(-1, 1);
		}
		accelerate(thrust);
		turn(turn);
	}

	@Override
	public void setupDockingMenu(Menu menu) {
		menu.add(new LootMenuButton("Loot", getInventory()));

		for(EscortShip escort : getWorld().findObjects(EscortShip.class)) {
			if(escort.getTarget() == this) {
				escort.setTarget(menu.getPlayer().getShip());
			}
		}
	}

	@Override
	public void drawNearby(float r) {
		drawShip(r, ShipModelType.CARGO_SHIP);
	}
}  
