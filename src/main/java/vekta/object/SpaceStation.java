package vekta.object;

import processing.core.PVector;
import vekta.object.module.ComponentModule;
import vekta.object.module.ModuleType;

import java.util.*;

import static processing.core.PApplet.sqrt;
import static processing.core.PConstants.HALF_PI;
import static vekta.Vekta.v;

public class SpaceStation extends ModularShip {
	private static final float DEF_SPEED = .02F; // Base engine speed (much slower than player)
	private static final float DEF_TURN = 5; // Base turn speed (much slower than player)

	private static final float TILE_SIZE = 16;

	private final Component core;

	public SpaceStation(String name, ComponentModule coreModule, PVector heading, PVector position, PVector velocity, int color) {
		super(name, heading, position, velocity, color, DEF_SPEED, DEF_TURN);

		// Default modules
		addModule(coreModule);
		this.core = new Component(null, Direction.RIGHT, coreModule);
	}

	public float getTileSize() {
		return TILE_SIZE;
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

	public final class Component {
		private final Component parent;
		private Direction rotation;
		private ComponentModule module;

		private final Direction tileDirection;
		private final int tileX, tileY;

		private final Map<Direction, Component> attached = new HashMap<>();

		public Component(Component parent, Direction dir, ComponentModule module) {
			this.parent = parent;
			this.rotation = Direction.RIGHT;
			this.module = module;

			if(parent == null) {
				tileDirection = dir;
				tileX = 0;
				tileY = 0;
			}
			else {
				attached.put(rotation.back(), parent);

				tileDirection = parent.getTileDirection().rotate(dir);
				tileX = parent.getTileX() + getTileDirection().getX(parent.getModule());
				tileY = parent.getTileY() + getTileDirection().getY(parent.getModule());
			}
		}

		public Component getParent() {
			return parent;
		}

		public Direction getRotation() {
			return rotation;
		}

		public void setRotation(Direction rotation) {
			attached.put(rotation.back(), attached.remove(this.rotation.back()));
			this.rotation = rotation;
		}

		public ComponentModule getModule() {
			return module;
		}

		public boolean isReplaceable(ComponentModule module) {
			for(Direction dir : attached.keySet()) {
				if(!module.hasAttachmentPoint(dir)) {
					return false;
				}
			}
			return true;
		}

		public void replaceModule(ComponentModule module) {
			removeModule(getModule());
			addModule(module);
			this.module = module;
		}

		public Direction getTileDirection() {
			return tileDirection;
		}

		public int getTileX() {
			return tileX;
		}

		public int getTileY() {
			return tileY;
		}

		public boolean hasChildren() {
			return getAttached().size() > 1;
		}

		public Collection<Component> getAttached() {
			return attached.values();
		}

		public Component getAttached(Direction dir) {
			return attached.get(dir);
		}

		public boolean hasAttachmentPoint(Direction dir) {
			return !attached.containsKey(dir) && getModule().hasAttachmentPoint(dir.rotate(getRotation()));
		}

		public Collection<Direction> getAttachableDirections() {
			Set<Direction> set = new HashSet<>();
			for(Direction dir : Direction.values()) {
				if(hasAttachmentPoint(dir)) {
					set.add(dir);
				}
			}
			return set;
		}

		public Component tryAttach(Direction dir, ComponentModule module) {
			if(hasAttachmentPoint(dir)) {
				addModule(module);
				Component prev = getAttached(dir);
				if(prev != null) {
					detach(prev);
				}
				Component component = new Component(this, dir, module);
				attached.put(dir, component);
				return component;
			}
			return null;
		}

		public void detach(Component component) {
			// TODO detach recursively
			for(Direction dir : Direction.values()) {
				if(getAttached(dir) == component) {
					removeModule(component.getModule());
					attached.remove(dir);
				}
			}
		}

		public void draw() {
			v.rotate(getRotation().getAngle());
			module.draw(TILE_SIZE);
			for(Direction dir : attached.keySet()) {
				Component next = getAttached(dir);
				if(next != getParent()) {
					v.pushMatrix();
					v.translate(dir.getX(getModule()) * TILE_SIZE, dir.getY(getModule()) * TILE_SIZE);
					v.rotate(dir.getAngle());
					next.draw();
					v.popMatrix();
				}
			}
		}
	}

	/**
	 * Cardinal directions (ordinals are counter-clockwise: +x, -y, -x, +y)
	 */
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
			case DOWN:
				return module.getHeight();
			case UP:
				return -module.getHeight();
			default:
				return 0;
			}
		}

		public float getAngle() {
			return -ordinal() * HALF_PI; // Negative sign accounts for upside-down world coordinates
		}

		public Direction rotate(int d) {
			int len = Direction.values().length;
			return Direction.values()[(ordinal() + d + len) % len];
		}

		public Direction rotate(Direction dir) {
			return rotate(dir.ordinal());
		}

		public Direction relativeTo(Direction dir) {
			return rotate(-dir.ordinal());
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
