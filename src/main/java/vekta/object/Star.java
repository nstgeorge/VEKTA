package vekta.object;

import processing.core.PVector;
import vekta.Vekta;

import static vekta.Vekta.getInstance;

public class Star extends Planet {

    private Vekta v;

    public Star(float mass, float density, PVector position, PVector velocity, int color) {
        super(mass, density, false, position, velocity, color);
        v = getInstance();
    }

    public void draw() {
        drawRadialGradient(position, super.getColor(), v.color(0,0,0), super.getRadius(), super.getRadius() * 1.5F);
        super.draw();
    }

    // Draws radial gradient. This abstraction isn't necessary, but it helps readability
    private void drawRadialGradient(PVector position, int colorFrom, int colorTo, float innerRadius, float outerRadius) {
        int color;
        for(float i = outerRadius; i >= innerRadius; i -= 1) {
            color = v.lerpColor(colorFrom, colorTo, (i - innerRadius) / (outerRadius - innerRadius));
            v.stroke(color);
            v.fill(color);
            v.ellipse(position.x, position.y, i, i);
        }
    }
}
