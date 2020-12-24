package vekta.terrain.location;

import vekta.Resources;
import vekta.knowledge.ObservationLevel;
import vekta.menu.Menu;
import vekta.menu.handle.LocationMenuHandle;
import vekta.menu.handle.MenuHandle;
import vekta.menu.option.BackButton;
import vekta.menu.option.MenuOption;
import vekta.menu.option.PathwayButton;
import vekta.menu.option.SettlementButton;
import vekta.object.planet.TerrestrialPlanet;
import vekta.player.Player;
import vekta.player.PlayerEvent;
import vekta.sound.Tune;
import vekta.spawner.WorldGenerator;
import vekta.terrain.settlement.Settlement;

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

	/// Monolithic features for now
	private Tune tune;
	private String music;
	private String wittyText = Resources.generateString("witty");
	private int color = WorldGenerator.randomPlanetColor();

	private Settlement settlement;

	public Location(TerrestrialPlanet planet) {
		this.planet = planet;
	}

	public TerrestrialPlanet getPlanet() {
		return planet;
	}

	public Tune getTune() {
		return tune;
	}

	public void setTune(Tune tune) {
		this.tune = tune;
	}

	public String getMusic() {
		return music;
	}

	public void setMusic(String music) {
		this.music = music;
	}

	public String getWittyText() {
		return wittyText;
	}

	public void setWittyText(String wittyText) {
		this.wittyText = wittyText;
	}

	public abstract String getName();

	public String getFullName() {
		return getName() + " (" + getPlanet() + ")";
	}

	public abstract String getOverview();

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public boolean hasSettlement() {
		return settlement != null;
	}

	public Settlement getSettlement() {
		return settlement;
	}

	public void notifySettlement(Settlement settlement) {
		if(hasSettlement() && getSettlement() != settlement) {
			throw new RuntimeException("Location already has a different settlement: " + this.settlement);
		}
		this.settlement = settlement;
	}

	/**
	 * @return whether the `Location` can exist under the current conditions.
	 */
	public boolean isEnabled() {
		return true;
	}

	/**
	 * @return whether the `Location` is capable of being visited under normal conditions.
	 */
	public boolean isVisitable() {
		return true;
	}

	/**
	 * @return whether the `Location` can be colonized.
	 */
	public boolean isHabitable() {
		return false;
	}

	/**
	 * @return whether the `Location` currently supports inhabitants.
	 */
	public boolean isInhabited() {
		return hasSettlement() && getSettlement().isInhabited();
	}

	public List<Pathway> findEnabledPathways() {
		return pathways.stream()
				.filter(pathway -> pathway.getLocation().isEnabled())
				.collect(Collectors.toList());
	}

	public List<Pathway> findVisitablePathways() {
		return pathways.stream()
				.filter(pathway -> pathway.getLocation().isEnabled() && pathway.getLocation().isVisitable())
				.collect(Collectors.toList());
	}

	public void addPathway(Location location) {
		addPathway(location, null);
	}

	public void addPathway(Location location, String name) {
		pathways.add(location.new Pathway(name));
		onAddPathway(location, name);
	}

	protected void onAddPathway(Location location, String name) {
	}

	//	public void removePathways(Location location) {
	//		pathways.removeIf(pathway -> pathway.getLocation() == location);
	//	}

	public final Set<String> findSurveyTags() {
		Set<String> tags = new HashSet<>();
		addSurveyTagsRecursive(tags);
		return tags;
	}

	protected final void addSurveyTagsRecursive(Set<String> tags) {
		onSurveyTags(tags);
		for(Pathway pathway : findEnabledPathways()) {
			pathway.getLocation().addSurveyTagsRecursive(tags);
		}
	}

	protected void onSurveyTags(Set<String> tags) {
	}

	public final void openMenu(Player player, MenuOption back) {

		Menu menu = new Menu(player, back, chooseMenuHandle());

		onSetupMenu(menu);

		if(hasSettlement()) {
			menu.add(new SettlementButton(getSettlement()));
		}

		for(Pathway pathway : findVisitablePathways()) {
			menu.add(new PathwayButton(pathway));
		}

		menu.addDefault();
		setContext(menu);

		player.emit(PlayerEvent.VISIT, this);
		getPlanet().observe(ObservationLevel.VISITED, player);
	}

	protected MenuHandle chooseMenuHandle() {
		return new LocationMenuHandle(this);
	}

	protected void onSetupMenu(Menu menu) {
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
