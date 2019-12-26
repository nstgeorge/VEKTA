package vekta.item;

import vekta.Faction;
import vekta.Player;
import vekta.mission.Mission;
import vekta.mission.MissionIssuer;
import vekta.mission.objective.KeepItemObjective;
import vekta.mission.objective.Objective;
import vekta.object.SpaceObject;
import vekta.object.planet.TerrestrialPlanet;
import vekta.spawner.FactionGenerator;

import java.io.Serializable;

import static vekta.Vekta.getWorld;
import static vekta.Vekta.v;

public class MissionItem extends Item implements MissionIssuer {
	private final String name;
	private final MissionProvider provider;

	private boolean created;
	private Faction faction;
	private SpaceObject home;

	public MissionItem(String name, Mission mission) {
		this(name, player -> mission);
	}

	public MissionItem(String name, MissionProvider provider) {
		this.name = name;
		this.provider = provider;
	}

	@Override
	public int getMass() {
		return 10;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public ItemType getType() {
		return ItemType.MISSION;
	}

	public Mission createMission(Player player) {
		created = true;
		Mission mission = provider.provide(player);
		Objective objective = new KeepItemObjective(this);
		if(v.chance(.1F)) {
			objective.optional();
		}
		mission.add(objective);
		return mission;
	}

	@Override
	public Faction getFaction() {
		if(faction == null) {
			faction = FactionGenerator.randomFaction();
		}
		return faction;
	}

	@Override
	public void onAdd(Player player) {
		home = player.getShip();
		if(!created) {
			createMission(player).start();
		}
	}

	@Override
	public SpaceObject findHomeObject() {
		if(home == null) {
			home = getWorld().findRandomObject(TerrestrialPlanet.class);
		}
		return home;
	}

	@Override
	public int chooseMissionTier(Player player) {
		return (int)v.random(2) + 1;
	}

	public interface MissionProvider extends Serializable {
		Mission provide(Player player);
	}
}
