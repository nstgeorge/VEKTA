package vekta.menu.handle;

import vekta.menu.Menu;
import vekta.menu.option.MenuOption;
import vekta.object.SpaceObject;

import static vekta.Vekta.v;

/**
 * Menu renderer for a specific SpaceObject
 */
public class ObjectMenuHandle extends MenuHandle {
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

		v.textSize(64);
		v.fill(target.getColor());
		v.text(target.getName(), v.width / 2F, v.height / 4F);
	}
}
