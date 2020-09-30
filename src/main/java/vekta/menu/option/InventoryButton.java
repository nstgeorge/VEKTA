package vekta.menu.option;

import vekta.item.Inventory;
import vekta.item.Item;
import vekta.menu.Menu;
import vekta.menu.handle.InventoryMenuHandle;

import static vekta.Vekta.setContext;

public class InventoryButton implements ButtonOption {
    private final Inventory inv;

    public InventoryButton(Inventory inv) {
        this.inv = inv;
    }

    @Override
    public String getName() {
        return "Inventory";
    }

    @Override
    public void onSelect(Menu menu) {
        Menu sub = new Menu(menu, new InventoryMenuHandle(inv));
        for(Item item : inv) {
            sub.add(new JettisonButton(item, inv));
        }
        sub.addDefault();
        setContext(sub);
    }
}
