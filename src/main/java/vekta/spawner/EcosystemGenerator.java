package vekta.spawner;

import vekta.Resources;
import vekta.ecosystem.Ecosystem;
import vekta.ecosystem.Species;

import static processing.core.PApplet.ceil;
import static processing.core.PApplet.sq;
import static vekta.Vekta.v;

public class EcosystemGenerator {
	private static final EcosystemSpawner[] SPAWNERS = Resources.findSubclassInstances(EcosystemSpawner.class);

	public static Species randomSpecies(Ecosystem ecosystem) {
		Species species = ecosystem.getSpecies().isEmpty() || v.chance(.1F / ecosystem.getSpecies().size()) ? null : v.random(ecosystem.getSpecies());
		if(species == null) {
			species = introduceSpecies(ecosystem);
		}
		return species;
	}

	public static Species introduceSpecies(Ecosystem ecosystem) {
		boolean inherit = !ecosystem.getSpecies().isEmpty() && !v.chance(.5F / ecosystem.getSpecies().size());
		Species species = createSpecies(inherit ? v.random(ecosystem.getSpecies()) : null);
		if(species.getNaturalGrowth() < 0) {
			for(Species other : ecosystem.getSpecies()) {
				if(other.getNaturalGrowth() > 0 && v.chance(2F / ecosystem.getSpecies().size())) {
					species.setFoodSourceWeight(other, -species.getNaturalGrowth() * v.random(.1F, 1.5F));
				}
			}
		}
		ecosystem.add(species, ceil(1 + sq(v.random(.5F)) * ecosystem.getCapacity() / species.getMass()));
		return species;
	}

	public static Species createSpecies(Species parent) {
		Species species = new Species(parent, Resources.generateString("species"));
		species.setNaturalGrowth(parent != null ? parent.getNaturalGrowth() * v.random(.8F, 1.2F) : v.random(-1, 1));
		species.setMass(parent != null ? parent.getMass() * v.random(.8F, 1.2F) : sq(v.random(.2F, 3F)));
		return species;
	}

	public static void populateEcosystem(Ecosystem ecosystem, int newSpecies) {
		for(int i = 0; i < newSpecies; i++) {
			introduceSpecies(ecosystem);

			//			EcosystemSpawner spawner = Weighted.random(SPAWNERS);
			//			spawner.spawn(ecosystem);
		}
	}

	public interface EcosystemSpawner extends Weighted {
		void spawn(Ecosystem ecosystem);
	}

}

