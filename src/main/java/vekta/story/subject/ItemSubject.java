package vekta.story.subject;

import vekta.item.Item;

public class ItemSubject implements StorySubject {
	private final Item item;

	public ItemSubject(Item item) {
		this.item = item;
	}

	public Item getItem() {
		return item;
	}

	@Override
	public String getFullName() {
		return getItem().getName();
	}
}
