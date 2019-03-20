package vekta.menu.option;

import vekta.Player;
import vekta.context.KnowledgeContext;
import vekta.menu.Menu;

import static vekta.Vekta.getWorld;
import static vekta.Vekta.setContext;

public class PlayerKnowledgeOption implements MenuOption {
	public PlayerKnowledgeOption() {
	}

	@Override
	public String getName() {
		return "Knowledge";
	}

	@Override
	public void onSelect(Menu menu) {
		Player player = menu.getPlayer();
		setContext(new KnowledgeContext(getWorld(), player));
	}
}
