package vekta.spawner.dialog;

import vekta.menu.Menu;
import vekta.person.Dialog;
import vekta.spawner.DialogGenerator;
import vekta.spawner.StoryGenerator;
import vekta.story.part.Story;

public class RequestedStoryDialogSpawner implements DialogGenerator.DialogSpawner {
	@Override
	public String getType() {
		return "requested_story";
	}

	@Override
	public void setup(Menu menu, Dialog dialog) {
//		dialog.getPerson().setBusy(true);

		Story story = StoryGenerator.createStory();
		dialog.then(StoryGenerator.createDialog(dialog.getPerson(), story));
	}
}
