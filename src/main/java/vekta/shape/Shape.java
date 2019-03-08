package vekta.shape;

import processing.core.PGraphics;

import java.util.ArrayList;
import java.util.List;

public class Shape {
	private final List<Shape> subShapes = new ArrayList<>();

	private final String name;

	public Shape(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void draw(PGraphics g) {
		
	}
}
