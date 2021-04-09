package vekta.tag.resolver;

import vekta.display.Layout;
import vekta.display.VerticalLayout;
import vekta.tag.Tag;

public class VerticalTagResolver extends LayoutTagResolver {

	@Override
	public Layout createLayout(Tag node) {
		return new VerticalLayout();
	}
}

