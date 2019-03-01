package vekta.menu;// These will move into their own files once we migrate to Maven

import vekta.context.Context;
import vekta.Resources;
import vekta.menu.handle.MenuHandle;

import java.util.ArrayList;
import java.util.List;

public class Menu implements Context {
	private final MenuHandle handle;

	private final List<MenuOption> items = new ArrayList<>();

	private int index = 0;

	public Menu(MenuHandle handle) {
		this.handle = handle;
	}

	public MenuOption getCursor() {
		return items.get(index);
	}

	public int getIndex() {
		return index;
	}

	public int size() {
		return items.size();
	}

	public MenuOption get(int i) {
		return items.get(i);
	}

	public void add(MenuOption item) {
		items.add(item);
	}

	public boolean remove(MenuOption item) {
		return items.remove(item);
	}

	public void scroll(int n) {
		Resources.stopSound("change");
		index += n;
		int len = items.size();
		while(index < 0) {
			index += len;
		}
		index %= len;
	}

	@Override
	public void render() {
		handle.render(this);
	}

	@Override
	public void keyPressed(char key) {
		handle.keyPressed(this, key);
	}

	@Override
	public void keyReleased(char key) {
	}

	@Override
	public void mouseWheel(int amount) {
		scroll(amount);
	}
}
