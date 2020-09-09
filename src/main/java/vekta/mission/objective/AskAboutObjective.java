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

public class AskAboutObjective extends Objective {
	private final TopicKnowledge knowledge;

	private final Set<Person> alreadyAsked = new HashSet<>();

	public AskAboutObjective(TopicKnowledge knowledge) {
		this.knowledge = knowledge;
	}

	public String getTopic() {
		return knowledge.getTopic();
	}

	public String getDescription() {
		return knowledge.getDescription();
	}

	public float getRarity() {
		return 1F / knowledge.getArchiveValue();
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
			if(getMissions().stream().allMatch(m -> m.getIssuer() != dialog.getPerson()) && !alreadyAsked.contains(dialog.getPerson())) {
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
