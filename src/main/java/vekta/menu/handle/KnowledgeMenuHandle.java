package vekta.menu.handle;

import vekta.KeyBinding;
import vekta.knowledge.Knowledge;
import vekta.menu.Menu;

import static vekta.Vekta.v;

public class KnowledgeMenuHandle extends MenuHandle {
	private final Knowledge knowledge;

	public KnowledgeMenuHandle(Knowledge knowledge) {
		this.knowledge = knowledge;
	}

	public Knowledge getKnowledge() {
		return knowledge;
	}

	@Override
	public KeyBinding getShortcutKey() {
		return KeyBinding.SHIP_KNOWLEDGE;
	}

	@Override
	public void render(Menu menu) {
		super.render(menu);

		v.textSize(48);
		v.fill(knowledge.getColor(menu.getPlayer()));
		v.text(knowledge.getName(), v.width / 2F, getItemY(-3));
	}
}
