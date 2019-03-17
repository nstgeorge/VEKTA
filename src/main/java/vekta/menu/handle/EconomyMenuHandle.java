package vekta.menu.handle;

import vekta.KeyBinding;
import vekta.Settings;
import vekta.economy.Economy;
import vekta.economy.ProductivityModifier;
import vekta.item.EconomyItem;
import vekta.item.Inventory;
import vekta.item.Item;
import vekta.item.ItemType;
import vekta.menu.Menu;
import vekta.menu.option.EconomyItemOption;
import vekta.menu.option.MenuOption;

import java.io.Serializable;
import java.util.List;

import static processing.core.PApplet.round;
import static processing.core.PConstants.LEFT;
import static vekta.Vekta.*;

/**
 * Mission selector inject handle
 */
public class EconomyMenuHandle extends MenuHandle {
	private final Inventory inv;

	private boolean buying = true;

	private final EconomyMenuCallback onChangeSide;

	public EconomyMenuHandle(MenuOption defaultOption, Inventory inv, EconomyMenuCallback onChangeSide) {
		super(defaultOption);

		this.inv = inv;
		this.onChangeSide = onChangeSide;
	}

	public Inventory getInventory() {
		return inv;
	}

	public boolean isBuying() {
		return buying;
	}

	public void setBuying(Menu menu, boolean buying) {
		this.buying = buying;
		onChangeSide.callback(menu, buying);
	}

	@Override
	public int getButtonWidth() {
		return v.width / 3;
	}

	@Override
	public int getButtonX() {
		int x = v.width / 6 + getButtonWidth() / 2;
		return buying ? x : v.width - x;
	}

	@Override
	public int getButtonY(int i) {
		return super.getButtonY(i - 2);
	}

	@Override
	public void focus(Menu menu) {
		super.focus(menu);

		if(buying && menu.size() == 1) {
			setBuying(menu, false);
		}
	}

	@Override
	public void render(Menu menu) {
		super.render(menu);

		v.textSize(32);
		v.fill(UI_COLOR);
		v.text((buying ? "Buying" : "Selling") + ": [" + inv.getMoney() + " G]", getButtonX(), getButtonY(-1));

		int currentIndex = menu.getIndex();
		int currentCount = 0;

		// Display number of owned items for each type
		v.textSize(24);
		v.fill(UI_COLOR);
		for(int i = 0; i < menu.size(); i++) {
			MenuOption opt = menu.get(i);
			if(opt instanceof EconomyItemOption) {
				EconomyItem item = ((EconomyItemOption)opt).getItem();
				int ct = 0;
				for(Item other : getInventory()) {
					if(item.getName().equals(other.getName())) {
						ct++;
					}
				}
				if(ct > 0) {
					v.text("x" + ct, getButtonX() + getButtonWidth() / 2F + 50, getButtonY(i) + 6);
					if(i == currentIndex) {
						currentCount = ct;
					}
				}
			}
		}

		// Ensure that the cursor item is related to a chart
		if(!(menu.getCursor() instanceof EconomyItemOption)) {
			return;
		}
		EconomyItem item = ((EconomyItemOption)menu.getCursor()).getItem();
		Economy economy = item.getEconomy();
		float[] history = economy.getHistory();

		// Configure chart position
		float chartCenter = v.width - getButtonX() + 200 * (buying ? 1 : -1);
		float chartLeft = chartCenter - getButtonWidth() / 2F;
		float chartRight = chartCenter + getButtonWidth() / 2F;
		float chartTop = v.height * .25F;
		float chartBottom = v.height * .5F;

		float max = 0;
		float min = Float.POSITIVE_INFINITY;
		for(float value : history) {
			if(value > max) {
				max = value;
			}
			else if(value < min) {
				min = value;
			}
		}

		v.noFill();
		v.stroke(ItemType.ECONOMY.getColor());

		float spacingX = (chartRight - chartLeft) / history.length;
		float spacingY = (chartBottom - chartTop) / (max - min);

		float prevX = 0;
		float prevY = 0;

		// Draw chart lines
		for(int i = 0; i < history.length; i++) {
			float value = history[i];

			float x = chartRight - i * spacingX;
			float y = chartTop + (max - value) * spacingY;

			if(i > 0) {
				v.line(prevX, prevY, x, y);
			}

			prevX = x;
			prevY = y;
		}

		v.fill(200);
		v.text(round(max) + " G", chartLeft, chartTop);
		v.text(round(min) + " G", chartLeft, chartBottom);

		v.textSize(24);
		v.textAlign(LEFT);

		// Draw modifier text
		List<ProductivityModifier> modifiers = economy.getModifiers();
		for(int i = 0; i < modifiers.size(); i++) {
			ProductivityModifier mod = modifiers.get(i);
			float p = economy.getProductivity(mod);
			v.fill(p > 0 ? UI_COLOR : p < 0 ? DANGER_COLOR : 100);
			v.text(mod.getModifierName(), chartLeft, chartBottom + (i + 2) * 32);
		}

		v.textAlign(RIGHT);
		v.fill(200);

		// Draw left/right key binding text when appropriate
		if(currentCount > 0 || !buying) {
			String keyText = buying
					? Settings.getKeyText(KeyBinding.MENU_RIGHT) + " to sell (>)"
					: Settings.getKeyText(KeyBinding.MENU_LEFT) + " to buy (<)";
			v.text(keyText, chartRight, chartBottom + 64);
		}
	}

	@Override
	public void keyPressed(Menu menu, KeyBinding key) {
		super.keyPressed(menu, key);

		if(key == KeyBinding.MENU_LEFT) {
			setBuying(menu, true);
		}
		else if(key == KeyBinding.MENU_RIGHT) {
			setBuying(menu, false);
		}
	}

	public interface EconomyMenuCallback extends Serializable {
		void callback(Menu menu, boolean buying);
	}
}
