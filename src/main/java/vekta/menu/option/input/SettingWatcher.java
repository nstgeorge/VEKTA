package vekta.menu.option.input;

import vekta.Resources;

public abstract class SettingWatcher<K, V> implements InputWatcher<V> {
	private final K key;

	public SettingWatcher(K key) {
		this.key = key;
	}

	public K getKey() {
		return key;
	}

	@Override
	public final V getValue() {
		return findSetting();
	}

	@Override
	public final void setValue(V value) {
		updateSetting(value);
		Resources.adjustFromSettings();
	}

	public abstract V findSetting();

	public abstract void updateSetting(V value);
}
