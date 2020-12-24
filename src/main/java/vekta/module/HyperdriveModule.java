package vekta.module;

import vekta.KeyBinding;
import vekta.Resources;
import vekta.menu.Menu;
import vekta.object.HyperdriveShockwave;
import vekta.object.ship.ModularShip;
import vekta.player.Player;
import vekta.util.InfoGroup;
import vekta.world.RenderLevel;
import vekta.world.World;
import vekta.world.ZoomController;

import static vekta.Vekta.*;

public class HyperdriveModule extends ShipModule implements ZoomController {
	private static final float ENERGY_CONSUME_SCALE = .1F;
	private static final float LOW_TIME_SCALE_SPEEDUP = 50;
	private static final float THRUST_SCALE = .1F;
	private static final float MIN_BOOST = 10;
	private static final float MAX_BOOST = 1000;

	private final float boost;

	private float currentBoost;

	public HyperdriveModule() {
		this(1);
	}

	public HyperdriveModule(float boost) {
		this.boost = boost;
	}

	public float getBoost() {
		return boost;
	}

	public boolean isActive() {
		return getShip().isHyperdriving();
	}

	@Override
	public String getName() {
		return "Hyperdrive v" + getBoost();
	}

	@Override
	public ModuleType getType() {
		return ModuleType.HYPERDRIVE;
	}

	@Override
	public int getMass() {
		return (int)((getBoost() + 2) * 500);
	}

	@Override
	public float getValueScale() {
		return 10 * getBoost();
	}

	@Override
	public boolean isBetter(Module other) {
		return other instanceof HyperdriveModule && getBoost() > ((HyperdriveModule)other).getBoost();
	}

	@Override
	public Module createVariant() {
		return new HyperdriveModule(chooseInclusive(.1F, 1, .1F));
	}

	@Override
	public void onUninstall() {
		endHyperdrive();
	}

	@Override
	public void onUpdate() {
		if(isActive()) {
			World world = getWorld();
			ModularShip ship = getShip();

			float zoom = world.getZoom();
			float timeScale = world.getTimeScale();
			float thrust = ship.isLanding() ? -1 : ship.getThrustControl();
			currentBoost = max(0, min(MAX_BOOST, sqrt(abs(ship.getVelocity().dot(ship.getHeading())))));

			float effectiveBoost = currentBoost * getBoost() * (zoom / INTERSTELLAR_LEVEL);

			boolean movingFast = ship.getVelocity().magSq() >= sq(MIN_BOOST * timeScale);
			if((!movingFast && thrust < 0) || !ship.hasEnergy()) {
				endHyperdrive();
			}

			if(ship.consumeEnergyOverTime(ENERGY_CONSUME_SCALE * effectiveBoost * PER_MINUTE)) {
				float effectiveThrust = thrust * max(timeScale, LOW_TIME_SCALE_SPEEDUP) / timeScale;
				ship.setVelocity(ship.getHeading().setMag(min(effectiveBoost * timeScale, ship.getVelocityReference().mag())));
				ship.accelerate(effectiveThrust * effectiveBoost * THRUST_SCALE, ship.getVelocityReference());

				// Create shockwave effect
				float speed = (RenderLevel.SHIP.isVisibleTo(world.getRenderLevel()) ? zoom : timeScale * 5e-5F) * effectiveBoost;
				register(new HyperdriveShockwave(ship, speed, (int)v.random(20, 25), ship.getColor()));
			}
		}
	}

	@Override
	public void onKeyPress(KeyBinding key) {
		if(key == KeyBinding.SHIP_HYPERDRIVE) {
			if(!isActive()) {
				if(getShip().getThrustControl() > 0 && !RenderLevel.SHIP.isVisibleTo(getWorld().getRenderLevel())) {
					startHyperdrive();
				}
				else {
					Resources.playSound("hyperdriveSputter");
				}
			}
			else {
				getShip().setLanding(true);
				//				endHyperdrive();
			}
		}
	}

	public void startHyperdrive() {
		if(!isActive()) {
			getShip().setHyperdriving(true);
			getShip().setLanding(false);
			Resources.playSound("hyperdriveHit");
			Resources.loopSound("hyperdriveLoop"/*, true*/);
			getWorld().addZoomController(this);

			Resources.stopSound("engine");////
			getWorld().setZoom(STAR_LEVEL);//////
		}
	}

	public void endHyperdrive() {
		if(isActive()) {
			getShip().setHyperdriving(false);
			currentBoost = 0;
			Resources.stopSound("hyperdriveLoop");
			Resources.playSound("hyperdriveEnd");

			if(getWorld().getZoom() > STAR_LEVEL) {
				getWorld().setZoom(STAR_LEVEL);//////
			}
		}
	}

	@Override
	public void onMenu(Menu menu) {
		//			endHyperdrive();

		Resources.stopSound("hyperdriveLoop");///TODO re-enable sound on close menu
	}

	@Override
	public void onInfo(InfoGroup info) {
		info.addDescription("At long last, scientists have found an efficient way to distort the fabric of space-time with standard-grade spacecraft energy. Just make sure not to run out of charge while in hyperspace...");

		info.addKey(KeyBinding.SHIP_HYPERDRIVE, "start hyperdrive");
	}

	@Override
	public boolean shouldCancelZoomControl(Player player) {
		return !isActive();
	}

	@Override
	public float controlZoom(Player player, float zoom) {
		return zoom > SHIP_LEVEL ? INTERSTELLAR_LEVEL : zoom;
		//		return zoom > SHIP_LEVEL ? max(STAR_LEVEL, zoom) : zoom;
	}
}
