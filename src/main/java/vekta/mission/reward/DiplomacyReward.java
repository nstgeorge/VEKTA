package vekta.mission.reward;

import vekta.faction.Faction;
import vekta.mission.Mission;
import vekta.player.Player;

public abstract class DiplomacyReward extends Reward {
	private final Faction a;
	private final Faction b;

	public DiplomacyReward(Faction faction) {
		this(faction, null);
	}

	public DiplomacyReward(Faction a, Faction b) {
		this.a = a;
		this.b = b;
	}

	@Override
	public String getName() {
		if(b != null) {
			return getTypeName() + " between " + a.getName() + " and " + b.getName();
		}
		return getTypeName() + " with " + a.getName();
	}

	@Override
	public int getColor() {
		return a.getColor();
	}

	@Override
	public String getDisplayText() {
		return "Expect: " + getName();
	}

	@Override
	public final void onReward(Mission mission, Player player) {
		onReward(mission, a, b != null ? b : player.getFaction());
	}

	public abstract String getTypeName();

	public abstract void onReward(Mission mission, Faction a, Faction b);
}
