package vekta.mission;

import vekta.faction.Faction;
import vekta.object.SpaceObject;
import vekta.player.Player;

import java.io.Serializable;

public interface MissionIssuer extends MissionListener, Serializable {
	String getName();

	Faction getFaction();
	
	SpaceObject findHomeObject();
	
	int chooseMissionTier(Player player);
}
