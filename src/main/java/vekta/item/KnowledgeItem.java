package vekta.item;

import vekta.Player;
import vekta.context.KnowledgeContext;
import vekta.knowledge.Knowledge;
import vekta.knowledge.KnowledgeDelta;
import vekta.menu.option.ItemTradeButton;

import java.io.Serializable;

import static vekta.Vekta.getContext;
import static vekta.Vekta.setContext;

public class KnowledgeItem extends Item implements ItemTradeButton.TradeAware {
	private final String name;
	private final KnowledgeProvider provider;

	private Knowledge knowledge;
	private Player player;
	boolean enabled;

	public KnowledgeItem(Knowledge knowledge) {
		this(knowledge.getName(), () -> knowledge);
	}

	public KnowledgeItem(String name, KnowledgeProvider provider) {
		this.name = name;
		this.provider = provider;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public ItemType getType() {
		return ItemType.KNOWLEDGE;
	}

	@Override
	public int getColor() {
		if(knowledge != null) {
			return knowledge.getColor(player);
		}
		return super.getColor();
	}

	@Override
	public int getMass() {
		return 1;
	}

	@Override
	public boolean isTradeEnabled(Player player) {
		findKnowledge(player);
		return enabled;
	}

	@Override
	public void onAdd(Player player) {
		Knowledge knowledge = findKnowledge(player);
		player.addKnowledge(knowledge);

		setContext(new KnowledgeContext(getContext(), player)
				.withTab(getName(), knowledge));

		player.getInventory().remove(this);
	}

	private Knowledge findKnowledge(Player player) {
		if(knowledge == null || player != this.player) {
			this.knowledge = provider.provide();
			this.player = player;
			this.enabled = !player.hasKnowledge(knowledge.getClass(), k -> {
				KnowledgeDelta delta = knowledge.getDelta(k);
				return delta == KnowledgeDelta.SAME || delta == KnowledgeDelta.WORSE;
			});
		}
		return knowledge;
	}

	public interface KnowledgeProvider extends Serializable {
		Knowledge provide();
	}
}
