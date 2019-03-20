package vekta.display;

import java.io.Serializable;

public interface Display extends Serializable {
	
	default float getWidth(float width, float height) {
		return width;
	}

	default float getHeight(float width, float height) {
		return height;
	}

	void draw(float width, float height);
}
