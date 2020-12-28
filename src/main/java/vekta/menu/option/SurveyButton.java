package vekta.menu.option;

import vekta.context.KnowledgeContext;
import vekta.knowledge.Knowledge;
import vekta.knowledge.SpaceObjectKnowledge;
import vekta.menu.Menu;
import vekta.object.planet.TerrestrialPlanet;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static vekta.Vekta.getContext;
import static vekta.Vekta.setContext;

public class SurveyButton extends ButtonOption {
	private final TerrestrialPlanet planet;

	public SurveyButton(TerrestrialPlanet planet) {
		this.planet = planet;
	}

	@Override
	public String getName() {
		return "Survey";
	}

	@Override
	public void onSelect(Menu menu) {
		//		Menu sub = new Menu(menu, new SurveyMenuHandle(getTerrain()));
		//		sub.addDefault();
		//		setContext(sub);

		List<SpaceObjectKnowledge> items = menu.getPlayer()
				.findKnowledge(SpaceObjectKnowledge.class, k -> k.getSpaceObject() == planet).stream()
				.sorted(Comparator.comparing(Knowledge::getArchiveValue).reversed())
				.collect(Collectors.toList());

		setContext(new KnowledgeContext(getContext(), menu.getPlayer())
				.withTab(getName(), items));
	}
}
