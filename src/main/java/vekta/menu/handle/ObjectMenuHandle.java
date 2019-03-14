package vekta.menu.handle;

import vekta.RenderLevel;
import vekta.menu.Menu;
import vekta.menu.option.MenuOption;
import vekta.object.SpaceObject;

import static vekta.Vekta.v;

/**
 * Menu renderer for a specific SpaceObject
 */
public class ObjectMenuHandle extends MenuHandle {
	private static final float ROTATE_SPEED = 5e-3F;

	private final SpaceObject target;

	public ObjectMenuHandle(MenuOption def, SpaceObject target) {
		super(def);

		this.target = target;
	}

	public SpaceObject getSpaceObject() {
		return target;
	}

	@Override
	public void render(Menu menu) {
		super.render(menu);

		v.textSize(48);
		v.fill(target.getColor());
		v.text(target.getName(), v.width / 2F, getButtonY(-3));

		// Draw object preview
		v.pushMatrix();
		v.translate(getButtonX(), getButtonY(-2) + 40);
		v.rotate(v.frameCount * ROTATE_SPEED);
		v.noFill();
		v.stroke(target.getColor());
		target.draw(RenderLevel.PARTICLE, getSpacing() / 3F);
		v.popMatrix();
	}
}
