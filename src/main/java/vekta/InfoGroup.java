package vekta;

import vekta.display.Layout;
import vekta.display.TextDisplay;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class InfoGroup implements Iterable<String> {
	private final List<String> lines = new ArrayList<>();

	public InfoGroup() {
	}

	public void clear() {
		lines.clear();
	}

	public void addDescription(String detail) {
		lines.add(detail);
	}

	public void addKey(KeyBinding key, String value) {
		lines.add("[" + Settings.getKeyText(key) + "] " + value);
	}

	public void addStat(String key, int value) {
		addStat(key, String.valueOf(value));
	}

	public void addStat(String key, float value) {
		addStat(key, String.valueOf(value));
	}

	public void addStat(String key, String value) {
		lines.add(key + ": " + value);
	}

	public void addTrait(String trait) {
		lines.add("* " + trait);
	}
	
	public void addAll(InfoGroup other) {
		lines.addAll(other.lines);
	}

	public void onLayout(Layout layout) {
		// TODO: improve
		for(String line : lines) {
			layout.add(new TextDisplay(line));
		}
	}

	@Override
	public Iterator<String> iterator() {
		return lines.iterator();
	}
}
