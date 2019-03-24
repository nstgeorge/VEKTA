package vekta.context;

import vekta.*;
import vekta.item.Inventory;
import vekta.menu.Menu;
import vekta.menu.handle.LoadoutMenuHandle;
import vekta.menu.option.BackOption;
import vekta.menu.option.InstallModuleOption;
import vekta.module.Module;
import vekta.module.ModuleType;
import vekta.module.ModuleUpgrader;
import vekta.module.station.ComponentModule;
import vekta.module.station.StationCoreModule;
import vekta.object.ship.ModularShip;
import vekta.object.ship.SpaceStation;

import java.util.Collections;

import static vekta.Vekta.*;

/**
 * Context for modifying space stations.
 */
public class StationLayoutContext implements Context, ModuleUpgrader {
	private static final float STATION_SCALE = 3;

	private final Context parent;
	private final SpaceStation station;
	private final Player player;

	private SpaceStation.Component cursor;
	private final ComponentModule placementModule = new PlacementModule();

	public StationLayoutContext(Context parent, SpaceStation station, Player player) {
		this.parent = parent;
		this.station = station;
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

	public Inventory getPlayerInventory() {
		return player.getInventory();
	}

	@Override
	public void focus() {
	}

	@Override
	public void render() {
		v.clear();
		//		v.camera();
		//		v.noLights();
		//		v.hint(DISABLE_DEPTH_TEST);
		v.strokeWeight(.5F);

		RenderLevel level = RenderLevel.PARTICLE;

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
		station.drawRelative(level, station.getRadius());

		// Highlight cursor component
		SpaceStation.Direction dir = cursor.getDirection();
		v.stroke(isPlacing() ? 200 : 255);
		v.pushMatrix();
		v.translate(cursor.getX(), cursor.getY());
		v.rotate(dir.getAngle());
		cursor.getModule().draw(level, station.getTileSize());
		v.popMatrix();

		// End station rendering
		v.popMatrix();

		v.textAlign(CENTER);

		// Draw title
		v.textSize(48);
		v.fill(station.getColor());
		v.text(station.getName(), v.width / 2F, 100);

		StationCoreModule module = station.getCoreModule();

		// Draw module statistics
		v.textSize(24);
		v.fill(100);
		v.text("Core Level: " + module.getTier(), v.width / 2F, 200);
		v.text("Modules: " + station.getModules().size() + " / " + module.getPartLimit(), v.width / 2F, 250);
		v.text("Modules/Type: " + module.getPartLimitPerType(), v.width / 2F, 300);

		// Draw current module name
		v.textSize(32);
		v.fill(200);
		v.text(cursor.getModule().getName(), v.width / 2F, v.height - 100);

		// Draw helper text
		v.textSize(24);
		v.fill(255);
		if(!cursor.hasChildren()) {
			v.text(Settings.getKeyText(KeyBinding.MENU_SELECT) + " to " + (isPlacing() ? "INSTALL" : "REMOVE"), v.width / 2F, v.height - 32);
		}
		v.strokeWeight(1);
	}

	public void moveCursor(SpaceStation.Direction dir) {
		SpaceStation.Component prevCursor = cursor;
		SpaceStation.Component component = cursor.getAttached(dir);
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

	public void selectCursor() {
		if(isCursorRemovable()) {
			// Uninstall cursor
			uninstallModule(cursor.getModule());
		}
		else if(isPlacing()) {
			Menu menu = new Menu(getPlayer(), new BackOption(this), new LoadoutMenuHandle(Collections.singletonList(cursor.getModule())));
			for(Module module : getPlayer().getShip().findUpgrades()) {
				if(module instanceof ComponentModule) {
					ComponentModule m = (ComponentModule)module;
					if((isPlacing() ? cursor.isAttachable(cursor) : cursor.isReplaceable(m)) && station.canEquip(m)) {
						menu.add(new InstallModuleOption(this, m));
					}
				}
			}
			menu.addDefault();
			setContext(menu);
		}
		else {
			return;
		}
		Resources.playSound("select");
	}

	@Override
	public void keyPressed(KeyBinding key) {
		switch(key) {
		case MENU_CLOSE:
			setContext(parent);
			break;
		case MENU_UP:
			moveCursor(SpaceStation.Direction.UP);
			break;
		case MENU_DOWN:
			moveCursor(SpaceStation.Direction.DOWN);
			break;
		case MENU_LEFT:
			moveCursor(SpaceStation.Direction.LEFT);
			break;
		case MENU_RIGHT:
			moveCursor(SpaceStation.Direction.RIGHT);
			break;
		case MENU_SELECT:
			selectCursor();
			break;
		}
	}

	@Override
	public void keyReleased(KeyBinding key) {
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

		getPlayerInventory().moveTo(station.getInventory()); // Transfer ship items to station

		if(isPlacing()) {
			cursor = cursor.getParent().attach(cursor.getDirection(), (ComponentModule)module);
		}
		else {
			cursor.replaceModule((ComponentModule)module);
		}

		station.getInventory().moveTo(getPlayerInventory()); // Return items to ship

		setContext(this);
	}

	@Override
	public void uninstallModule(Module module) {
		if(cursor.getModule() != module) {
			return;
		}

		cursor.detach();
		cursor = cursor.getParent();
		station.getInventory().moveTo(getPlayerInventory()); // Send items to ship

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
		public int getMass() {
			return 0;
		}

		@Override
		public void draw(RenderLevel dist, float tileSize) {
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
