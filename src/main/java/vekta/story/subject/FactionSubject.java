package vekta.story.subject;

import vekta.faction.Faction;

public class FactionSubject implements StorySubject {
	private final Faction faction;

	public FactionSubject(Faction faction) {
		this.faction = faction;
	}

	public Faction getFaction() {
		return faction;
	}

	@Override
	public String getFullName() {
		return getFaction().getName();
	}
}
