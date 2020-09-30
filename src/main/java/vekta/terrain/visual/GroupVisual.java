package vekta.terrain.visual;

import java.util.ArrayList;
import java.util.List;

import static vekta.Vekta.v;

public class GroupVisual extends Visual {
	private final List<Visual> visuals = new ArrayList<>();

	public GroupVisual(float x, float y) {
		super(x, y);
	}

	public void add(Visual visual) {
		visuals.add(visual);
	}

	public List<Visual> getVisuals() {
		return visuals;
	}

	@Override
	public void draw() {
		v.pushMatrix();
		v.translate(getX(), getY());
		for(Visual v : getVisuals()) {
			v.draw();
		}
		v.popMatrix();
	}
}
