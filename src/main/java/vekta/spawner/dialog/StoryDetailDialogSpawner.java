package vekta.spawner.dialog;

import vekta.knowledge.StoryKnowledge;
import vekta.menu.Menu;
import vekta.person.Dialog;
import vekta.spawner.DialogGenerator;
import vekta.story.part.Story;

public class StoryDetailDialogSpawner implements DialogGenerator.DialogSpawner {
	@Override
	public String getType() {
		return "story_detail";
	}

	@Override
	public void setup(Menu menu, Dialog dialog) {
		for(StoryKnowledge knowledge : menu.getPlayer().findKnowledge(StoryKnowledge.class)) {
			Story story = knowledge.getStory();
			for(String key : story.getSubjects().keySet()) {
				if(dialog.getPerson().getFullName().equals(story.getSubject(key).getFullName())) {
					// Remember already asking this person about the story
					knowledge.setAskedForDetails(key);
				}
			}
		}
	}
}
