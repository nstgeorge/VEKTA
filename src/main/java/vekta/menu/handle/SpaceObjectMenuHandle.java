package vekta.menu.handle;

import vekta.KeyBinding;
import vekta.menu.Menu;
import vekta.object.SpaceObject;

import static vekta.Vekta.v;

/**
 * Menu renderer for a specific SpaceObject
 */
public class SpaceObjectMenuHandle extends MenuHandle {
	private final SpaceObject target;

	public SpaceObjectMenuHandle(SpaceObject target) {
		this.target = target;
	}

	public SpaceObject getSpaceObject() {
		return target;
	}

	@Override
	public KeyBinding getShortcutKey() {
		return KeyBinding.SHIP_MENU;
	}

	@Override
	public void render(Menu menu) {
		super.render(menu);

		v.textSize(48);
		v.fill(target.getColor());
		v.text(target.getName(), v.width / 2F, getItemY(-3));

		// Draw object preview
		v.pushMatrix();
		v.translate(getItemX(), getItemY(-2) + 40);
		v.noFill();
		v.stroke(target.getColor());
		target.drawPreview(getSpacing() / 2F);
		v.popMatrix();
	}
}
