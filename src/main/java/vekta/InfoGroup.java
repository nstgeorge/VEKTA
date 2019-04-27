package vekta;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class InfoGroup implements Iterable<String> {
	private final List<String> info = new ArrayList<>();

	public InfoGroup() {
	}

	public void clear() {
		info.clear();
	}

	public void addDescription(String detail) {
		info.add(detail);
	}

	public void addKey(KeyBinding key, String value) {
		info.add("[" + Settings.getKeyText(key) + "] " + value);
	}

	public void addStat(String key, int value) {
		addStat(key, String.valueOf(value));
	}

	public void addStat(String key, float value) {
		addStat(key, String.valueOf(value));
	}

	public void addStat(String key, String value) {
		info.add(key + ": " + value);
	}

	public void addTrait(String trait) {
		info.add("* " + trait);
	}

	@Override
	public Iterator<String> iterator() {
		return info.iterator();
	}
}
