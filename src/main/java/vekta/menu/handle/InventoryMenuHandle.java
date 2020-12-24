package vekta.menu.handle;

import vekta.KeyBinding;
import vekta.item.Inventory;

import static vekta.Vekta.UI_COLOR;
import static vekta.Vekta.v;

public class InventoryMenuHandle extends SideLayoutMenuHandle {
    private final Inventory inv;

    private String title;

    public InventoryMenuHandle(Inventory inv) {
        super(false);

        this.inv = inv;
    }

    public Inventory getInventory() {
        return inv;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public KeyBinding getShortcutKey() {
        return KeyBinding.SHIP_INVENTORY;
    }

    @Override
    public void render() {
        super.render();

        if(getTitle() != null) {
            v.textSize(24);
            v.fill(200);
            v.text(getTitle(), getItemX(), getY() - (getY() / 2));
        }

        v.textSize(32);
        v.fill(UI_COLOR);
        v.text("Gold: [" + getInventory().getMoney() + " G]", getItemX(), getItemY(-2));
    }
}
