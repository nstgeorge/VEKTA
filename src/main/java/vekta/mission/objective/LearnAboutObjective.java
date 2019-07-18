package vekta.mission.objective;

import vekta.knowledge.TopicKnowledge;
import vekta.menu.Menu;
import vekta.menu.handle.DialogMenuHandle;
import vekta.menu.option.DialogButton;
import vekta.object.SpaceObject;
import vekta.person.Dialog;
import vekta.person.Person;

import java.util.HashSet;
import java.util.Set;

import static vekta.Vekta.v;

public class LearnAboutObjective extends Objective {
	private final String topic;
	private final String description;
	private final float rarity;

	private final Set<Person> alreadyAsked = new HashSet<>();

	public LearnAboutObjective(String topic, String description, float rarity) {
		this.topic = topic;
		this.description = description;
		this.rarity = rarity;
	}

	public String getTopic() {
		return topic;
	}

	public String getDescription() {
		return description;
	}

	public float getRarity() {
		return rarity;
	}

	@Override
	public String getName() {
		return "Learn about " + getTopic();
	}

	@Override
	public SpaceObject getSpaceObject() {
		return null;
	}

	@Override
	public void onMenu(Menu menu) {
		if(menu.getHandle() instanceof DialogMenuHandle) {
			Dialog dialog = ((DialogMenuHandle)menu.getHandle()).getDialog();
			if(!alreadyAsked.contains(dialog.getPerson())) {
				alreadyAsked.add(dialog.getPerson());
				
				boolean foundInfo = v.chance(getRarity());
				Dialog next = foundInfo
						? new Dialog("topic", dialog.getPerson(), getDescription())
						: dialog.getPerson().createDialog("topic_unknown");

				if(foundInfo) {
					next.addResponse("Thanks for the help!");
					complete();
					menu.getPlayer().addKnowledge(new TopicKnowledge(getTopic(), getDescription(), (int)(1 / getRarity())));
				}

				menu.add(new DialogButton("Ask about " + getTopic(), next));
			}
		}
	}
}
