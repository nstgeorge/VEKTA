package vekta.module;

import processing.core.PVector;
import vekta.InfoGroup;
import vekta.KeyBinding;
import vekta.menu.Menu;
import vekta.menu.handle.SurveyMenuHandle;
import vekta.menu.option.BackButton;
import vekta.object.SpaceObject;
import vekta.object.Targeter;
import vekta.object.planet.TerrestrialPlanet;

import static processing.core.PApplet.round;
import static vekta.Vekta.*;

public class TelescopeModule extends ShipModule {
	private static final float RANGE_SCALE = AU_DISTANCE;

	private final float resolution;

	public TelescopeModule() {
		this(1);
	}

	public TelescopeModule(float resolution) {
		this.resolution = resolution;
	}

	public float getResolution() {
		return resolution;
	}

	@Override
	public String getName() {
		return "Survey Telescope v" + getResolution();
	}

	@Override
	public ModuleType getType() {
		return ModuleType.TELESCOPE;
	}

	@Override
	public int getMass() {
		return (int)((getResolution() + 5) * 200);
	}

	@Override
	public float getValueScale() {
		return 1 * getResolution();
	}

	@Override
	public boolean isBetter(Module other) {
		return other instanceof TelescopeModule && getResolution() > ((TelescopeModule)other).getResolution();
	}

	@Override
	public Module createVariant() {
		return new TelescopeModule(chooseInclusive(.1F, 3, .1F));
	}

	@Override
	public void onKeyPress(KeyBinding key) {
		if(key == KeyBinding.SHIP_SCAN) {
			Targeter t = (Targeter)getShip().getModule(ModuleType.NAVIGATION);
			SpaceObject target = t != null ? t.getTarget() : getWorld().findOrbitObject(getShip());
			if(target instanceof TerrestrialPlanet) {
				TerrestrialPlanet planet = (TerrestrialPlanet)target;
				float dist = PVector.dist(getShip().getPosition(), planet.getPosition());
				float maxDist = getResolution() * RANGE_SCALE;
				if(dist <= maxDist) {
					Menu menu = new Menu(getShip().getController(), new BackButton(getWorld()), new SurveyMenuHandle(planet.getLandingSite()));
					menu.addDefault();
					setContext(menu);
				}
				else if(getShip().hasController()) {
					getShip().getController().send("Out of range! (" + round(maxDist / dist * 100) + "% resolution)")
							.withTime(.5F);
				}
			}
		}
	}

	@Override
	public void onInfo(InfoGroup info) {
		info.addDescription("The tool of choice for explorers, surveyors, and data collectors.");

		info.addKey(KeyBinding.SHIP_SCAN, "survey target");
	}
}
