package vekta.tag.resolver;

import vekta.Resources;
import vekta.display.Display;
import vekta.display.TextDisplay;
import vekta.tag.Tag;
import vekta.tag.TextTag;

public class TextTagResolver extends TagResolver {

	@Override
	public Display compile(Tag tag) {
		if(tag instanceof TextTag) {
			return new TextDisplay(((TextTag)tag).getText());
		}
		else if(tag.getChildren().size() == 1) {
			// Special case for explicit <Text/> tags with one child element
			return Resources.createUI(tag.getChildren().get(0));
		}
		return null;
	}
}

