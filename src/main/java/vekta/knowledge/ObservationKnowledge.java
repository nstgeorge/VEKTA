package vekta.knowledge;

public abstract class ObservationKnowledge implements Knowledge {
	private final ObservationLevel level;

	public ObservationKnowledge(ObservationLevel level) {
		this.level = level;
	}

	public ObservationLevel getLevel() {
		return level;
	}

	public abstract boolean isSimilar(ObservationKnowledge other);

	@Override
	public KnowledgeDelta getDelta(Knowledge other) {
		if(other instanceof ObservationKnowledge && isSimilar((ObservationKnowledge)other)) {
			// Compare based on knowledge levels
			ObservationLevel a = getLevel();
			ObservationLevel b = ((ObservationKnowledge)other).getLevel();
			return a == b ? KnowledgeDelta.SAME
					: a.isBetter(b) ? KnowledgeDelta.BETTER
					: KnowledgeDelta.WORSE;
		}
		else {
			return KnowledgeDelta.DIFFERENT;
		}
	}
}
