package vekta.spawner;

import java.io.Serializable;

import static vekta.Vekta.v;

public interface Weighted extends Serializable {

	// Calculate random spawner based on weights
	static <T extends Weighted> T random(T[] spawners) {
		float total = 0;
		for(Weighted spawner : spawners) {
			total += spawner.getWeight();
		}
		int index = -1;
		float random = v.random(total);
		for(int i = 0; i < spawners.length; i++) {
			random -= spawners[i].getWeight();
			if(random <= 0) {
				index = i;
				break;
			}
		}
		return spawners[index];
	}

	default float getWeight() {
		return 1;
	}
}
