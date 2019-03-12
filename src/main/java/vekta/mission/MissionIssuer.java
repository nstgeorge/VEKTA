package vekta.mission;

import vekta.Faction;
import vekta.object.SpaceObject;

import java.io.Serializable;

public interface MissionIssuer extends MissionListener, Serializable {
	String getName();

	Faction getFaction();
	
	SpaceObject findHomeObject();
}
