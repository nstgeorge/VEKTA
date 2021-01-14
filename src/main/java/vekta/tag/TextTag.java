package vekta.tag;

public class TextTag extends Tag {
	private final String text;

	public TextTag(String text) {
		super("Text"); // TODO: split hierarchy

		this.text = text;
	}

	public String getText() {
		return text;
	}

	@Override
	public String toString() {
		return '"' + text + '"';
	}
}
