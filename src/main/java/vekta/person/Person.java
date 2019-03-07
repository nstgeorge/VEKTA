package vekta.person;

import vekta.object.SpaceObject;
import vekta.terrain.HabitableTerrain;
import vekta.terrain.LandingSite;
import vekta.terrain.Terrain;
import vekta.terrain.settlement.Settlement;

public class Person {
	private final String name;
	private final int color;
	
	private String title;
	private LandingSite home;

	public Person(String name, int color) {
		this.name = name;
		this.color = color;
	}

	public int getColor() {
		return color;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public LandingSite getHome() {
		return home;
	}

	public SpaceObject getHomeObject() {
		return getHome() != null ? getHome().getParent() : null;
	}

	public Settlement getHomeSettlement() {
		if(getHome() != null) {
			Terrain terrain = getHome().getTerrain();
			if(terrain instanceof HabitableTerrain) {
				return ((HabitableTerrain)terrain).getSettlement();
			}
		}
		return null;
	}

	public void setHome(LandingSite home) {
		this.home = home;
	}

	public String getDisplayName() {
		return name + (title != null ? " " + title : "");
	}
}
