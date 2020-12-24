package vekta.config;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
public class Config implements Serializable {
	private static final Random RANDOM = new Random();

	//	public String type;
	//	public Object value;

	private Config[] children = {};

	public Config() {
	}

	@JsonGetter
	public Config[] getChildren() {
		return children;
	}

	@JsonSetter
	public void setChildren(Config[] children) {
		this.children = children;
	}

	public <T extends Config> void expand(Class<T> type, List<T> list) {
		if(this.getClass().isAssignableFrom(type)) {
			list.add(cast());
		}
		for(Config child : this.children) {
			child.expand(type, list);
		}
	}

	public <T extends Config> void expandRandom(Class<T> type, List<T> list, Random random) {
		expand(type, list);
	}

	public final <T extends Config> T random(Class<T> type) {
		return random(type, RANDOM);
	}

	public <T extends Config> T random(Class<T> type, Random random) {
		List<T> list = new ArrayList<>();
		expandRandom(type, list, random);
		return list.get(random.nextInt(list.size()));
	}

	@SuppressWarnings("unchecked")
	public <T extends Config> T cast() {
		return (T)this;
	}
}
