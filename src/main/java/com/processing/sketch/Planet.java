package com.processing.sketch;

/**
 * Model for a planet.
 */
class Planet implements SpaceObject
{
    // Default Planet settings
    private final double DEF_MASS = 1.989 * Math.pow(10, 30);
    private final int DEF_RADIUS = 15;
    private final PVector DEF_POSITION = new PVector(100, 100);
    private final PVector DEF_VELOCITY = new PVector(0, 0);
    private final int DEF_COLOR = color(255, 255, 255);

    private double mass;
    private float radius;
    private PVector position;
    private PVector velocity;
    private int c;

    /**
     * Default constructor for planets
     */
    public Planet()
    {
        mass = DEF_MASS;
        radius = DEF_RADIUS;
        position = DEF_POSITION;
        velocity = DEF_VELOCITY;
        c = DEF_COLOR;
    }

    public Planet(double mass, float radius, int x, int y, float xVelocity, float yVelocity, int c)
    {
        this.mass = mass;
        this.radius = radius;
        position = new PVector(x, y);
        velocity = new PVector(xVelocity, yVelocity);
        this.c = c;
    }

    // Draws the planet
    void draw()
    {
        stroke(this.c);
        fill(0);
        ellipseMode(RADIUS);

        ellipse(position.x, position.y, radius, radius);
    }

    void update()
    {
        position.add(velocity);
    }

    /**
     * Calculates influence of this Planet *from* another object in space.
     */
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

    /**
     * Checks if this planet collides with another planet
     */
    boolean collidesWith(SpaceObject s)
    {
        double r = PVector.dist(position, s.getPosition());
        if (r < (getRadius() + s.getRadius())) return true;
        return false;
    }

    @Override
    double getMass()
    {
        return mass;
    }

    // Mass setter
    void setMass(int mass)
    {
        this.mass = mass;
    }

    @Override
    float getRadius()
    {
        return radius;
    }

    // Radius setter
    void setRadius(int radius)
    {
        this.radius = radius;
    }

    int getColor()
    {
        return c;
    }

    void setColor(int c)
    {
        this.c = c;
    }

    @Override
    PVector getPosition()
    {
        return position;
    }

    @Override
    PVector getVelocity()
    {
        return velocity;
    }

    @Override
    PVector addVelocity(PVector add)
    {
        return velocity.add(add);
    }

    /**
     * Returns a PVector position that stays bounded to the screen.
     */
    private PVector setPlanetLoc(PVector position, PVector velocity)
    {
        if (position.x + velocity.x <= 0 && position.y + velocity.y <= 0)
            return new PVector(width, height);

        else if (position.x + velocity.x <= 0)
            return new PVector(width, (position.y + velocity.y) % height);

        else if (position.y + velocity.y <= 0)
            return new PVector((position.x + velocity.x) % width, height);

        else
            return new PVector((position.x + velocity.x) % width, (position.y + velocity.y) % height);
    }
}