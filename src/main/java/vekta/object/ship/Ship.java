package vekta.object.ship;

import processing.core.PVector;
import vekta.Player;
import vekta.Renameable;
import vekta.RenderLevel;
import vekta.item.Inventory;
import vekta.item.InventoryListener;
import vekta.item.Item;
import vekta.menu.Menu;
import vekta.module.RadiatorModule;
import vekta.object.CargoCrate;
import vekta.object.SpaceObject;
import vekta.terrain.LandingSite;

import static processing.core.PConstants.CLOSE;
import static processing.core.PConstants.HALF_PI;
import static vekta.Vekta.*;

public abstract class Ship extends SpaceObject implements Renameable, InventoryListener {
	private static final float CRATE_SPEED = 1;
	private static final float DEPART_FRAMES = 100; // Number of frames to wait before docking/landing again

	private String name;
	private final float speed;  // Force of the vector added when engine is on
	private final float turnSpeed; // Rotational speed when turning

	protected final PVector heading = new PVector();
	private final Inventory inventory = new Inventory(this);

	private SpaceObject dock;
	private int departTime; // Dock/land frame

	public Ship(String name, PVector heading, PVector position, PVector velocity, int color, float speed, float turnSpeed) {
		super(position, velocity, color);

		this.name = name;
		this.heading.set(heading);
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

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public RenderLevel getRenderLevel() {
		return RenderLevel.SHIP;
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

	public boolean isDockable(SpaceObject s) {
		return v.frameCount - departTime >= DEPART_FRAMES;
	}

	public void dock(SpaceObject s) {
		dock = s;
		doDock(s);
	}

	public void undock() {
		SpaceObject dock = getDock();
		if(dock != null) {
			PVector offset = getPosition().sub(dock.getPosition());
			setVelocity(dock.getVelocity().add(offset.mult(.05F)));
			position.add(offset.setMag(getRadius() * 2 + dock.getRadius()));
			this.dock = null;
			onDepart(dock);
		}
		departTime = v.frameCount; // Calls to undock() start a docking/landing debounce
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
	public void onCollide(SpaceObject s) {
		if(s instanceof CargoCrate) {
			// Add item_common.txt to ship's inventory
			getInventory().add(((CargoCrate)s).getItem());
		}
		else if(s instanceof Ship && isDockable(s) && ((Ship)s).isDockable(this)) {
			// Board ship
			dock(s);
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

	public abstract void setupDockingMenu(Player player, Menu menu);

	public void onDepart(SpaceObject obj) {
	}

	protected enum ShipModelType {
		DEFAULT,
		CARGO_SHIP,
		FIGHTER,
	}
}
