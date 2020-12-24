package vekta.terrain.location;

import vekta.menu.Menu;
import vekta.menu.handle.LocationMenuHandle;
import vekta.menu.handle.MenuHandle;
import vekta.menu.option.*;
import vekta.object.planet.TerrestrialPlanet;
import vekta.player.Player;
import vekta.sync.Sync;
import vekta.sync.Syncable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static vekta.Vekta.getContext;
import static vekta.Vekta.setContext;

public abstract class Location implements Serializable {

	private final TerrestrialPlanet planet;

	private final List<Pathway> pathways = new ArrayList<>();

	protected Location(TerrestrialPlanet planet) {
		this.planet = planet;
	}

	public TerrestrialPlanet getPlanet() {
		return planet;
	}

	public List<Pathway> getEnabledPathways() {
		return pathways.stream()
				.filter(pathway -> pathway.getLocation().isEnabled())
				.collect(Collectors.toList());
	}

	public List<Pathway> getVisitablePathways(Player player) {
		return pathways.stream()
				.filter(pathway -> pathway.getLocation().isEnabled() && pathway.getLocation().isVisitable(player))
				.collect(Collectors.toList());
	}

	public void addPathway(Location location) {
		addPathway(location, null);
	}

	public void addPathway(Location location, String name) {
		pathways.add(location.new Pathway(name));
	}

	public void removePathways(Location location) {
		pathways.removeIf(pathway -> pathway.getLocation() == location);
	}

	//	public Pathway pathway(String name) {
	//		return new Pathway(name);
	//	}
	//
	//	public Pathway pathway() {
	//		return pathway(null);
	//	}

	public abstract String getName();

	public abstract String getOverview();

	public int getColor() {
		return getPlanet().getColor();
	}

	public boolean isEnabled() {
		return true;
	}

	public boolean isVisitable(Player player) {
		return true;
	}

	public final Set<String> findSurveyTags() {
		Set<String> tags = new HashSet<>();
		addSurveyTagsRecursive(tags);
		return tags;
	}

	protected final void addSurveyTagsRecursive(Set<String> tags) {
		onSurveyTags(tags);
		for(Pathway pathway : getEnabledPathways()) {
			pathway.getLocation().addSurveyTagsRecursive(tags);
		}
	}

	protected void onSurveyTags(Set<String> tags) {
	}

	public final void openMenu(Player player, MenuOption back) {

		Menu menu = new Menu(player, back, createMenuHandle());

		onVisitMenu(menu);

		for(Pathway pathway : getVisitablePathways(player)) {
			menu.add(new PathwayButton(pathway));
		}

		menu.addDefault();
		setContext(menu);
	}

	protected MenuHandle createMenuHandle() {
		return new LocationMenuHandle(this);
	}

	public void onVisitMenu(Menu menu) {
	}

	public class Pathway implements Serializable {

		private String name;

		public Pathway(String name) {
			setName(name);
		}

		public String getName() {
			return name != null ? name : getLocation().getName();
		}

		public void setName(String name) {
			this.name = name;
		}

		public Location getLocation() {
			return Location.this;
		}

		public void travel(Player player) {
			getLocation().openMenu(player, new BackButton(getContext()));
		}
	}

}
