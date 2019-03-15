package vekta.mission.objective;

import vekta.menu.Menu;
import vekta.menu.handle.DialogMenuHandle;
import vekta.menu.option.DialogOption;
import vekta.object.SpaceObject;
import vekta.person.Dialog;
import vekta.person.Person;

import java.util.HashSet;
import java.util.Set;

import static vekta.Vekta.v;

public class LearnAboutObjective extends Objective {
	private final String topic;
	private final String info;
	private final float rarity;

	private final Set<Person> alreadyAsked = new HashSet<>();

	public LearnAboutObjective(String topic, String info, float rarity) {
		this.topic = topic;
		this.info = info;
		this.rarity = rarity;
	}

	public String getTopic() {
		return topic;
	}

	public String getInfo() {
		return info;
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
				if(foundInfo) {
					complete();
				}
				
				Dialog next = foundInfo
						? new Dialog("topic", dialog.getPerson(), getInfo())
						: dialog.getPerson().createDialog("unknown_topic");
				next.addResponse("Thanks for the help!");
				menu.add(new DialogOption("Ask about " + getTopic(), next));
			}
		}
	}
}
