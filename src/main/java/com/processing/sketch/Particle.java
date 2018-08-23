package com.processing.sketch;

class Particle
{
    private PVector loc;
    private PVector accel;
    private PVector velocity;
    private int c;

    public Particle(PVector loc, PVector accel, int c)
    {
        this.loc = loc;
        this.accel = accel;
        this.velocity = accel;
        this.c = c;
    }

    void draw()
    {
        fill(0);
        stroke(c);
        line(loc.x, loc.y, loc.x - velocity.x, loc.y - velocity.y);
    }

    void update()
    {
        velocity.add(accel);
        loc.add(velocity);
    }

    PVector getLoc()
    {
        return loc;
    }
}