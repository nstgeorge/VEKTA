package vekta.tag.resolver;

import vekta.display.Display;
import vekta.tag.Tag;

public abstract class TagResolver {

	public abstract Display compile(Tag tag);
}
