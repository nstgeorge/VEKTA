package vekta.menu;

import vekta.KeyBinding;
import vekta.Player;
import vekta.PlayerEvent;
import vekta.context.Context;
import vekta.menu.handle.MenuHandle;
import vekta.menu.option.BackOption;
import vekta.menu.option.MenuOption;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Menu implements Context {
	private final Player player;
	private final MenuHandle handle;

	private MenuOption defaultOption;
	private final List<MenuOption> options = new ArrayList<>();
	private final Map<KeyBinding, MenuOption> hotkeys = new HashMap<>();
	private final List<MenuListener> listeners = new ArrayList<>();

	private boolean hasAutoOption;
	private MenuOption autoOption;

	private int index;

	public Menu(Menu parent, MenuHandle handle) {
		this(parent.getPlayer(), new BackOption(parent), handle);
	}

	public Menu(Player player, MenuOption def, MenuHandle handle) {
		this.player = player;
		this.handle = handle;
		this.defaultOption = def;

		handle.init(this);

		if(getPlayer() != null) {
			getPlayer().emit(PlayerEvent.MENU, this);
		}
	}

	public MenuHandle getHandle() {
		return handle;
	}

	public Player getPlayer() {
		return player;
	}

	public MenuOption getCursor() {
		return get(getIndex());
	}

	public List<MenuOption> getOptions() {
		return options;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int n) {
		index = n;
		MenuOption cursor = getCursor();
		for(MenuListener listener : listeners) {
			listener.onHover(cursor);
		}
	}

	public int size() {
		return options.size();
	}

	public MenuOption get(int i) {
		if(i >= size()) {
			i = size() - 1;
		}
		return options.get(i);
	}

	public MenuOption getDefault() {
		return defaultOption;
	}

	public void setDefault(MenuOption defaultOption) {
		this.defaultOption = defaultOption;
	}

	public void clear() {
		options.clear();
	}

	public void add(MenuOption item) {
		options.add(item);
	}

	public void add(int index, MenuOption item) {
		options.add(index, item);
	}

	public void addDefault() {
		add(defaultOption);
	}

	public boolean remove(MenuOption item) {
		if(options.remove(item)) {
			if(size() == 0) {
				close();
			}
			else if(index >= size()) {
				index = size() - 1;
			}
			return true;
		}
		return false;
	}

	public void scroll(int n) {
		int next = max(0, min(size() - 1, index + n));
		if(index != next) {
			setIndex(next);
		}
	}

	public void setAuto(MenuOption option) {
		// Disambiguate multiple automatic options by adding both to the menu
		if(autoOption != null) {
			add(autoOption);
			this.autoOption = null;
			hasAutoOption = true;
		}

		if(!hasAutoOption) {
			autoOption = option;
		}
		else {
			add(option);
		}
	}

	public void selectCursor() {
		select(getCursor());
	}

	public void select(MenuOption option) {
		option.onSelect(this);
		for(MenuListener listener : listeners) {
			listener.onSelect(option);
		}
	}

	public void addSelectListener(Consumer<MenuOption> callback) {
		addListener(new MenuListener() {
			@Override
			public void onSelect(MenuOption option) {
				callback.accept(option);
			}
		});
	}

	public void addListener(MenuListener listener) {
		listeners.add(listener);
	}

	public boolean removeListener(MenuListener listener) {
		return listeners.remove(listener);
	}

	@Override
	public void focus() {
		handle.focus(this);
		if(autoOption != null) {
			select(autoOption);
			autoOption = null;
		}
		for(MenuListener listener : listeners) {
			listener.onFocus();
		}
	}

	@Override
	public void render() {
		handle.render(this);
	}

	@Override
	public void keyPressed(KeyBinding key) {
		handle.keyPressed(this, key);
	}

	@Override
	public void keyReleased(KeyBinding key) {
	}

	@Override
	public void mouseWheel(int amount) {
		scroll(amount);
	}

	public void close() {
		if(getDefault() != null) {
			select(getDefault());
		}
	}
}
