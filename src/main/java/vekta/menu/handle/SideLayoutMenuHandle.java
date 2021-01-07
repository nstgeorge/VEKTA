package vekta.menu.handle;

import vekta.display.Layout;
import vekta.display.VerticalLayout;
import vekta.menu.Menu;
import vekta.menu.option.LayoutAware;
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
	public int getItemWidth() {
		return v.width / 3;
	}

	@Override
	public int getItemX() {
		int x = v.width / 6 + getItemWidth() / 2;
		return leftSide ? x : v.width - x;
	}

	@Override
	public int getItemY(int i) {
		return super.getItemY(i) - 100;
	}

	@Override
	public void init() {
		super.init();
		getMenu().addSelectListener(opt -> updateLayout());
	}

	@Override
	public void focus() {
		super.focus();

		updateLayout();
	}

	@Override
	public void render() {
		super.render();

		float offset = leftSide ? 0 : getItemWidth() / 2F;

		float infoX = v.width - getItemX() - getItemWidth() / 4F - offset;
		float infoY = v.height / 4F;

		v.pushStyle();
		layout.draw(infoX, infoY, getItemWidth(), v.height);
		v.popStyle();
	}

	@Override
	public void onChange() {
		updateLayout();
	}

	private void updateLayout() {
		MenuOption cursor = getMenu().getCursor();
		layout.clear();
		layout.getStyle().color(cursor.getColor());
		if(cursor instanceof LayoutAware) {
			((LayoutAware)cursor).onLayout(layout);
		}
	}
}
