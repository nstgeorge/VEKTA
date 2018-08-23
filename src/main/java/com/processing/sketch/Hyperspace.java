package com.processing.sketch;

class Hyperspace
{
    private PVector origin;
    private float particleSpeed;
    private Particle[] particles;
    private int c;

    public Hyperspace(PVector origin, float accel, int particleNum, int c)
    {
        this.origin = origin;
        this.particleSpeed = accel;
        this.c = c;

        // Generate a new set of particles all at once
        int i = 0;
        while (i <= particleNum)
        {
            newParticle();
        }
    }

    Particle newParticle()
    {
        // Create a new random PVector
        PVector accel = PVector.random2D();
        accel.mult(particleSpeed);
        return new Particle(origin, accel, c);
    }

    void draw()
    {
        for (Particle p : particles)
        {
            p.draw();
        }
    }

    void update()
    {
        for (Particle p : particles)
        {
            p.update();
            if (p.getLoc().x > width || p.getLoc().y > height)
            {
                p = newParticle();
            }
        }
    }
}  