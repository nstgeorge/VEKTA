package vekta.context;

import vekta.Resources;
import vekta.menu.Menu;
import vekta.menu.handle.LoadoutMenuHandle;
import vekta.menu.option.BackOption;
import vekta.menu.option.InstallModuleOption;
import vekta.object.ModularShip;
import vekta.object.SpaceStation;
import vekta.object.module.Module;
import vekta.object.module.Upgrader;
import vekta.object.module.station.ComponentModule;

import java.util.Collections;

import static vekta.Vekta.*;

/**
 * Context for modifying space stations.
 */
public class StationLayoutContext implements Context, Upgrader {
	private static final float STATION_SCALE = 3;

	private final Context parent;
	private final SpaceStation station;
	private final ModularShip ship;

	private SpaceStation.Component cursor;
	private SpaceStation.Direction placementDir;

	public StationLayoutContext(Context parent, SpaceStation station, ModularShip ship) {
		this.parent = parent;
		this.station = station;
		this.ship = ship;
	}

	@Override
	public void focus() {
	}

	@Override
	public void render() {
		v.clear();
		v.camera();
		v.noLights();
		v.hint(DISABLE_DEPTH_TEST);

		float tileSize = station.getTileSize();
		SpaceStation.Component core = station.getCore();
		if(cursor == null) {
			cursor = core;
		}

		// Set up station rendering
		v.pushMatrix();
		v.translate(v.width / 2F, v.height / 2F);
		v.scale(STATION_SCALE);

		// Draw station components
		v.noFill();
		v.stroke(UI_COLOR);
		station.drawRelative();

		// Highlight cursor component
		SpaceStation.Direction dir = cursor.getDirection();
		if(placementDir == null) {
			v.stroke(255);
			v.pushMatrix();
			v.translate(cursor.getX(), cursor.getY());
			v.rotate(dir.getAngle());
			cursor.getModule().draw(station.getTileSize());
			v.popMatrix();
		}
		else {
			v.stroke(v.color(200));
			float bx = cursor.getBorderX(placementDir);
			float by = cursor.getBorderY(placementDir);
			v.ellipse(
					cursor.getX() + bx + v.sign(bx) * station.getTileSize() / 2,
					cursor.getY() + by + v.sign(by) * station.getTileSize() / 2,
					tileSize / 3,
					tileSize / 3);
		}

		// End station rendering
		v.popMatrix();

		// Draw title
		v.textSize(48);
		v.textAlign(CENTER);
		v.fill(100);
		v.text(station.getName(), v.width / 2F, v.height / 4F);

		// Draw current module name
		v.textSize(32);
		v.fill(200);
		v.text(cursor.getModule().getName(), v.width / 2F, v.height * 3 / 4F);

		// Draw helper text
		v.textSize(24);
		v.fill(255);
		if(!cursor.hasChildren()) {
			v.text("X to " + (placementDir != null ? "INSTALL" : "REMOVE"), v.width / 2F, v.height * 3 / 4F + 100);
		}
	}

	public void moveCursor(SpaceStation.Direction dir) {
		SpaceStation.Component component = cursor.getAttached(dir);
		if(placementDir != null) {
			placementDir = null;
		}
		else if(component != null) {
			cursor = component;
		}
		else if(cursor.getModule().hasAttachmentPoint(dir)) {
			placementDir = dir;
		}
		else {
			return;
		}
		Resources.playSound("change");
	}

	@Override
	public void keyPressed(char key) {
		if(key == ESC) {
			setContext(parent);
		}
		else if(key == 'w') {
			moveCursor(SpaceStation.Direction.UP);
		}
		else if(key == 's') {
			moveCursor(SpaceStation.Direction.DOWN);
		}
		else if(key == 'a') {
			moveCursor(SpaceStation.Direction.LEFT);
		}
		else if(key == 'd') {
			moveCursor(SpaceStation.Direction.RIGHT);
		}
		else if(key == 'x') {
			if(isCursorRemovable()) {
				// Uninstall cursor
				uninstallModule(cursor.getModule());
			}
			else if(placementDir != null) {
				Menu menu = new Menu(new LoadoutMenuHandle(new BackOption(this), Collections.singletonList(cursor.getModule())));
				for(Module module : ship.findUpgrades()) {
					if(module instanceof ComponentModule && cursor.isReplaceable((ComponentModule)module)) {
						menu.add(new InstallModuleOption(this, module));
					}
				}
				//			if(isCursorRemovable()) {
				//				menu.add(new UninstallModuleOption(this, cursor.getModule()));
				//			}
				menu.addDefault();
				setContext(menu);
			}
			else {
				return;
			}
			Resources.playSound("select");
		}
	}

	@Override
	public void keyReleased(char key) {
	}

	@Override
	public void mouseWheel(int amount) {
	}

	@Override
	public Module getRelevantModule(Module module) {
		return placementDir != null ? null : cursor.getModule();
	}

	@Override
	public void installModule(Module module) {
		if(!(module instanceof ComponentModule)) {
			return;
		}

		ship.getInventory().moveTo(station.getInventory()); // Transfer ship items to station

		if(placementDir != null) {
			SpaceStation.Component component = cursor.attach(placementDir, (ComponentModule)module);
			if(component != null) {
				placementDir = null;
				cursor = component;
			}
		}
		else {
			cursor.replaceModule((ComponentModule)module);
		}

		station.getInventory().moveTo(ship.getInventory()); // Return items to ship

		setContext(this);
	}

	@Override
	public void uninstallModule(Module module) {
		if(cursor.getModule() != module) {
			return;
		}

		cursor.detach();
		cursor = cursor.getParent();
		station.getInventory().moveTo(ship.getInventory()); // Send items to ship

		setContext(this);
	}

	private boolean isCursorRemovable() {
		return placementDir == null && !cursor.hasChildren();
	}
}
