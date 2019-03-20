package vekta.knowledge;

import static processing.core.PApplet.max;
import static processing.core.PApplet.min;

public enum KnowledgeLevel {
	AWARE,
	SCANNED,
	VISITED,
	OWNED;

	public boolean isBetter(KnowledgeLevel level) {
		return ordinal() > level.ordinal();
	}

	public boolean isAvailableFrom(KnowledgeLevel level) {
		return level == this || level.isBetter(this);
	}

	public KnowledgeLevel increased() {
		return KnowledgeLevel.values()[min(KnowledgeLevel.values().length - 1, ordinal() + 1)];
	}

	public KnowledgeLevel decreased() {
		return KnowledgeLevel.values()[max(0, ordinal() - 1)];
	}
}
