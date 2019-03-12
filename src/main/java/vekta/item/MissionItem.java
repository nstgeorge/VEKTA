package vekta.item;

import vekta.Faction;
import vekta.Player;
import vekta.mission.Mission;
import vekta.mission.MissionIssuer;
import vekta.object.SpaceObject;
import vekta.object.planet.TerrestrialPlanet;
import vekta.spawner.FactionGenerator;

import java.io.Serializable;

import static vekta.Vekta.getWorld;

public class MissionItem extends Item implements MissionIssuer {
	private final MissionProvider provider;

	private boolean activated;
	private Faction faction;
	private SpaceObject home;

	public MissionItem(String name, Mission mission) {
		this(name, player -> mission);
	}
	
	public MissionItem(String name, MissionProvider provider) {
		super(name, ItemType.MISSION);

		this.provider = provider;
	}

	public Mission createMission(Player player) {
		activated = true;
		return provider.createMission(player);
	}

	@Override
	public void onAdd(Player player) {
		home = player.getShip();
		if(!activated) {
			createMission(player).start();
		}
	}

	@Override
	public Faction getFaction() {
		if(faction == null){
			faction = FactionGenerator.randomFaction();
		}
		return faction;
	}

	@Override
	public SpaceObject findHomeObject() {
		if(home == null) {
			home = getWorld().findRandomObject(TerrestrialPlanet.class);
		}
		return home;
	}

	public interface MissionProvider extends Serializable {
		Mission createMission(Player player);
	}
}
