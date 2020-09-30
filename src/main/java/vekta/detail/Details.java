package vekta.detail;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Details implements Serializable {
	private final Map<Class<? extends Detail>, Detail> map = new HashMap<>();

	public boolean has(Class<? extends Detail> cls) {
		return map.containsKey(cls);
	}

	@SuppressWarnings("unchecked")
	public <T extends Detail> T get(Class<T> cls) {
		Detail detail = map.get(cls);
		if(detail == null) {
			detail = createDefault(cls);
			map.put(cls, detail);
		}
		return (T)detail;
	}

	private <T extends Detail> T createDefault(Class<T> cls) {
		try {
			return cls.newInstance();
		}
		catch(Exception e) {
			throw new RuntimeException("Failed to initialize " + cls.getName(), e);
		}
	}
}
