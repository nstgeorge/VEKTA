package vekta.menu;

import vekta.KeyBinding;
import vekta.Player;
import vekta.PlayerEvent;
import vekta.context.Context;
import vekta.menu.handle.MenuHandle;
import vekta.menu.option.MenuOption;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Menu implements Context {
	private final Player player;
	private final MenuHandle handle;

	private final List<MenuOption> options = new ArrayList<>();
	private final List<MenuListener> listeners = new ArrayList<>();

	private boolean hasAutoOption;
	private MenuOption autoOption;

	private int index;

	public Menu(Player player, MenuHandle handle) {
		this.player = player;
		this.handle = handle;
		
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
		return options.get(index);
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
		return options.get(i);
	}

	public MenuOption getDefault() {
		return handle.getDefault();
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
		add(handle.getDefault());
	}

	public boolean remove(MenuOption item) {
		if(options.remove(item)) {
			if(size() == 0) {
				close();
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

	public void select() {
		select(getCursor());
	}

	public void select(MenuOption option) {
		option.select(this);
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

	public void addCloseListener(Runnable callback) {
		addListener(new MenuListener() {
			@Override
			public void onClose() {
				callback.run();
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
		// If menu changed, ensure that the index is valid
		if(index >= size()) {
			index = size() - 1;
		}
		
		handle.focus(this);
		if(autoOption != null) {
			autoOption.select(this);
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
		if(handle.getDefault() != null) {
			handle.getDefault().select(this);
		}
		for(MenuListener listener : listeners) {
			listener.onClose();
		}
	}
}
