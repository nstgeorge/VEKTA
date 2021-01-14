package vekta.tag;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Tag {
	private final String name;
	private final Map<String, String> attributes = new LinkedHashMap<>();
	private final List<Tag> children = new ArrayList<>();

	public Tag(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public boolean hasAttribute(String key) {
		return attributes.containsKey(key);
	}

	public void setAttribute(String key, String value) {
		attributes.put(key, value);
	}

	public Object getAttribute(String key) {
		return attributes.get(key);
	}

	public List<Tag> getChildren() {
		return children;
	}

	public void prepend(Tag node) {
		children.add(0, node);
	}

	public void append(Tag node) {
		children.add(node);
	}

	public void remove(Tag node) {
		children.remove(node);
	}

	@Override
	public String toString() {
		return "<" + getName() + "" + attributes.entrySet().stream().map(p -> p.getKey() + "=\"" + p.getValue() + "\"").collect(Collectors.joining(" "))
				+ (getChildren().isEmpty() ? "/>" : "> " + children.stream().map(String::valueOf).collect(Collectors.joining(" ")) + " </" + getName() + ">");
	}
}
