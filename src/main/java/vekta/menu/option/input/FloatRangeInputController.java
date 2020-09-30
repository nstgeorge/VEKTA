package vekta.menu.option.input;

import vekta.menu.Menu;

import static processing.core.PApplet.max;
import static processing.core.PApplet.min;
import static vekta.Vekta.roundEpsilon;

public class FloatRangeInputController implements InputController<Float> {
	private final float min, max, step;

	public FloatRangeInputController(float min, float max, float step) {
		this.min = min;
		this.max = max;
		this.step = step;
	}

	public float getMin() {
		return min;
	}

	public float getMax() {
		return max;
	}

	public float getStep() {
		return step;
	}

	@Override
	public String getName(Float value) {
		return String.valueOf(value);
	}

	@Override
	public boolean hasLeft(Float value) {
		return value > min;
	}

	@Override
	public boolean hasRight(Float value) {
		return value < max;
	}

	@Override
	public Float getLeft(Float value) {
		return max(min, roundEpsilon(value - step));
	}

	@Override
	public Float getRight(Float value) {
		return min(max, roundEpsilon(value + step));
	}

	@Override
	public String getSelectVerb() {
		return null;
	}

	@Override
	public void select(Menu menu, InputWatcher<Float> watcher) {
	}
}
