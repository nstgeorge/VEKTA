package vekta.menu;// These will move into their own files once we migrate to Maven

import vekta.Resources;
import vekta.context.Context;
import vekta.menu.handle.MenuHandle;
import vekta.menu.option.MenuOption;

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

	public void clear() {
		items.clear();
	}

	public void add(MenuOption item) {
		items.add(item);
	}

	public void addDefault() {
		add(handle.getDefault());
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
	public void focus() {
		handle.focus(this);
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

	public void close() {
		if(handle.getDefault() != null) {
			handle.getDefault().select(this);
		}
	}
}
