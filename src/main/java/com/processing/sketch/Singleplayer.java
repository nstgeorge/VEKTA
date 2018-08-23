package com.processing.sketch;

import java.util.*;

class Singleplayer implements Gamemode
{

    int planetCount = 0;
    boolean dead = false;
    PVector lastPosition;
    float shortestDistance = Integer.MAX_VALUE;

    UniverseGen generator = new UniverseGen(6000, 50);

    ArrayList<SpaceObject> objects = new ArrayList<SpaceObject>();
    ArrayList<Spaceship> ships = new ArrayList<Spaceship>();
    ArrayList<ArrayList<PVector>> oldPositions;

    void init()
    {
        // Clear all previous data
        objects.clear();
        ships.clear();
        clearOverlay();

        frameCount = 0;
        dead = false;
        oldPositions = new ArrayList<ArrayList<PVector>>();

        objects = generator.generate();

        ships.add(new Spaceship(
                5000,  // Mass
                5,     // Radius
                new PVector(1, 0), // Heading
                100, 100, // Position
                0, 0,    // Velocity
                color(0, 255, 0),
                0, 50, 60  // Control scheme, Speed, and Handling
        ));
    }

    void render()
    {
        background(0);
        SpaceObject markedForDeath = null;

        // Camera follow
        PVector pos = ships.get(0).getPosition();
        float spd = ships.get(0).getVelocity().mag();
        camera(pos.x, pos.y, (.07 * spd + .7) * (height / 2.0) / tan(PI * 30.0 / 180.0), pos.x, pos.y, 0.0,
                0.0, 1.0, 0.0);

        for (SpaceObject p : objects)
        {
            for (SpaceObject p2 : objects)
            {
                if (!paused) p.getInfluenceVector(p2);
                if (p.collidesWith(p2) && p != p2)
                {
                    // System.out.println(p + " has collided with " + p2);
                    if (p.getMass() > p2.getMass()) markedForDeath = p2;
                    else markedForDeath = p;
                }
                ;
            }
            for (Spaceship s : ships)
            {
                if (!paused) s.getInfluenceVector(p);
                if (s.collidesWith(p))
                {
                    // System.out.println(s + " has collided with " + p);
                    // Assumes ship explodes on contact with anything
                    markedForDeath = s;
                }
                for (Projectile projectile : s.getProjectiles())
                {
                    if (projectile != null)
                    {
                        if (!paused) projectile.getInfluenceVector(p);
                        if (projectile.collidesWith(p))
                        {
                            markedForDeath = p;
                            s.removeProjectile(projectile);
                        }
                        for (Spaceship s2 : ships)
                        {
                            if (projectile.collidesWith(s2) && s != s2)
                            {
                                markedForDeath = s2;
                            }
                        }
                    }
                }
            }
            if (!paused) p.update();
            p.draw();
            drawTrail(p, planetCount);
            planetCount++;
        }
        int shipCount = objects.size();
        for (Spaceship s : ships)
        {
            if (!paused) s.update();
            s.draw();
            drawTrail(s, shipCount);
            shipCount++;
            for (Projectile projectile : s.getProjectiles())
            {
                if (projectile != null)
                {
                    if (!paused) projectile.update();
                    projectile.draw();
                }
            }

            SpaceObject closestObject;
            for (int i = 0; i < objects.size(); i++)
            {
                if (objects.get(i).getPosition().dist(s.getPosition()) < shortestDistance)
                {
                    closestObject = objects.get(i);
                    shortestDistance = (float) closestObject.getPosition().dist(s.getPosition());
                }
            }
        }
        if (markedForDeath != null)
        {
            System.out.println("Object " + markedForDeath + " is marked for death.");
            // I DIED!
            if (markedForDeath instanceof Spaceship)
            {
                dead = true;
                engine.stop();
                death.play(); // Sound of death
                // oldPositions.remove(ships.indexOf(markedForDeath) + objects.size());
                lastPosition = markedForDeath.getPosition();
                ships.remove(markedForDeath);
            } else oldPositions.remove(objects.indexOf(markedForDeath));
            objects.remove(markedForDeath);
            if (objects.size() == 0)
            {
                lastPosition = ships.get(0).getPosition();
            }
        }
        planetCount = 0;

        // Info
        if (!dead)
        {
            Spaceship main = ships.get(0);
            textFont(bodyFont);
            textAlign(CENTER);
            textSize(14);
            fill(0, 255, 0);
            text("Speed = " + (float) round(main.getVelocity().mag() * 100) / 100, main.getPosition().x, main.getPosition().y + 100);
        }

        // Menus
        if (dead)
        {
            overlay.beginDraw();
            overlay.textFont(headerFont);
            overlay.textAlign(CENTER, CENTER);

            // Header text
            overlay.stroke(0);
            overlay.fill(255, 0, 0);
            overlay.text("You died.", width / 2, (height / 2) - 100);

            // Body text
            overlay.stroke(0);
            overlay.fill(255);
            overlay.textFont(bodyFont);
            overlay.text("X TO RETRY", width / 2, (height / 2) + 97);
            overlay.endDraw();
            overlay.setLoaded(true);
        }
    }

    void drawTrail(SpaceObject p, int planetCount)
    {
        if (frameCount > 1)
        {
            ArrayList<PVector> old = oldPositions.get(planetCount);
            if (old.size() > TRAIL_LENGTH) old.remove(0);
            old.add(new PVector(p.getPosition().x, p.getPosition().y));
            for (int i = 0; i < old.size() - 1; i++)
            {
                // Get two positions
                PVector oldPos = old.get(i);
                PVector newPos = old.get(i + 1);
                // Set the color and draw the line
                stroke(lerpColor(p.getColor(), color(0), (float) (old.size() - i) / old.size()));
                line(oldPos.x, oldPos.y, newPos.x, newPos.y);
                // set oldPositions value
                oldPositions.set(planetCount, old);
            }
        } else
        {
            // When the trails are just starting
            ArrayList<PVector> init = new ArrayList<PVector>();
            oldPositions.add(init);
            oldPositions.get(planetCount).add(new PVector(p.getPosition().x, p.getPosition().y));
        }
    }

    void keyPressed(char key)
    {
        if (dead)
        {
            if (key == 'x')
            {
                init();
            }
        } else
        {
            if (key == 'k')
            {
                lastPosition = ships.get(0).getPosition();
                dead = true;
            }
            for (Spaceship s : ships)
            {
                s.keyPress(key);
            }
        }
    }

    void keyReleased(char key)
    {
        for (Spaceship s : ships)
        {
            s.keyReleased(key);
        }
    }
}