package vekta.menu.option;

import vekta.player.Player;
import vekta.context.KnowledgeContext;
import vekta.menu.Menu;

import static vekta.Vekta.getContext;
import static vekta.Vekta.setContext;

public class PlayerKnowledgeButton extends ButtonOption {
	public PlayerKnowledgeButton() {
	}

	@Override
	public String getName() {
		return "Knowledge";
	}

	@Override
	public void onSelect(Menu menu) {
		Player player = menu.getPlayer();
		setContext(new KnowledgeContext(getContext(), player));
	}
}
