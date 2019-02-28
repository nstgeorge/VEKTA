package vekta;// These will move into their own files once we migrate to Maven

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static vekta.Vekta.*;

interface MenuOption {
	String getName();

	void select(Menu menu);
}

class BackOption implements MenuOption {
	private final Context parent;

	public BackOption(Context parent) {
		this.parent = parent;
	}

	@Override
	public String getName() {
		return "Back";
	}

	@Override
	public void select(Menu menu) {
		setContext(parent);
	}
}

class TradeMenuOption implements MenuOption {
	private final boolean buying;
	private final Inventory you, them;
	private final Map<Item, Integer> offers;

	public TradeMenuOption(boolean buying, Inventory you, Inventory them, Map<Item, Integer> offers) {
		this.buying = buying;
		this.you = you;
		this.them = them;
		this.offers = offers;
	}

	@Override
	public String getName() {
		return (buying ? "Buy" : "Sell") + " Goods";
	}

	public Inventory getFrom() {
		return buying ? them : you;
	}

	public Inventory getTo() {
		return buying ? you : them;
	}

	@Override
	public void select(Menu menu) {
		MenuOption def = new BackOption(menu);
		Menu sub = new Menu(new TradeMenuHandle(def, you, them));
		for(Item item : offers.keySet()) {
			if(getFrom().has(item)) {
				sub.add(new TradeOption(getFrom(), getTo(), item, offers.get(item), true));
			}
		}
		sub.add(def);
		setContext(sub);
	}
}

class TradeOption implements MenuOption {
	private final Inventory from, to;
	private final Item item;
	private final int price;
	private final boolean transfer;

	public TradeOption(Inventory from, Inventory to, Item item, int price, boolean transfer) {
		this.from = from;
		this.to = to;
		this.item = item;
		this.price = price;
		this.transfer = transfer;
	}

	@Override
	public String getName() {
		return item.getName() + " [" + price + " G]";
	}

	@Override
	public void select(Menu menu) {
		if(to.has(price) && from.has(item)) {
			to.remove(price);
			from.remove(item);
			from.add(price);
			if(transfer) {
				to.add(item);
			}
			menu.remove(this);
		}
	}
}

class InfoOption implements MenuOption {
	@Override
	public String getName() {
		return "Info";
	}

	@Override
	public void select(Menu menu) {
		// TODO: implement
	}
}

class TakeoffOption implements MenuOption {
	private final LandingSite site;
	private final World world;

	public TakeoffOption(LandingSite site, World world) {
		this.site = site;
		this.world = world;
	}

	@Override
	public String getName() {
		return "Take Off";
	}

	@Override
	public void select(Menu menu) {
		setContext(world);
		site.takeoff();
	}
}

/**
 * Default menu renderer implementation; draws buttons and select text
 */
class MenuHandle {
	// Default parameters
	private static final int DEF_SPACING = 100;
	private static final int DEF_WIDTH = 200;

	private final MenuOption defaultOption;
	private final int spacing;
	private final int buttonWidth;

	public MenuHandle(Context parent) {
		this(new BackOption(parent));
	}

	public MenuHandle(MenuOption defaultOption) {
		this(defaultOption, DEF_SPACING, DEF_WIDTH);
	}

	public MenuHandle(MenuOption defaultOption, int spacing, int buttonWidth) {
		this.defaultOption = defaultOption;
		this.spacing = spacing;
		this.buttonWidth = buttonWidth;
	}

	public MenuOption getDefault() {
		return defaultOption;
	}

	public void render(Menu menu) {
		Vekta v = getInstance();
		v.clear(); // TODO: find a way to avoid clearing without creating weird artifacts
		v.hint(DISABLE_DEPTH_TEST);
		v.camera();
		v.noLights();
		//shapeMode(CENTER);
		//shape(logo, width/2, height/4, 339.26, 100);
		v.textFont(bodyFont);

		v.stroke(0);
		v.fill(255);
		v.textAlign(CENTER, CENTER);
		v.textSize(24);
		for(int i = 0; i < menu.size(); i++) {
			drawButton(menu.get(i).getName(), (v.height / 2) + (i * spacing), menu.getIndex() == i);
		}

		//textFont(bodyFont);
		v.stroke(0);
		v.fill(255);
		v.textAlign(CENTER);
		v.text("X to select", v.width / 2, (v.height / 2) + (menu.size() * 100) + 200);
	}

	private void drawButton(String name, int yPos, boolean selected) {
		Vekta v = getInstance();
		if(selected)
			v.stroke(255);
		else
			v.stroke(UI_COLOR);
		v.fill(1);
		v.rectMode(CENTER);
		v.rect(v.width / 2, yPos, buttonWidth, 50);
		// Text ----------------------
		//textFont(bodyFont);
		v.stroke(0);
		v.fill(UI_COLOR);
		v.text(name, v.width / 2, yPos - 3);
	}

	public void keyPressed(Menu menu, char key) {
		if(key == ESC) {
			if(defaultOption != null) {
				defaultOption.select(menu);
			}
		}
		else if(key == 'w') {
			if(getSetting("sound") > 0)
				change.play();
			menu.scroll(-1);
		}
		else if(key == 's') {
			if(getSetting("sound") > 0)
				change.play();
			menu.scroll(1);
		}
		else if(key == 'x') {
			if(getSetting("sound") > 0)
				select.play();
			menu.getCursor().select(menu);
		}
	}
}

/**
 * Menu renderer for landing on planets
 */
class LandingMenuHandle extends MenuHandle {
	private final LandingSite site;

	public LandingMenuHandle(LandingSite site, World world) {
		super(new TakeoffOption(site, world));

		this.site = site;
	}

	public void render(Menu menu) {
		super.render(menu);

		Vekta v = getInstance();
		SpaceObject s = site.getParent();
		v.textSize(32);
		v.fill(100);
		v.text("Welcome to", v.width / 2, v.height / 4);
		v.textSize(48);
		v.fill(s.getColor());
		v.text(s.getName(), v.width / 2, v.height / 4 + 64);
	}
}

/**
 * Menu renderer for trading
 */
class TradeMenuHandle extends MenuHandle {
	private final Inventory you, them;

	public TradeMenuHandle(MenuOption defaultOption, Inventory you, Inventory them) {
		super(defaultOption, 60, Vekta.getInstance().width * 2 / 3);

		this.you = you;
		this.them = them;
	}

	public void render(Menu menu) {
		super.render(menu);

		Vekta v = getInstance();
		v.textSize(32);
		v.fill(100);
		v.text("You have: [" + you.getMoney() + "G]", v.width / 2, v.height / 4);
		v.text("They have: [" + them.getMoney() + "G]", v.width / 2, v.height / 4 + 48);
	}
}

class Menu implements Context {
	private final MenuHandle handle;

	private final List<MenuOption> items = new ArrayList<MenuOption>();

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
		if(getSetting("sound") > 0)
			change.play();
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
