package vekta.item;

import vekta.InfoGroup;
import vekta.menu.Menu;
import vekta.menu.handle.DialogMenuHandle;
import vekta.person.Dialog;
import vekta.person.Person;

import java.util.HashSet;
import java.util.Set;

import static vekta.Vekta.v;

public class ArtifactItem extends BasicItem {
	private static final float IDENTIFY_CHANCE = .5F;

	private final String description;

	private final Set<Person> alreadyAsked = new HashSet<>();

	private boolean identified;

	public ArtifactItem(String name, String description, ItemType type) {
		super(name, type);

		this.description = description;
	}

	@Override
	public ItemType getType() {
		return isIdentified() ? super.getType() : ItemType.COMMON;
	}

	public String getDescription() {
		return description;
	}

	public boolean isIdentified() {
		return identified;
	}

	public void identify() {
		identified = true;
	}

	@Override
	public void onInfo(InfoGroup info) {
		if(isIdentified()) {
			info.addDescription(getDescription());
		}
		else {
			info.addTrait("Unidentified");
		}
	}
	
	@Override
	public void onMenu(Menu menu) {
		super.onMenu(menu);

		if(menu.getHandle() instanceof DialogMenuHandle) {
			DialogMenuHandle handle = (DialogMenuHandle)menu.getHandle();
			Person person = handle.getPerson();
			if(handle.getDialog().getType().equals("identify_success")) {
				identify();
			}
			else if(!alreadyAsked.contains(person) && handle.getPerson().hasInterest("Artifacts")) {
				alreadyAsked.add(person);

				Dialog dialog = person.createDialog("identify");
				if(v.chance(IDENTIFY_CHANCE)) {
					dialog.then(new Dialog("identify_success", person, getDescription(), ItemType.LEGENDARY.getColor()));
				}
				else {
					dialog.then("identify_fail");
				}
				handle.getDialog().add("Can you identify this " + getName() + "?", dialog);
			}
		}
	}
}
