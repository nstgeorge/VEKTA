package vekta.logic;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static vekta.Vekta.v;

public class Logic<L extends Logic<L, T, R>, T, R> implements Serializable {

	public L[] first;
	public L[] random;

	public boolean hasValue;
	public R value;

	public Logic() {
	}

	public Logic(R value) {
		this.value = value;
	}

	public boolean hasValue(T input) {
		return hasValue;
	}

	public R getValue(T input) {
		if(first != null) {
			for(L log : first) {
				if(log.hasValue(input)) {
					return log.getValue(input);
				}
			}
		}
		if(random != null) {
			List<L> options = Arrays.stream(random)
					.filter(log -> log.hasValue(input))
					.collect(Collectors.toList());

			if(!options.isEmpty()) {
				return v.random(options).getValue(input);
			}
		}
		return value;
	}

	public boolean asBoolean(T input) {
		if(!hasValue(input)) {
			return false;
		}
		R result = getValue(input);
		return result != null && result != Boolean.valueOf(false);
	}
}
