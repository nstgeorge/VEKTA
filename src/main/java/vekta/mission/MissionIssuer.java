package vekta.mission;

import vekta.Faction;
import vekta.Player;
import vekta.object.SpaceObject;

import java.io.Serializable;

public interface MissionIssuer extends MissionListener, Serializable {
	String getName();

	Faction getFaction();
	
	SpaceObject findHomeObject();
	
	int chooseMissionTier(Player player);
}
