package vekta.object;

import processing.core.PVector;
import vekta.item.Inventory;
import vekta.menu.Menu;
import vekta.menu.handle.SpaceObjectMenuHandle;
import vekta.menu.option.ShipUndockButton;
import vekta.object.planet.Planet;
import vekta.object.ship.Damageable;
import vekta.object.ship.Damager;
import vekta.object.ship.ModularShip;
import vekta.player.Player;
import vekta.spawner.item.OreItemSpawner;
import vekta.world.RenderLevel;

import static processing.core.PConstants.TWO_PI;
import static vekta.Vekta.*;

public class RingDebris extends Planet implements Damageable {
	private static final float MAX_PART_DISTANCE = .5F;
	private static final float SPIN_SCALE = .01F;

	private final Inventory inventory = new Inventory();

	private float angle;
	private float spinSpeed = v.random(-1, 1) * SPIN_SCALE;

	private final float[][] positions = new float[(int)v.random(4) + 1][5];

	public RingDebris(String name, float mass, float density, PVector position, PVector velocity, int color) {
		super(name, mass, density, position, velocity, color);

		for(int i = 0; i < positions.length; i++) {
			PVector offset = PVector.random2D().mult(v.random(MAX_PART_DISTANCE));
			positions[i] = new float[] {
					offset.x,
					offset.y,
					v.random(.75F, 1.25F),
					v.random(.75F, 1.25F),
					v.random(TWO_PI),
			};
		}

		if(v.chance(.5F)) {
			getInventory().add(OreItemSpawner.randomOre(getName()));
		}
	}

	public Inventory getInventory() {
		return inventory;
	}

	@Override
	public RenderLevel getRenderLevel() {
		return RenderLevel.SHIP;
	}

	@Override
	public float getSpecificHeat() {
		return 1;
	}

	@Override
	public void onUpdate(RenderLevel level) {
		angle += spinSpeed;
	}

	@Override
	public void drawNearby(float r) {
		v.rotate(angle);

		for(float[] pos : positions) {
			v.rotate(pos[4]);
			v.ellipse(pos[0] * r, pos[1] * r, pos[2] * r, pos[3] * r);
		}
	}

	@Override
	public void drawDistant(float r) {
		v.point(0, 0);
	}

	@Override
	public void drawPreview(float r) {
		drawNearby(r);
	}

	@Override
	public void onCollide(SpaceObject s) {
		if(s instanceof ModularShip) {
			ModularShip ship = (ModularShip)s;
			if(ship.hasController() && ship.isDockable(this)) {
				ship.dock(this);
				Player player = ship.getController();
				Menu menu = new Menu(player, new ShipUndockButton(ship, getWorld()), new SpaceObjectMenuHandle(this));
				menu.addDefault();
				setContext(menu);
				applyContext();
			}
		}
		else {
			super.onCollide(s);
		}
	}

	@Override
	public boolean damage(float amount, Damager damager) {
		destroyBecause(damager.getParentObject());
		return true;
	}
}
