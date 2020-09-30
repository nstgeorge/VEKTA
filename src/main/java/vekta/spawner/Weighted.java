package vekta.spawner;

import java.util.Arrays;
import java.util.List;

import static vekta.Vekta.v;

public interface Weighted {

	// Calculate random spawner based on weights
	static <T extends Weighted> T random(List<T> spawners) {
		float total = 0;
		for(Weighted spawner : spawners) {
			total += spawner.getWeight();
		}
		int index = -1;
		float random = v.random(total);
		for(int i = 0; i < spawners.size(); i++) {
			random -= spawners.get(i).getWeight();
			if(random <= 0) {
				index = i;
				break;
			}
		}
		return spawners.get(index);
	}

	static <T extends Weighted> T random(T[] spawners) {
		return random(Arrays.asList(spawners));
	}

	default float getWeight() {
		return 1;
	}
}
