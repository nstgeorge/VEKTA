package vekta.menu.option;

import vekta.menu.Menu;
import vekta.person.Dialog;
import vekta.person.Person;
import vekta.spawner.StoryGenerator;
import vekta.story.part.Story;

public class StoryProgressButton implements ButtonOption {
	private final Story story;
	private final Person person;
	private final int steps;

	public StoryProgressButton(Story story, Person person, int steps) {
		this.story = story;
		this.person = person;
		this.steps = steps;
	}

	@Override
	public String getName() {
		return "Next";
	}

	@Override
	public void onSelect(Menu menu) {
		story.proceed(1);
		Dialog dialog = StoryGenerator.createDialog(person, story, steps);
		dialog.openMenu(menu.getPlayer(), menu.getDefault());
	}
}
