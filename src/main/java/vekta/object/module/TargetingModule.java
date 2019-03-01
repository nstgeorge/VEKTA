package vekta.object.module;

import vekta.object.Planet;
import vekta.object.Ship;
import vekta.object.SpaceObject;
import vekta.object.Targeter;

public class TargetingModule implements Module, Targeter {

    TARGETING_MODE targetMode;

    SpaceObject target;
    Ship parent;

    public enum TARGETING_MODE {
        NEAREST_PLANET, NEAREST_SHIP
    }

    public TargetingModule(TARGETING_MODE mode, Ship parent) {
        targetMode = mode;
        this.parent = parent;
    }

    public void setMode(TARGETING_MODE mode) {
        targetMode = mode;
    }

    @Override
    public SpaceObject getTarget() {
        return target;
    }

    @Override
    public void setTarget(SpaceObject target) {
        this.target = target;
    }

    @Override
    public boolean isValidTarget(SpaceObject obj) {
        switch(targetMode) {
            case NEAREST_PLANET:
                return obj instanceof Planet;
            case NEAREST_SHIP:
                return obj instanceof Ship && obj != parent;
            default:
                return false;
        }
    }

    @Override
    public String getName() {
        return "Targeting Computer";
    }

    @Override
    public ModuleType getType() {
        return ModuleType.TARGETING_COMPUTER;
    }

    @Override
    public boolean isBetter(Module other) {
        return false;
    }
}
