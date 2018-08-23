package com.processing.sketch;

class Projectile implements SpaceObject
{
    // Default settings
    private final double DEF_MASS = 1000;
    private final int DEF_RADIUS = 2;
    private final int DEF_SPEED = 7;

    private SpaceObject parent;
    private double mass;
    private float radius;
    private PVector position;
    private PVector velocity;
    private int c;

    /**
     * Default constructor for planets
     */

    public Projectile(PVector position, PVector heading, PVector velocity, int c)
    {
        // this.parent = parent;
        this.mass = DEF_MASS;
        this.radius = DEF_RADIUS;
        this.position = position;
        this.velocity = heading.setMag(DEF_SPEED).add(velocity);
        this.c = c;
    }

    // Draws the projectile
    void draw()
    {
        stroke(this.c);
        fill(0);
        ellipseMode(RADIUS);
        ellipse(position.x, position.y, radius, radius);
    }

    @Override
    PVector getInfluenceVector(SpaceObject s)
    {
        double r = PVector.dist(position, s.getPosition()) * SCALE;
        if (r == 0) return new PVector(0, 0); // If the planet being checked is itself (or directly on top), don't move
        double force = G * ((mass * s.getMass()) / (r * r)); // G defined in orbit
        PVector influence = new PVector(s.getPosition().x - position.x, s.getPosition().y - position.y).setMag((float) (force / mass));
        stroke(255, 0, 0);
        //line(Position.x, Position.y, PVector.add(Position, influence).x * 1.1, PVector.add(Position, influence).y * 1.1);
        addVelocity(influence);
        return influence;
    }

    void update()
    {
        position.add(velocity);
    }

    int getColor() { return c; }

    float getRadius() { return radius; }

    PVector getVelocity() { return velocity; }

    PVector getPosition() { return position; }

    double getMass() { return mass; }

    PVector addVelocity(PVector add)
    {
        return velocity.add(add);
    }

    boolean collidesWith(SpaceObject s)
    {
        double r = PVector.dist(position, s.getPosition());
        if (r < (getRadius() + s.getRadius())) return true;
        return false;
    }
}  