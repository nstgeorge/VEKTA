package vekta.context;

import vekta.Resources;
import vekta.menu.Menu;
import vekta.menu.handle.LoadoutMenuHandle;
import vekta.menu.option.BackOption;
import vekta.menu.option.InstallModuleOption;
import vekta.module.Module;
import vekta.module.ModuleType;
import vekta.module.Upgrader;
import vekta.module.station.ComponentModule;
import vekta.object.ship.ModularShip;
import vekta.object.ship.SpaceStation;

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
	private final ComponentModule placementModule = new PlacementModule();

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
		v.stroke(isPlacing() ? 200 : 255);
		v.pushMatrix();
		v.translate(cursor.getX(), cursor.getY());
		v.rotate(dir.getAngle());
		cursor.getModule().draw(station.getTileSize());
		v.popMatrix();

		// End station rendering
		v.popMatrix();

		// Draw title
		v.textSize(48);
		v.textAlign(CENTER);
		v.fill(100);
		v.text(station.getName(), v.width / 2F, 200);

		// Draw current module name
		v.textSize(32);
		v.fill(200);
		v.text(cursor.getModule().getName(), v.width / 2F, v.height - 100);

		// Draw helper text
		v.textSize(24);
		v.fill(255);
		if(!cursor.hasChildren()) {
			v.text("X to " + (isPlacing() ? "INSTALL" : "REMOVE"), v.width / 2F, v.height - 32);
		}
	}

	public void moveCursor(SpaceStation.Direction dir) {
		SpaceStation.Component prevCursor = cursor;
		SpaceStation.Component component = cursor.getAttached(dir);
		//		if(isPlacing()) {
		//			cursor = cursor.getParent();
		//		}
		/*else */
		if(component != null) {
			cursor = component;
		}
		else {
			SpaceStation.Component hypothetical = station.new Component(cursor, dir, placementModule);
			if(!isPlacing() && cursor.isAttachable(hypothetical)) {
				cursor = hypothetical;
			}
			else {
				cursor = hypothetical.getNearest(dir);
			}
		}

		if(cursor != prevCursor) {
			Resources.playSound("change");
		}
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
			else if(isPlacing()) {
				Menu menu = new Menu(new LoadoutMenuHandle(new BackOption(this), Collections.singletonList(cursor.getModule())));
				for(Module module : ship.findUpgrades()) {
					if(module instanceof ComponentModule) {
						ComponentModule m = (ComponentModule)module;
						if(isPlacing() ? cursor.isAttachable(cursor) : cursor.isReplaceable(m)) {
							menu.add(new InstallModuleOption(this, m));
						}
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
		return isPlacing() ? null : cursor.getModule();
	}

	@Override
	public void installModule(Module module) {
		if(!(module instanceof ComponentModule)) {
			return;
		}

		ship.getInventory().moveTo(station.getInventory()); // Transfer ship items to station

		if(isPlacing()) {
			cursor = cursor.getParent().attach(cursor.getDirection(), (ComponentModule)module);
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

	private boolean isPlacing() {
		return cursor.getModule() == placementModule;
	}

	private boolean isCursorRemovable() {
		return !isPlacing() && !cursor.hasChildren();
	}

	private final class PlacementModule implements ComponentModule {
		@Override
		public int getWidth() {
			return 1;
		}

		@Override
		public int getHeight() {
			return 1;
		}

		@Override
		public void draw(float tileSize) {
			v.ellipse(0, 0, tileSize / 3, tileSize / 3);
		}

		@Override
		public boolean hasAttachmentPoint(SpaceStation.Direction direction) {
			return true;
		}

		@Override
		public String getName() {
			return "";
		}

		@Override
		public ModuleType getType() {
			return null;
		}

		@Override
		public boolean isBetter(Module other) {
			return false;
		}

		@Override
		public Module getVariant() {
			return null;
		}

		@Override
		public boolean isApplicable(ModularShip ship) {
			return false;
		}
	}
}
