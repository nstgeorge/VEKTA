package vekta;

import static processing.core.PApplet.pow;

public enum SkillLevel {
	FRESH("Fresh", 0),
	BEGINNER("Beginner", 1),
	ROOKIE("Rookie", 2),
	SEASONED("Seasoned", 3),
	EXPERT("Expert", 4),
	UNMATCHED("Unmatched", 5);

	public static SkillLevel fromSkill(float amount) {
		SkillLevel[] values = values();
		for(int i = values.length - 1; i >= 0; i--) {
			SkillLevel level = values[i];
			if(level.getThreshold() >= amount) {
				return level;
			}
		}
		return FRESH;
	}

	private final String name;
	private final float threshold;

	SkillLevel(String name, float order) {
		this.name = name;
		this.threshold = pow(10, order);
	}

	public String getName() {
		return name;
	}

	public float getThreshold() {
		return threshold;
	}
}
