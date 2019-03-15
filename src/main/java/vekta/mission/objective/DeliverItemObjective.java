package vekta.mission.objective;

import vekta.item.Item;
import vekta.menu.Menu;
import vekta.menu.handle.DialogMenuHandle;
import vekta.menu.option.CustomOption;
import vekta.mission.Mission;
import vekta.object.SpaceObject;
import vekta.person.Dialog;
import vekta.person.Person;

public class DeliverItemObjective extends Objective {
	private final Item item;
	private final Person person;

	public DeliverItemObjective(Item item, Person person) {
		this.item = item;
		this.person = person;
	}

	public Item getItem() {
		return item;
	}

	public Person getPerson() {
		return person;
	}

	@Override
	public String getName() {
		return "Deliver " + getItem().getName() + " to " + getPerson().getName();
	}

	@Override
	public SpaceObject getSpaceObject() {
		return getPerson().findHomeObject();
	}

	@Override
	public void onStartFirst(Mission mission) {
		mission.getPlayer().getInventory().add(item);
	}

	@Override
	public void onMenu(Menu menu) {
		if(menu.getHandle() instanceof DialogMenuHandle) {
			Dialog dialog = ((DialogMenuHandle)menu.getHandle()).getDialog();

			if(dialog.getPerson() == getPerson()) {
				menu.add(new CustomOption("Here's the " + getItem().getName() + ".", m -> {
					m.getPlayer().getInventory().remove(getItem());
					complete();
					dialog.getPerson().createDialog("receive").openMenu(m.getPlayer(), m.getDefault());
				}));
			}
		}
	}

	@Override
	public void onRemoveItem(Item item) {
		if(item == getItem()) {
			cancel();
		}
	}
}
