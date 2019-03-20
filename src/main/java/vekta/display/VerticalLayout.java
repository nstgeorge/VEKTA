package vekta.display;

import static vekta.Vekta.v;

public class VerticalLayout extends Layout {
	public VerticalLayout(DisplayStyle style) {
		super(style);
	}

	@Override
	public float getWidth(float width, float height) {
		float max = 0;
		for(Display item : getItems()) {
			float w = item.getWidth(width, height);
			if(w > max) {
				max = w;
			}
		}
		return max;
	}

	@Override
	public float getHeight(float width, float height) {
		float sum = 0;
		for(Display item : getItems()) {
			sum += item.getHeight(width, height);
		}
		return sum + getStyle().spacing() * (getItems().size() - 1);
	}

	@Override
	public void draw(float width, float height) {
		v.pushMatrix();
		for(Display item : getItems()) {
			//			if(height <= 0) {
			//				break; // Don't draw beyond overflowing item
			//			}

			// Draw layout item
			item.draw(width, height);

			// Compute offset for next element
			float y = item.getHeight(width, height) + getStyle().spacing();
			v.translate(0, y);
			height -= y;
		}
		v.popMatrix();
	}
}
