package vekta.menu.handle;

import vekta.Vekta;
import vekta.context.World;
import vekta.menu.Menu;
import vekta.menu.option.BackOption;
import vekta.object.SpaceObject;

import static vekta.Vekta.getInstance;

/**
 * Menu renderer for a specific SpaceObject
 */
public class ObjectMenuHandle extends MenuHandle {
	private final SpaceObject target;

	public ObjectMenuHandle(SpaceObject target, World world) {
		super(new BackOption(world));

		this.target = target;
	}

	public void render(Menu menu) {
		super.render(menu);

		Vekta v = getInstance();
		v.textSize(48);
		v.fill(target.getColor());
		v.text(target.getName(), v.width / 2F, v.height / 4F);
	}
}
