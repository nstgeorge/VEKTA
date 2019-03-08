package vekta.mission;

import vekta.item.Item;
import vekta.menu.Menu;
import vekta.menu.handle.DialogMenuHandle;
import vekta.menu.option.BasicOption;
import vekta.object.SpaceObject;
import vekta.person.Dialog;
import vekta.person.Person;

import static vekta.Vekta.setContext;

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
		return "Deliver " + getItem().getName() + " to " + getPerson().getShortName();
	}

	@Override
	public SpaceObject getSpaceObject() {
		return getPerson().getHomeObject();
	}

	@Override
	public void onMenu(Menu menu) {
		if(menu.getHandle() instanceof DialogMenuHandle) {
			Dialog dialog = ((DialogMenuHandle)menu.getHandle()).getDialog();

			if(dialog.getPerson() == getPerson()) {
				menu.add(new BasicOption("Here's the " + getItem().getName() + ".", m -> {
					m.getPlayer().getInventory().remove(getItem());
					complete();
					Dialog newDialog = dialog.getPerson().createDialog("receive");
					Menu sub = new Menu(m.getPlayer(), new DialogMenuHandle(m.getDefault(), newDialog));
					setContext(sub);
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
