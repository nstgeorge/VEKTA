package vekta.person;

import vekta.Faction;
import vekta.Player;
import vekta.Resources;
import vekta.mission.Mission;
import vekta.mission.MissionListener;
import vekta.object.SpaceObject;
import vekta.spawner.PersonGenerator;
import vekta.terrain.LandingSite;
import vekta.terrain.building.HouseBuilding;
import vekta.terrain.settlement.Settlement;

import java.util.HashMap;
import java.util.Map;

public class Person implements MissionListener {
	private final Map<Object, OpinionType> opinions = new HashMap<>();

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
	}

	public int getColor() {
		return getFaction().getColor();
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean hasHome() {
		return home != null && !findHome().getSite().getParent().isDestroyed();
	}

	public Settlement findHome() {
		if(!hasHome()) {
			home = PersonGenerator.randomHome(this);
		}
		return home;
	}

	public LandingSite findHomeSite() {
		return hasHome() ? findHome().getSite() : null;
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
	}

	public Dialog createDialog(String type) {
		String[] parts = Resources.generateString("dialog_" + type).split("\\*");
		Dialog dialog = new Dialog(type, parts[0].trim(), this);
		if(parts.length > 1) {
			for(int i = 1; i < parts.length; i++) {
				// Add custom response messages
				dialog.addResponse(parts[i].trim());
			}
		}
		return dialog;
	}

	public OpinionType getOpinion(Player player) {
		return opinions.getOrDefault(player, OpinionType.NEUTRAL);
	}

	public void setOpinion(Player player, OpinionType opinion) {
		opinions.put(player, opinion);
	}

	@Override
	public void onCancel(Mission mission) {
		if(getOpinion(mission.getPlayer()).isPositive()) {
			setOpinion(mission.getPlayer(), OpinionType.NEUTRAL);
		}
	}

	@Override
	public void onComplete(Mission mission) {
		setOpinion(mission.getPlayer(), OpinionType.GRATEFUL);
	}
}
