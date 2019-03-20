package vekta.display;

import java.util.ArrayList;
import java.util.List;

import static processing.core.PApplet.max;
import static processing.core.PConstants.LEFT;
import static vekta.Vekta.v;

public class TextDisplay extends StyledDisplay {
	private static final String WRAP_DELIMITER = " ";

	private String text;

	private float cachedWidth;
	private List<String> cachedLines;

	public TextDisplay(DisplayStyle style) {
		super(style);
	}

	public TextDisplay(DisplayStyle style, String text) {
		this(style);

		setText(text);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		if(text == null) {
			text = "";
		}
		if(!text.equals(getText())) {
			this.text = text;
			cachedWidth = 0; // Reset cache
		}
	}

	public float getLineSpacing() {
		return max(getStyle().fontSize(), getStyle().spacing());
	}

	@Override
	public float getHeight(float width, float height) {
		return getLineSpacing() * getLineCount(width);
	}

	@Override
	public void draw(float width, float height) {
		v.textAlign(LEFT); // TODO: align style
		v.textSize(getStyle().fontSize());
		v.fill(getStyle().color());

		List<String> lines = getLines(width);
		float spacing = getLineSpacing();
		float y = 0;
		for(String line : lines) {
			//			if(y >= height) {
			//				break; // Don't draw overflowing lines
			//			}

			v.text(line, 0, y);
			y += spacing;
		}
	}

	public List<String> getLines(float width) {
		if(width == cachedWidth) {
			return cachedLines;
		}

		// Split words by space character
		String[] words = text.split(WRAP_DELIMITER);

		List<String> lines = new ArrayList<>();
		String currentLine = "";
		for(String word : words) {
			String expected = currentLine.isEmpty() ? word : currentLine + WRAP_DELIMITER + word;

			if(v.textWidth(expected) > width) {
				lines.add(currentLine);
				currentLine = word;
			}
			else {
				currentLine = expected;
			}
		}
		if(!currentLine.isEmpty()) {
			lines.add(currentLine);
		}

		cachedWidth = width;
		cachedLines = lines;
		return lines;
	}

	public int getLineCount(float width) {
		return getLines(width).size();
	}
}
