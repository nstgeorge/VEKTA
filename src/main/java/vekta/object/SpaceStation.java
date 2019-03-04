package vekta.object;

import processing.core.PVector;
import vekta.object.module.ComponentModule;
import vekta.object.module.ModuleType;

import java.util.HashMap;
import java.util.Map;

import static processing.core.PApplet.sqrt;
import static processing.core.PConstants.HALF_PI;
import static vekta.Vekta.v;

public class SpaceStation extends ModularShip {
	private static final float DEF_SPEED = .02F; // Base engine speed (much slower than player)
	private static final float DEF_TURN = 5; // Base turn speed (much slower than player)

	private static final float TILE_SIZE = 24;

	private final Component core;

	public SpaceStation(String name, ComponentModule coreModule, PVector heading, PVector position, PVector velocity, int color) {
		super(name, heading, position, velocity, color, DEF_SPEED, DEF_TURN);

		// Default modules
		addModule(coreModule);
		this.core = new Component(null, Direction.RIGHT, coreModule);
	}

	public Component getCore() {
		return core;
	}

	public boolean isModuleTypeExclusive(ModuleType type) {
		return false;
	}

	// TODO: compute
	@Override
	public float getMass() {
		return 1000 * getModules().size();
	}

	// TODO: compute
	@Override
	public float getRadius() {
		return TILE_SIZE * sqrt(getModules().size());
	}

	@Override
	public void draw() {
		v.stroke(getColor());
		v.noFill();
		v.pushMatrix();
		v.translate(position.x, position.y);
		v.rotate(heading.heading());
		core.draw();
		v.popMatrix();
	}

	public class Component {
		private final Component parent;
		private final Direction direction;
		private final ComponentModule module;
		private final Map<Direction, Component> attached = new HashMap<>();

		public Component(Component parent, Direction direction, ComponentModule module) {
			this.parent = parent;
			this.direction = direction;
			this.module = module;
		}

		public Component getParent() {
			return parent;
		}

		public Direction getDirection() {
			return direction;
		}

		public ComponentModule getModule() {
			return module;
		}

		public Component getAttached(Direction dir) {
			return attached.get(dir);
		}

		public boolean hasAttachmentPoint(Direction dir) {
			return !attached.containsKey(dir) && getModule().hasAttachmentPoint(dir.rotate(getDirection()));
		}

		public Component tryAttach(Direction dir, ComponentModule module) {
			if(hasAttachmentPoint(dir)) {
				addModule(module);
				Component prev = getAttached(dir);
				if(prev != null) {
					detach(prev);
				}
				Component component = new Component(this, Direction.RIGHT, module);
				attached.put(dir, component);
				return component;
			}
			return null;
		}

		public void detach(Component component) {
			for(Direction dir : Direction.values()) {
				if(getAttached(dir) == component) {
					removeModule(component.getModule());
					attached.remove(dir);
				}
			}
		}

		public void draw() {
			v.rotate(getDirection().getAngle());
			module.draw(TILE_SIZE);
			for(Direction dir : attached.keySet()) {
				v.pushMatrix();
				v.translate(dir.getX(getModule()) * TILE_SIZE, dir.getY(getModule()) * TILE_SIZE);
				v.rotate(dir.getAngle());
				getAttached(dir).draw();
				v.popMatrix();
			}
		}
	}

	public enum Direction {
		RIGHT,
		UP,
		LEFT,
		DOWN;

		public static Direction random() {
			return Direction.values()[(int)v.random(4)];
		}

		public int getX(ComponentModule module) {
			switch(this) {
			case RIGHT:
				return module.getWidth();
			case LEFT:
				return -module.getWidth();
			default:
				return 0;
			}
		}

		public int getY(ComponentModule module) {
			switch(this) {
			case UP:
				return module.getHeight();
			case DOWN:
				return -module.getHeight();
			default:
				return 0;
			}
		}

		public float getAngle() {
			return ordinal() * HALF_PI;
		}

		public Direction rotate(int d) {
			int len = Direction.values().length;
			return Direction.values()[(ordinal() + d + len) % len];
		}

		public Direction rotate(Direction dir) {
			return rotate(dir.ordinal());
		}

		public Direction left() {
			return rotate(-1);
		}

		public Direction right() {
			return rotate(1);
		}

		public Direction back() {
			return rotate(2);
		}
	}
}  
