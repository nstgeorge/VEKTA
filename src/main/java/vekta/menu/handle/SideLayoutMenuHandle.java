package vekta.menu.handle;

import vekta.display.Layout;
import vekta.display.VerticalLayout;
import vekta.menu.Menu;
import vekta.menu.option.LayoutBuilder;
import vekta.menu.option.MenuOption;

import static vekta.Vekta.v;

/**
 * Left/right side information menu renderer
 */
public class SideLayoutMenuHandle extends MenuHandle {
	private boolean leftSide;

	private final Layout layout = new VerticalLayout();

	public SideLayoutMenuHandle(boolean leftSide) {
		this.leftSide = leftSide;
	}

	public boolean isLeftSide() {
		return leftSide;
	}

	@Override
	public int getSpacing() {
		return 70;
	}

	@Override
	public int getButtonWidth() {
		return v.width / 3;
	}

	@Override
	public int getButtonX() {
		int x = v.width / 6 + getButtonWidth() / 2;
		return leftSide ? x : v.width - x;
	}

	@Override
	public int getButtonY(int i) {
		return super.getButtonY(i) - 100;
	}

	@Override
	public void focus(Menu menu) {
		super.focus(menu);

		updateLayout(menu);
	}

	@Override
	public void render(Menu menu) {
		super.render(menu);

		float offset = leftSide ? 0 : getButtonWidth() / 2F;

		float infoX = v.width - getButtonX() - getButtonWidth() / 4F - offset;
		float infoY = v.height / 4F;

		v.pushStyle();
		layout.draw(infoX, infoY, getButtonWidth() + offset, v.height);
		v.popStyle();
	}

	@Override
	public void onChange(Menu menu) {
		updateLayout(menu);
	}

	private void updateLayout(Menu menu) {
		MenuOption cursor = menu.getCursor();
		layout.clear();
		layout.getStyle().color(cursor.getColor());
		if(cursor instanceof LayoutBuilder) {
			((LayoutBuilder)cursor).onLayout(layout);
		}
	}
}
