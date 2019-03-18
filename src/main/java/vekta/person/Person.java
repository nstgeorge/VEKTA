package vekta.person;

import vekta.*;
import vekta.mission.Mission;
import vekta.mission.MissionIssuer;
import vekta.object.SpaceObject;
import vekta.spawner.PersonGenerator;
import vekta.terrain.LandingSite;
import vekta.terrain.building.HouseBuilding;
import vekta.terrain.settlement.Settlement;

import java.util.HashMap;
import java.util.Map;

import static vekta.Vekta.getWorld;
import static vekta.Vekta.v;

public class Person extends Syncable<Person> implements MissionIssuer {

	private final Map<Syncable, OpinionType> opinions = new HashMap<>();

	private final String name;

	private @Sync Faction faction;
	private @Sync String title;
	private @Sync Settlement home;
	private @Sync boolean busy;

	private boolean dead;

	public Person(String name, Faction faction) {
		this.name = name;

		setFaction(faction);
	}

	@Override
	public String getName() {
		return name;
	}

	public String getFullName() {
		return name + (title != null ? " " + title : "");
	}

	@Override
	public Faction getFaction() {
		return faction;
	}

	public void setFaction(Faction faction) {
		if(faction == null) {
			throw new RuntimeException("Faction cannot be null");
		}
		this.faction = faction;
		syncChanges();
	}

	public int getColor() {
		return getFaction().getColor();
	}

	public void setTitle(String title) {
		this.title = title;
		syncChanges();
	}

	public boolean hasHome() {
		return home != null && !home.getSite().getParent().isDestroyed();
	}

	public Settlement findHome() {
		if(!hasHome()) {
			home = PersonGenerator.randomHome(getFaction());
		}
		return home;
	}

	public LandingSite findHomeSite() {
		Settlement home = findHome();
		return home != null ? home.getSite() : null;
	}

	@Override
	public SpaceObject findHomeObject() {
		LandingSite site = findHomeSite();
		return site != null ? site.getParent() : null;
	}

	public void setHome(Settlement home) {
		if(this.home == home) {
			return;
		}

		if(hasHome()) {
			// Remove previous house if exists
			home.remove(home.getParts().stream()
					.filter(p -> p instanceof HouseBuilding && ((HouseBuilding)p).getPerson() == this)
					.findFirst().orElse(null));
		}
		this.home = home;
		if(home != null) {
			home.add(new HouseBuilding(this));
		}
		syncChanges();
	}

	public Dialog createDialog(String type) {
		String[] parts = Resources.generateString("dialog_" + type).split("\\*");
		Dialog dialog = new Dialog(type, this, parts[0].trim());
		if(parts.length > 1) {
			for(int i = 1; i < parts.length; i++) {
				// Add custom response messages
				dialog.parseResponse(parts[i].trim());
			}
		}
		return dialog;
	}

	public OpinionType getOpinion(Faction faction) {
		OpinionType opinion = opinions.getOrDefault(faction, OpinionType.NEUTRAL);
		return getFaction().isAlly(faction)
				? opinion.upgraded()
				: getFaction().isEnemy(faction)
				? opinion.downgraded()
				: opinion;
	}

	public void setOpinion(Faction faction, OpinionType opinion) {
		opinions.put(faction, opinion);
		syncChanges();
	}

	public void upgradeOpinion(Faction faction) {
		setOpinion(faction, getOpinion(faction).upgraded());
		syncChanges();
	}

	public void downgradeOpinion(Faction faction) {
		setOpinion(faction, getOpinion(faction).downgraded());
		syncChanges();
	}

	public boolean isBusy() {
		return busy;
	}

	public boolean isDead() {
		return dead;
	}

	public void die() {
		this.dead = true;
		setHome(null);
		getWorld().remove(this);
		syncChanges();
	}

	@Override
	public int chooseMissionTier(Player player) {
		int tier = (int)v.random(2) + 1;
		if(getOpinion(player.getFaction()).isPositive()) {
			tier++; // Allow Tier III mission if positive opinion
			if(getFaction().isAlly(player.getFaction())) {
				tier++; // Allow Tier IV mission if factions are allied
				if(getOpinion(player.getFaction()) == OpinionType.GRATEFUL && v.chance(.5F)) {
					tier++; // Allow Tier V mission if very happy with the player
				}
			}
		}
		return tier;
	}

	@Override
	public void onStart(Mission mission) {
		busy = true;
		syncChanges();
	}

	@Override
	public void onCancel(Mission mission) {
		busy = false;
		Faction faction = mission.getPlayer().getFaction();
		if(!getOpinion(faction).isNegative()) {
			downgradeOpinion(faction);
			syncChanges();
		}
		syncChanges();
	}

	@Override
	public void onComplete(Mission mission) {
		busy = false;
		upgradeOpinion(mission.getPlayer().getFaction());
		syncChanges();
	}

	@Override
	public void onSync(Person data) {
		super.onSync(data);

		if(data.dead) {
			die();
		}
	}
}
