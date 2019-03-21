package vekta.display;

import java.io.Serializable;

import static vekta.Vekta.v;

public interface Display extends Serializable {

	default float getWidth(float width, float height) {
		return width;
	}

	default float getHeight(float width, float height) {
		return height;
	}

	void draw(float width, float height);

	default void draw(float x, float y, float width, float height) {
		v.pushMatrix();
		v.translate(x, y);
		draw(width, height);
		v.popMatrix();
	}

	default void drawCentered(float x, float y, float width, float height) {
		draw(x - width / 2, y - height / 2, width, height);
	}
}
