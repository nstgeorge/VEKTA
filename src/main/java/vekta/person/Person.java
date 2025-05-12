package vekta.person;

import vekta.Resources;
import vekta.detail.Detailed;
import vekta.detail.Details;
import vekta.faction.Faction;
import vekta.mission.Mission;
import vekta.mission.MissionIssuer;
import vekta.object.SpaceObject;
import vekta.person.personality.Personality;
import vekta.player.Player;
import vekta.spawner.PersonGenerator;
import vekta.sync.Sync;
import vekta.sync.Syncable;
import vekta.terrain.location.Location;
import vekta.terrain.settlement.Settlement;
import vekta.terrain.settlement.building.HouseBuilding;

import java.util.*;

import static vekta.Vekta.*;

public class Person extends Syncable<Person> implements MissionIssuer, Detailed {
	private final @Sync Map<Syncable, OpinionType> opinions = new HashMap<>();
	private final @Sync List<String> interests = new ArrayList<>();
	private final @Sync Details details = new Details();

	private final String name;

	private @Sync Faction faction;
	private @Sync String title;
	private Personality personality;
	private @Sync Settlement home;
	private @Sync boolean busy;

	private @Sync boolean dead;

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

	public Personality getPersonality() {
		return personality;
	}

	public void setPersonality(Personality personality) {
		if(this.personality != null) {
			throw new RuntimeException(this.getFullName() + " already has a personality: " + this.personality);
		}
		this.personality = personality;
		personality.preparePerson(this);
	}

	public boolean hasHome() {
		return home != null && home.isEconomyAlive() && home.getLocation().isEnabled();//TODO: must be habitable
	}

	public Settlement getCurrentHome() {
		return home;
	}

	public Settlement findHome() {
		if(!hasHome()) {
			home = PersonGenerator.randomHome(getFaction());
		}
		return getCurrentHome();
	}

	public Location findHomeLocation() {
		Settlement home = findHome();
		return home != null ? home.getLocation() : null;
	}

	@Override
	public SpaceObject findHomeObject() {
		Location location = findHomeLocation();
		return location != null ? location.getPlanet() : null;
	}

	public void setHome(Settlement home) {
		if(this.home == home) {
			return;
		}

		if(this.home != null) {
			// Remove previous house if exists
			this.home.remove(this.home.getParts().stream()
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
		return createDialog(type, Resources.generateString("dialog_" + type));
	}

	public Dialog createDialog(String type, String data) {
		if(isDead()) {
			type = "dead";
		}

		String[] parts = data.split("\\*");
		Personality personality = getPersonality();

		String text = parts[0].trim();

		Dialog dialog = new Dialog(type, this, text);

		if(personality != null) {
			if(!text.startsWith("|")) {
				text = personality.transformDialog(dialog, type, text);

				// Join and re-split responses
				parts[0] = text;
				parts = String.join("*", parts).split("\\*");
				text = parts[0];
				dialog.setMessage(text.trim());
			}
		}

		if(text.startsWith("!")) {
			// Angry message
			dialog.setColor(DANGER_COLOR);
			dialog.setMessage(text.substring(1).trim());
		}
		else if(text.startsWith("|")) {
			// Non-dialog message ("You notice something" or  other non-dialog message)
			dialog.setColor(v.color(100));
			dialog.setMessage(text.substring(1).trim());
		}

		for(int i = 1; i < parts.length; i++) {
			dialog.parseResponse(parts[i].trim());
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

	@Override
	public Details getDetails() {
		return details;
	}

	public Collection<String> getInterests() {
		return interests;
	}

	public boolean hasInterest(String interest) {
		return interests.contains(interest);
	}

	public void addInterest(String interest) {
		if(!hasInterest(interest)) {
			interests.add(interest);
			Collections.sort(interests);
		}
	}

	public void removeInterest(String interest) {
		interests.remove(interest);
	}

	public boolean isBusy() {
		return busy;
	}

	public void setBusy(boolean busy) {
		this.busy = busy;
	}

	public boolean isDead() {
		return dead;
	}

	public void die() {
		dead = true;
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
