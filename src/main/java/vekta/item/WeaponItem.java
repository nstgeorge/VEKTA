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
			if(!Resources.hasStrings("dialog_" + type)) {
				throw new RuntimeException("Missing weapon dialog filter type: `" + type + "`");
			}
		}
		DIALOG_FILTER = new HashSet<>(Arrays.asList(types));
	}

	private final String name;
	private final String action;

	public WeaponItem(String name, String action) {
		this.name = name;
		this.action = action;
	}

	public String getAction() {
		return action;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public ItemType getType() {
		return ItemType.DANGEROUS;
	}

	@Override
	public int getMass() {
		return 20;
	}

	@Override
	public void onMenu(Menu menu) {
		if(menu.getHandle() instanceof DialogMenuHandle) {
			Dialog dialog = ((DialogMenuHandle)menu.getHandle()).getDialog();
			if(DIALOG_FILTER.contains(dialog.getType())) {
				setupDialog(dialog);
				dialog.add(menu.getDefault());
			}
		}
	}

	public void setupDialog(Dialog dialog) {
		dialog.add(new MurderOption(dialog.getPerson(), this));
	}
}
