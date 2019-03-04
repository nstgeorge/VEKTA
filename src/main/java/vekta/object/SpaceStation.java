package vekta.object;

import processing.core.PVector;
import vekta.object.module.ModuleType;
import vekta.object.module.station.ComponentModule;

import java.awt.*;
import java.util.List;
import java.util.*;

import static processing.core.PApplet.*;
import static vekta.Vekta.distSq;
import static vekta.Vekta.v;

public class SpaceStation extends ModularShip {
	private static final float DEF_SPEED = .02F; // Base engine speed (much slower than player)
	private static final float DEF_TURN = 5; // Base turn speed (much slower than player)

	private static final float TILE_SIZE = 16;

	private final Component core;

	private final List<Component> components = new ArrayList<>();

	public SpaceStation(String name, ComponentModule coreModule, PVector heading, PVector position, PVector velocity, int color) {
		super(name, heading, position, velocity, color, DEF_SPEED, DEF_TURN);

		this.core = new Component(null, Direction.RIGHT, coreModule);
		addComponent(core);
	}

	public void addComponent(Component component) {
		addModule(component.getModule());
		components.add(component);
	}

	public void removeComponent(Component component) {
		removeModule(component.getModule());
		components.remove(component);
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
		drawRelative();
		v.popMatrix();
	}

	public void drawRelative() {
		for(Component component : components) {
			v.pushMatrix();
			v.translate(component.getX(), component.getY());
			v.rotate(component.getDirection()/*.rotate(component.getRotation())*/.getAngle());
			component.getModule().draw(TILE_SIZE);
			v.popMatrix();

			//			// DEBUG: draw bounding boxes
			//			v.stroke(100);
			//			Rectangle b = component.getBounds();
			//			v.rectMode(CORNER);
			//			v.rect(b.x, b.y, b.width, b.height);
			//			v.rectMode(CENTER);
			//			v.stroke(getColor());
		}
	}

	public final class Component {
		private final Component parent;
		private final Direction direction;

		private ComponentModule module;
		//		private Direction rotation;
		private float x, y;

		private final Map<Direction, Component> attached = new HashMap<>();

		public Component(Component parent, Direction dir, ComponentModule module) {
			this.parent = parent;
			//			this.rotation = Direction.RIGHT;
			this.module = module;
			this.direction = dir;

			if(parent == null) {
				x = 0;
				y = 0;
			}
			else {
				x = parent.getX() + getOffsetX();
				y = parent.getY() + getOffsetY();

				attached.put(dir.back(), parent);
			}
		}

		public Component getParent() {
			return parent;
		}

		//		public Direction getRotation() {
		//			return rotation;
		//		}
		//
		//		public void setRotation(Direction rotation) {
		//			attached.put(rotation.back(), attached.remove(this.rotation.back()));
		//			this.rotation = rotation;
		//			// TODO update x, y, direction
		//		}

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

		public Direction getDirection() {
			return direction;
		}

		public float getX() {
			return x;
		}

		public float getY() {
			return y;
		}

		public float getOffsetX() {
			if(parent == null) {
				return 0;
			}
			return parent.getBorderX(getDirection()) - getBorderX(getDirection().back());
		}

		public float getOffsetY() {
			if(parent == null) {
				return 0;
			}
			return parent.getBorderY(getDirection()) - getBorderY(getDirection().back());
		}

		public Component getNearest(Direction dir) {
			return getNearest(dir.getX(getTileSize()), dir.getY(getTileSize()));
		}

		public Component getNearest(float offsetX, float offsetY) {
			PVector point = new PVector(getX() + offsetX, getY() + offsetY);
			Component nearest = this;
			float minSq = Float.POSITIVE_INFINITY;
			for(Component other : components) {
				if(other != this){
					float distSq = distSq(point, new PVector(other.getX(), other.getY()));
					if(distSq < minSq) {
						minSq = distSq;
						nearest = other;
					}
					
				}
			}
			return nearest;
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
			return !attached.containsKey(dir) && getModule().hasAttachmentPoint(dir/*.rotate(getRotation())*/.relativeTo(getDirection()));
		}

		public boolean isAttachable(Component hypothetical) {
			if(!hasAttachmentPoint(hypothetical.getDirection())) {
				return false;
			}
			for(Component component : components) {
				if(component.collidesWith(hypothetical)) {
					return false;
				}
			}
			return true;
		}

		public Collection<Direction> getVacantDirections() {
			Set<Direction> set = new HashSet<>();
			for(Direction dir : Direction.values()) {
				if(hasAttachmentPoint(dir)) {
					set.add(dir);
				}
			}
			return set;
		}

		public Component attach(Direction dir, ComponentModule module) {
			Component prev = getAttached(dir);
			if(prev != null) {
				prev.detach();
			}
			Component component = new Component(this, dir, module);
			attached.put(dir, component);
			addComponent(component);
			return component;
		}

		public void detach() {
			for(Direction dir : new ArrayList<>(attached.keySet())) {
				Component component = attached.get(dir);
				if(component != parent) {
					component.detach();
				}
				else {
					parent.attached.remove(dir.back());
				}
				attached.remove(dir);
			}
			removeComponent(this);
		}

		public float getBorderX(Direction dir) {
			float dist = abs(getDirection().getX(module.getWidth(), module.getHeight()));
			return dir.getX(dist) * getTileSize() / 2F;
		}

		public float getBorderY(Direction dir) {
			float dist = abs(getDirection().getY(module.getWidth(), module.getHeight()));
			return dir.getY(dist) * getTileSize() / 2F;
		}

		public boolean collidesWith(Component other) {
			return getBounds().intersects(other.getBounds());
		}

		private Rectangle getBounds() {
			float width = getBorderX(Direction.RIGHT) - getBorderX(Direction.LEFT);
			float height = getBorderY(Direction.DOWN) - getBorderY(Direction.UP);
			return new Rectangle(
					(int)(x - width / 2 + 1),
					(int)(y - height / 2 + 1),
					(int)(width - 2),
					(int)(height - 2));
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

		public float getAngle() {
			return -ordinal() * HALF_PI; // Negative sign accounts for upside-down world coordinates
		}

		public float getX(float dist) {
			return getX(dist, 0);
		}

		public float getY(float dist) {
			return getY(dist, 0);
		}

		public float getX(float x, float y) {
			switch(this) {
			case RIGHT:
				return +x;
			case UP:
				return -y;
			case LEFT:
				return -x;
			case DOWN:
				return +y;
			default:
				return 0;
			}
		}

		public float getY(float x, float y) {
			return left().getX(x, y);
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

		public Direction inverse() {
			return RIGHT.relativeTo(this);
		}

		public Direction left() {
			return rotate(1);
		}

		public Direction right() {
			return rotate(-1);
		}

		public Direction back() {
			return rotate(2);
		}
	}
}  
