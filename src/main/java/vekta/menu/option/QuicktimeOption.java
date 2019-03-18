package vekta.menu.option;

import vekta.menu.Menu;

import static vekta.Vekta.DANGER_COLOR;
import static vekta.Vekta.v;

public class QuicktimeOption implements MenuOption {
	private float time;
	private final String name;
	private final CustomOption.MenuAction action;

	public QuicktimeOption(float time, MenuOption option) {
		this(time, option.getName(), option::onSelect);
	}

	public QuicktimeOption(float time, String name, CustomOption.MenuAction action) {
		this.time = time;
		this.name = name;
		this.action = action;
	}

	public float getTime() {
		return time;
	}

	@Override
	public String getName() {
		return name + " [" + (int)(time) + ":" + (int)(time * 60) + "]";
	}

	@Override
	public int getColor() {
		return DANGER_COLOR;
	}

	@Override
	public int getBorderColor() {
		return getColor();
	}

	@Override
	public boolean isEnabled() {
		return time > 0;
	}

	@Override
	public void onUpdate(Menu menu) {
		if(time > 0) {
			time -= 1 / v.frameRate;
		}
		else if(time < 0) {
			time = 0;
			menu.remove(this);
		}
	}

	@Override
	public void onSelect(Menu menu) {
		menu.remove(this);
		action.select(menu);
	}
}
