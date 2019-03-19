package vekta.menu.option;

import vekta.Player;
import vekta.context.NavigationContext;
import vekta.menu.Menu;

import static vekta.Vekta.setContext;
import static vekta.Vekta.getWorld;

public class NavigationOption implements MenuOption {

    Player player;

    public NavigationOption(Player player) {
        this.player = player;
    }

    @Override
    public String getName() {
        return "Navigation";
    }

    @Override
    public void onSelect(Menu menu) {
        setContext(new NavigationContext(getWorld(), player));
    }
}
