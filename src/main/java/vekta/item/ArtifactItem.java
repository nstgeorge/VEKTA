package vekta.item;

import vekta.InfoGroup;
import vekta.Player;
import vekta.menu.Menu;
import vekta.menu.handle.DialogMenuHandle;
import vekta.person.Dialog;

import static vekta.Vekta.v;

public class ArtifactItem extends BasicItem {
	private static final float IDENTIFY_CHANCE = .5F;

	private final String description;

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
	public void onAdd(Player player) {
		super.onAdd(player);

	}

	@Override
	public void onMenu(Menu menu) {
		super.onMenu(menu);

		if(menu.getHandle() instanceof DialogMenuHandle) {
			DialogMenuHandle handle = (DialogMenuHandle)menu.getHandle();
			if(handle.getPerson().hasInterest("Artifacts")) {
				Dialog dialog = handle.getPerson().createDialog("identify");
				if(v.chance(IDENTIFY_CHANCE)) {
					dialog.then(new Dialog("identify_success", handle.getPerson(), getDescription(), ItemType.LEGENDARY.getColor()));
				}
				else {
					dialog.then("identify_fail");
				}
				handle.getDialog().add("Can you identify this " + getName() + "?", dialog);
			}
		}
	}
}
