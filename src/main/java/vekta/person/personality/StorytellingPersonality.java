package vekta.person.personality;

import vekta.Resources;
import vekta.detail.Detail;
import vekta.person.Dialog;
import vekta.person.Person;

import java.util.List;
import java.util.Map;

import static vekta.Vekta.v;

public class StorytellingPersonality extends Personality {
	@Override
	public void preparePerson(Person person) {
		person.addInterest("storytelling");
	}

	@Override
	public void prepareDialog(Dialog dialog) {
		if(dialog.getType().equals("chat")) {
			StorytellingDetail detail = dialog.getPerson().getDetail(StorytellingDetail.class);

			if(!detail.toldStory) {
				dialog.add("Tell me a story!", dialog.getPerson().createDialog("requested_story"));
			}
		}
	}

	public class StorytellingDetail implements Detail {
		public boolean toldStory;
	}
}
