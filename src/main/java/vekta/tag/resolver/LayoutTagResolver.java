package vekta.tag.resolver;

import vekta.Resources;
import vekta.display.Display;
import vekta.display.Layout;
import vekta.tag.Tag;

public abstract class LayoutTagResolver extends TagResolver {

	@Override
	public Display compile(Tag tag) {
		Layout layout = createLayout(tag);

		for(Tag child : tag.getChildren()) {
			Display part = Resources.createUI(child);
			// Allowing null for now
			if(part != null) {
				layout.add(part);
			}
		}

		return layout;
	}

	public abstract Layout createLayout(Tag tag);
}

