package vekta.menu.handle;

import vekta.Vekta;
import vekta.menu.Menu;
import vekta.menu.option.MenuOption;
import vekta.object.SpaceObject;

import static vekta.Vekta.getInstance;

/**
 * Menu renderer for a specific SpaceObject
 */
public class ObjectMenuHandle extends MenuHandle {
	private final SpaceObject target;

	public ObjectMenuHandle(MenuOption def, SpaceObject target) {
		super(def);

		this.target = target;
	}

	public void render(Menu menu) {
		super.render(menu);

		Vekta v = getInstance();
		v.textSize(64);
		v.fill(target.getColor());
		v.text(target.getName(), v.width / 2F, v.height / 4F);
	}
}
