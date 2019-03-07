package vekta.object.ship;

import processing.core.PVector;
import vekta.Player;
import vekta.RenderLevel;
import vekta.item.Inventory;
import vekta.item.Item;
import vekta.menu.Menu;
import vekta.module.RadiatorModule;
import vekta.object.CargoCrate;
import vekta.object.Projectile;
import vekta.object.SpaceObject;
import vekta.terrain.LandingSite;

import static processing.core.PConstants.CLOSE;
import static processing.core.PConstants.HALF_PI;
import static vekta.Vekta.*;

public abstract class Ship extends SpaceObject {
	private static final float CRATE_SPEED = 1;
	private static final float MAX_DOCKING_SPEED = 10;

	private final String name;
	protected final PVector heading;
	private final float speed;  // Force of the vector added when engine is on
	private final float turnSpeed; // Angular speed when turning

	private final Inventory inventory = new Inventory();

	private SpaceObject dock;

	public Ship(String name, PVector heading, PVector position, PVector velocity, int color, float speed, float turnSpeed) {
		super(position, velocity, color);
		this.name = name;
		this.heading = heading;
		this.speed = speed;
		this.turnSpeed = turnSpeed;

		setTemperature(RadiatorModule.TARGET_TEMP);
	}

	public Inventory getInventory() {
		return inventory;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public RenderLevel getRenderLevel() {
		return RenderLevel.AROUND_SHIP;
	}

	@Override
	public float getSpecificHeat() {
		return 1;
	}

	public PVector getHeading() {
		return heading.copy();
	}

	public void setHeading(PVector heading) {
		this.heading.set(heading).normalize();
	}

	public float getSpeed() {
		return speed;
	}

	public float getTurnSpeed() {
		return turnSpeed;
	}

	public boolean isDocked() {
		return getDock() != null;
	}

	public SpaceObject getDock() {
		return dock;
	}

	public void dock(SpaceObject s) {
		dock = s;
		doDock(s);
	}

	public void undock() {
		SpaceObject dock = getDock();
		if(dock != null) {
			PVector offset = getPosition().sub(dock.getPosition());
			setVelocity(dock.getVelocity().add(offset.copy().mult(.1F)));
			position.add(offset.setMag(getRadius() * 4 + dock.getRadius() - offset.mag()));
			this.dock = null;
			onDepart(dock);
		}
	}

	public void accelerate(float amount) {
		accelerate(amount, heading);
	}

	public void accelerate(float amount, PVector direction) {
		if(amount != 0) {
			addVelocity(direction.copy().setMag(amount * getSpeed() * getWorld().getTimeScale()));
		}
	}

	public void turn(float amount) {
		heading.rotate(amount * getTurnSpeed() * DEG_TO_RAD);
	}

	@Override
	public boolean collidesWith(RenderLevel level, SpaceObject s) {
		return !(s instanceof Projectile && ((Projectile)s).getParent() == this) && super.collidesWith(level, s);
	}

	@Override
	public void onCollide(SpaceObject s) {
		if(s instanceof CargoCrate) {
			// Add item to ship's inventory
			getInventory().add(((CargoCrate)s).getItem());
		}
		else if(s instanceof Ship) {
			Ship ship = (Ship)s;
			if(ship.getVelocity().sub(velocity).magSq() <= MAX_DOCKING_SPEED * MAX_DOCKING_SPEED) {
				// Board ship
				ship.dock(this);
			}
			else {
				destroyBecause(s);
			}
		}
	}

	@Override
	public void onDestroy(SpaceObject s) {
		for(Item item : getInventory()) {
			addObject(new CargoCrate(item, getPosition(), PVector.random2D().setMag(v.random(CRATE_SPEED))));
		}
		super.onDestroy(s);
	}

	@Override
	public void drawDistant(float r) {
		drawMarker();///////
	}

	protected void drawShip(float r, ShipModelType shape) {
		float theta = heading.heading() + HALF_PI;
		v.rotate(theta);
		v.beginShape();
		switch(shape) {
		case CARGO_SHIP:
			v.vertex(0, -r * 1.35F);
			v.vertex(-r / 2, -r);
			v.vertex(-r / 2, r);
			v.vertex(r / 2, r);
			v.vertex(r / 2, -r);
			break;
		case FIGHTER:
			v.vertex(0, -r * 2);
			// Draw left spike
			v.vertex(-r, r * 2);
			v.vertex(-r, -r / 3.0F);
			v.vertex(-r, r * 2);
			// Draw right spike
			v.vertex(r, r * 2);
			v.vertex(r, -r / 3.0F);
			v.vertex(r, r * 2);
			break;
		default:
			v.vertex(0, -r * 2);
			v.vertex(-r, r * 2);
			v.vertex(r, r * 2);
			break;
		}
		v.endShape(CLOSE);
	}

	// Named Ship::doLand() to distinguish from PlayerListener::onLand()
	public void doLand(LandingSite site) {
		site.takeoff();
	}

	// Named Ship::doDock() to distinguish from PlayerListener::onDock()
	public void doDock(SpaceObject obj) {
		undock();
	}

	public void setupDockingMenu(Player player, Menu menu) {
	}

	public void onDepart(SpaceObject obj) {
	}

	protected enum ShipModelType {
		DEFAULT,
		CARGO_SHIP,
		FIGHTER,
	}
}
