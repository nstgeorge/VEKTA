package vekta.item;

import vekta.Resources;
import vekta.menu.Menu;
import vekta.menu.handle.DialogMenuHandle;
import vekta.menu.option.MurderOption;
import vekta.person.Dialog;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class WeaponItem extends Item {
	private static final Set<String> DIALOG_FILTER;

	static {
		String[] types = Resources.getStrings("dialog_weapon_filter");
		for(String type : types) {
			if(!Resources.hasStrings(type)) {
				throw new RuntimeException("Missing weapon dialog filter type: `" + type + "`");
			}
		}
		DIALOG_FILTER = new HashSet<>(Arrays.asList(types));
	}

	private final String action;

	public WeaponItem(String name, String action) {
		super(name, ItemType.DANGEROUS);

		this.action = action;
	}

	public String getAction() {
		return action;
	}

	@Override
	public void onMenu(Menu menu) {
		if(menu.getHandle() instanceof DialogMenuHandle) {
			Dialog dialog = ((DialogMenuHandle)menu.getHandle()).getDialog();
			if(DIALOG_FILTER.contains(dialog.getType())) {
				setupDialog(dialog); /// necessary to set up menu rather than dialog?
			}
		}
	}

	public void setupDialog(Dialog dialog) {
		dialog.add(new MurderOption(dialog.getPerson(), this));
	}
}
