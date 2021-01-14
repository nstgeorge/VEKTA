package vekta.menu.handle;

import processing.core.PShape;
import processing.core.PVector;
import vekta.Resources;
import vekta.context.Hyperspace;

import static processing.core.PConstants.CENTER;
import static vekta.Vekta.v;

public class MainMenuHandle extends MenuHandle {
	public static final Hyperspace HYPERSPACE = new Hyperspace(
			new PVector(v.width / 2F, v.height / 2F - 100),
			0.1F,
			170);

	private static final PShape LOGO = Resources.getShape("vekta_wordmark");

	@Override
	public void focus() {
		super.focus();

		Resources.setMusic("intro_and_menu", false);
	}

	@Override
	public void beforeDraw() {
		super.beforeDraw();

		HYPERSPACE.render();
	}

	@Override
	public void render() {
		super.render();

		v.shapeMode(CENTER);
		v.shape(LOGO, v.width / 2F, v.height / 4F, 646.15F, 100);

		v.textSize(14);
		v.text("Created by Nate St. George & Ryan Vandersmith", v.width / 2F, (v.height / 2F) + 100 * (getMenu().size() + 1));
	}
}
