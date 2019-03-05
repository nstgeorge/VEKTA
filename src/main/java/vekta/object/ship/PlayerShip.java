package vekta.object.ship;

import processing.core.PVector;
import vekta.Resources;
import vekta.Vekta;
import vekta.menu.Menu;
import vekta.menu.handle.LandingMenuHandle;
import vekta.menu.handle.ObjectMenuHandle;
import vekta.menu.option.BackOption;
import vekta.menu.option.LoadoutMenuOption;
import vekta.menu.option.ShipUndockOption;
import vekta.menu.option.SurveyOption;
import vekta.module.*;
import vekta.object.CargoCrate;
import vekta.object.SpaceObject;
import vekta.overlay.singleplayer.Notification;
import vekta.terrain.LandingSite;

import java.util.List;

import static vekta.Vekta.*;

public class PlayerShip extends ModularShip {
	// Default PlayerShip stuff
	private static final float DEF_MASS = 5000;
	private static final float DEF_RADIUS = 5;
	private static final float DEF_SPEED = .1F; // Base speed (engine speed = 1)
	private static final float DEF_TURN = 20; // Base turn speed (RCS turnSpeed = 1)

	private final PVector influence = new PVector();

	public PlayerShip(String name, PVector heading, PVector position, PVector velocity, int color) {
		super(name, heading, position, velocity, color, DEF_SPEED, DEF_TURN);

		// Default modules
		addModule(new EngineModule(1));
		addModule(new RCSModule(1));
		addModule(new TargetingModule());
		addModule(new BatteryModule(100));
		addModule(new CannonModule());

		setEnergy(getMaxEnergy());
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
	public void draw() {
		v.pushMatrix();
		drawShip(ShipModelType.DEFAULT);
		v.popMatrix();

		// Draw influence vector
		v.stroke(255, 0, 0);
		v.line(0, 0, (influence.x * 100), (influence.y * 100));
	}

	@Override
	public void onUpdate() {
		for(Module module : getModules()) {
			module.onUpdate();
		}
	}

	public void onKeyPress(char key) {
		super.onKeyPress(key);

		if(key == 'e') {
			openMenu();
		}
	}

	public void onKeyRelease(char key) {
		for(Module module : getModules()) {
			module.onKeyRelease(key);
		}
	}

	public void openMenu() {
		Menu menu = new Menu(new ObjectMenuHandle(new BackOption(getWorld()), this));
		menu.add(new LoadoutMenuOption(this));
		menu.addDefault();
		setContext(menu);
	}

	@Override
	public void onLand(LandingSite site) {
		Menu menu = new Menu(new LandingMenuHandle(site, getWorld()));
		for(Module m : getModules()) {
			m.onLandingMenu(site, menu);
		}
		site.getTerrain().setupLandingMenu(this, menu);
		menu.add(new SurveyOption(site));
		menu.addDefault();
		Resources.stopSound("engine");
		Resources.playSound("land");
		Vekta.setContext(menu);
	}

	@Override
	public void onDock(SpaceObject s) {
		Resources.stopSound("engine");
		if(s instanceof Ship) {
			Menu menu = new Menu(new ObjectMenuHandle(new ShipUndockOption(this, getWorld()), s));
			((Ship)s).setupDockingMenu(this, menu);
			menu.addDefault();
			setContext(menu);
		}
	}

	@Override
	public void onDepart(SpaceObject obj) {
		setThrustControl(0);
		setTurnControl(0);
	}

	@Override
	public void onCollide(SpaceObject s) {
		super.onCollide(s);

		if(s instanceof CargoCrate) {
			addObject(new Notification("Picked up: " + ((CargoCrate)s).getItem().getName()));
		}
	}

	@Override
	public void onDestroy(SpaceObject s) {
		getWorld().setDead();
	}

	@Override
	public PVector applyInfluenceVector(List<SpaceObject> objects) {
		this.influence.set(super.applyInfluenceVector(objects));
		return this.influence;
	}
}  