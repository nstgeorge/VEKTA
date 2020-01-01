package vekta.item;

import vekta.player.Player;
import vekta.knowledge.StoryKnowledge;
import vekta.menu.option.BackButton;
import vekta.person.Dialog;
import vekta.person.Person;
import vekta.person.TemporaryPerson;
import vekta.spawner.StoryGenerator;
import vekta.story.Story;

import java.io.Serializable;

import static vekta.Vekta.getContext;

public class StoryItem extends Item {
	private final String name;
	private final StoryProvider provider;

	public StoryItem(String name, StoryProvider provider) {
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
	public int getMass() {
		return 1;
	}

	@Override
	public int randomPrice() {
		return super.randomPrice() * 2;
	}

	@Override
	public void onAdd(Player player) {
		Story story = provider.provide(player);

		boolean hadStory = player.hasKnowledge(StoryKnowledge.class, k -> k.getStory() == story);
		player.addKnowledge(new StoryKnowledge(story, getName()));
		if(!hadStory) {
			Person person = new TemporaryPerson(getName(), player.getFaction());
			Dialog dialog = StoryGenerator.createDialog(person, story);

			dialog.openMenu(player, new BackButton(getContext()));
		}

		player.getInventory().remove(this);
	}

	public interface StoryProvider extends Serializable {
		Story provide(Player player);
	}
}
