package vekta.display;

import java.util.ArrayList;
import java.util.List;

public abstract class Layout extends StyledDisplay {
	private final List<Display> items = new ArrayList<>();
	
	protected List<Display> getItems() {
		return items;
	}

	public <T extends Display> T add(T item) {
		items.add(item);
		// TODO: general add/remove callbacks
		if(item instanceof StyledDisplay) {
			((StyledDisplay)item).setStyle(getStyle());
		}
		return item;
	}

	public void remove(Display item) {
		items.remove(item);
	}
}
