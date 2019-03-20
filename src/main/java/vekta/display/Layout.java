package vekta.display;

import java.util.ArrayList;
import java.util.List;

public abstract class Layout extends StyledDisplay {
	private final List<Display> items = new ArrayList<>();

	public Layout(DisplayStyle style) {
		super(style);
	}

	protected List<Display> getItems() {
		return items;
	}

	public <T extends Display> T add(T item) {
		items.add(item);
		return item;
	}

	public void remove(Display item) {
		items.remove(item);
	}
}
