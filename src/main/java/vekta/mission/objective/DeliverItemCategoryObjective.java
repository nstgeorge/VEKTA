package vekta.mission.objective;

import vekta.item.Inventory;
import vekta.item.Item;
import vekta.item.category.ItemCategory;
import vekta.menu.Menu;
import vekta.menu.handle.DialogMenuHandle;
import vekta.menu.option.CustomButton;
import vekta.mission.Mission;
import vekta.object.SpaceObject;
import vekta.person.Dialog;
import vekta.person.Person;
import vekta.player.Player;

public class DeliverItemCategoryObjective extends Objective {
	private final ItemCategory category;
	private final Person person;

	public DeliverItemCategoryObjective(ItemCategory category, Person person) {
		this.category = category;
		this.person = person;
	}

	public ItemCategory getCategory() {
		return category;
	}

	public Person getPerson() {
		return person;
	}

	public Item findItem(Player player) {
		return player.getInventory().stream().filter(getCategory()::isIncluded).findFirst().orElse(null);
	}

	@Override
	public String getName() {
		return "Deliver " + getCategory().getName() + " to " + getPerson().getFullName();
	}

	@Override
	public SpaceObject getSpaceObject() {
		return getPerson().findHomeObject();
	}

//	@Override
//	public void onStartFirst(Mission mission) {
//		mission.getPlayer().getInventory().add(item);
//	}

	@Override
	public void onMenu(Menu menu) {
		if(menu.getHandle() instanceof DialogMenuHandle) {
			Dialog dialog = ((DialogMenuHandle)menu.getHandle()).getDialog();

			if(dialog.getPerson() == getPerson()) {

				Item item = findItem(menu.getPlayer());
				if(item != null) {
					menu.add(new CustomButton("Does this " + item.getName() + " suit your needs?", m -> {
						m.getPlayer().getInventory().remove(item);
						complete();
						dialog.getPerson().createDialog("receive").openMenu(m.getPlayer(), m.getDefault());
					}));
				}
			}
		}
	}

	@Override
	public void onRemoveItem(Item item) {
		//		if(findItem() == null) {
		//			cancel();
		//		}
	}
}
