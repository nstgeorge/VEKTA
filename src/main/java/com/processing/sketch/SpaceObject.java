package com.processing.sketch;

interface SpaceObject
{

    /**
     * Gets the mass of the object
     */
    double getMass();

    /**
     * Gets the position of the object
     */
    PVector getPosition();

    /**
     * Gets the velocity of the object
     */
    PVector getVelocity();

    /**
     * Gets the color of the object
     */
    int getColor();

    /**
     * Gets the radius of the object (for collision purposes, not all objects are circular)
     */
    float getRadius();

    /**
     * Adds a vector to the velocity vector; returns new velocity
     */
    PVector addVelocity(PVector add);

    /**
     * Returns and applies the influence vector of another object on this
     */
    PVector getInfluenceVector(SpaceObject s);

    /**
     * Does this collide with that?
     */
    boolean collidesWith(SpaceObject s);

    void draw();

    /**
     * Update the position of this Object.
     */
    void update();
}  