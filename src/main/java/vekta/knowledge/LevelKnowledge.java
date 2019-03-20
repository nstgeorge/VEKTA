package vekta.knowledge;

public abstract class LevelKnowledge implements Knowledge {
	private final KnowledgeLevel level;

	public LevelKnowledge(KnowledgeLevel level) {
		this.level = level;
	}

	public KnowledgeLevel getLevel() {
		return level;
	}

	public abstract boolean isSimilar(LevelKnowledge other);

	@Override
	public KnowledgeDelta getDelta(Knowledge other) {
		if(other instanceof LevelKnowledge && isSimilar((LevelKnowledge)other)) {
			// Compare based on knowledge levels
			KnowledgeLevel a = getLevel();
			KnowledgeLevel b = ((LevelKnowledge)other).getLevel();
			return a == b ? KnowledgeDelta.SAME
					: a.isBetter(b) ? KnowledgeDelta.BETTER
					: KnowledgeDelta.WORSE;
		}
		else {
			return KnowledgeDelta.DIFFERENT;
		}
	}
}
