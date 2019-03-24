package vekta.item;

import vekta.menu.Menu;
import vekta.menu.handle.DialogMenuHandle;
import vekta.person.Dialog;
import vekta.spawner.item.DialogItemSpawner;

public class DialogItem extends BasicItem {

	public DialogItem(String name, ItemType type) {
		super(name, type);
	}

	@Override
	public void onMenu(Menu menu) {
		if(menu.getHandle() instanceof DialogMenuHandle) {
			Dialog dialog = ((DialogMenuHandle)menu.getHandle()).getDialog();

			String response = DialogItemSpawner.randomDialogResponse(getName(), dialog.getType());
			if(response != null) {
				dialog.parseResponse(response);
			}
		}
	}
}
