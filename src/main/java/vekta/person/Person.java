package vekta.person;

import vekta.Faction;
import vekta.Resources;
import vekta.Syncable;
import vekta.mission.Mission;
import vekta.mission.MissionListener;
import vekta.object.SpaceObject;
import vekta.spawner.PersonGenerator;
import vekta.terrain.LandingSite;
import vekta.terrain.building.HouseBuilding;
import vekta.terrain.settlement.Settlement;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static vekta.Vekta.randomID;
import static vekta.Vekta.register;

public class Person implements Serializable, MissionListener, Syncable<Person> {
	private final long id = randomID();

	private final Map<Syncable, OpinionType> opinions = new HashMap<>();

	private final String name;

	private Faction faction;
	private String title;
	private Settlement home;

	public Person(String name, Faction faction) {
		this.name = name;

		setFaction(faction);
	}

	public String getShortName() {
		return name;
	}

	public String getFullName() {
		return name + (title != null ? " " + title : "");
	}

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
	}

	public boolean hasHome() {
		return home != null && !home.getSite().getParent().isDestroyed();
	}

	public Settlement findHome() {
		if(!hasHome()) {
			home = PersonGenerator.randomHome();
		}
		return home;
	}

	public LandingSite findHomeSite() {
		Settlement home = findHome();
		return home != null ? home.getSite() : null;
	}

	public SpaceObject findHomeObject() {
		LandingSite site = findHomeSite();
		return site != null ? site.getParent() : null;
	}

	public void setHome(Settlement home) {
		if(hasHome()) {
			// Remove previous house if exists
			home.remove(home.getParts().stream()
					.filter(p -> p instanceof HouseBuilding && ((HouseBuilding)p).getPerson() == this)
					.findFirst().orElse(null));
		}
		this.home = home;
		home.add(new HouseBuilding(this));
		syncChanges();
	}

	public Dialog createDialog(String type) {
		String[] parts = Resources.generateString("dialog_" + type).split("\\*");
		Dialog dialog = new Dialog(type, parts[0].trim(), this);
		if(parts.length > 1) {
			for(int i = 1; i < parts.length; i++) {
				// Add custom response messages
				String response = parts[i].trim();
				dialog.addResponse(response);
			}
		}
		return dialog;
	}

	public OpinionType getOpinion(Faction faction) {
		return opinions.getOrDefault(faction, OpinionType.NEUTRAL);
	}

	public void setOpinion(Faction faction, OpinionType opinion) {
		opinions.put(faction, opinion);
		syncChanges();
	}

	@Override
	public void onCancel(Mission mission) {
		Faction faction = mission.getPlayer().getFaction();
		if(getOpinion(faction).isPositive()) {
			setOpinion(faction, OpinionType.NEUTRAL);
			syncChanges();
		}
	}

	@Override
	public void onComplete(Mission mission) {
		setOpinion(mission.getPlayer().getFaction(), OpinionType.GRATEFUL);
	}

	@Override
	public long getSyncID() {
		return id;
	}

	@Override
	public Person getSyncData() {
		return this;
	}

	@Override
	public void onSync(Person data) {
		this.faction = data.faction;
		this.title = data.title;
		//		this.home = register(data.home);

		register(opinions.keySet(), data.opinions.keySet());
	}
}
