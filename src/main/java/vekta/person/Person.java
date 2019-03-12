package vekta.person;

import vekta.Faction;
import vekta.Resources;
import vekta.Syncable;
import vekta.menu.option.DialogOption;
import vekta.mission.Mission;
import vekta.mission.MissionIssuer;
import vekta.object.SpaceObject;
import vekta.spawner.PersonGenerator;
import vekta.terrain.LandingSite;
import vekta.terrain.building.HouseBuilding;
import vekta.terrain.settlement.Settlement;

import java.util.HashMap;
import java.util.Map;

public class Person extends Syncable<Person> implements MissionIssuer {

	private final Map<Syncable, OpinionType> opinions = new HashMap<>();

	private final String name;

	private Faction faction;
	private String title;
	private Settlement home;

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

	@Override
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
		Dialog dialog = new Dialog(this, parts[0].trim());
		if(parts.length > 1) {
			for(int i = 1; i < parts.length; i++) {
				// Add custom response messages
				String response = parts[i].trim();
				if(response.startsWith(":")) {
					String[] args = response.split(" ", 2);
					Dialog next = createDialog(args[0].trim());
					dialog.add(new DialogOption(args[1].trim(), next));
					next.addContinuation(dialog); // TODO: inherit continuations rather than continue to `dialog` itself
				}
				else {
					dialog.addResponse(response);
				}
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

	//	@Override
	//	public void onSync(Person data) {
	//		this.faction = data.faction;
	//		this.title = data.title;
	//		//		this.home = register(data.home);
	//
	//		syncAll(opinions.keySet(), data.opinions.keySet());
	//	}
}
