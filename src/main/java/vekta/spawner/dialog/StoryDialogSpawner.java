package vekta.spawner.dialog;

import vekta.knowledge.StoryKnowledge;
import vekta.menu.Menu;
import vekta.person.Dialog;
import vekta.spawner.DialogGenerator;
import vekta.story.Story;

public class StoryDialogSpawner implements DialogGenerator.DialogSpawner {
	@Override
	public String getType() {
		return "story";
	}

	@Override
	public void setup(Menu menu, Dialog dialog) {
		for(StoryKnowledge knowledge : menu.getPlayer().findKnowledge(StoryKnowledge.class)) {
			Story story = knowledge.getStory();
			for(String key : story.getSubjects().keySet()) {
				if(dialog.getPerson().getFullName().equals(story.getSubject(key).getFullName())) {
					knowledge.setAskedForDetails(key);
				}
			}
		}
	}
}
