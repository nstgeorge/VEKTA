package com.processing.sketch;

import java.util.*;

import processing.sound.*;

public class Vekta extends processing.core.PApplet
{

    final String FONTNAME = "font/stm.ttf";
    Gamemode game;
    Menu menu;

    final double G = 6.674 * Math.pow(10, -11);
    public final double SCALE = 3 * Math.pow(10, 8);
    final float VECTOR_SCALE = 5;
    final int MAX_PLANETS = 100;
    final int TRAIL_LENGTH = 20;
    final float DEF_ZOOM = (height / 2.0) / tan(PI * 30.0 / 180.0); // For some reason, this is the default eyeZ location for Processing

    String[] pauseMenu = {"Continue", "Restart", "Quit to Menu"};
    int pauseSelected = 0;

    PFont headerFont;
    PFont bodyFont;

    PGraphics overlay;

    PShape logo;
    boolean modePicked = false;
    boolean switchedToGame = false;
    boolean paused = false;
    int selectedMode = 0;
    int[] playerWins = new int[2];

    SoundFile theme;
    SoundFile laser;
    SoundFile death;
    SoundFile engine;
    SoundFile change;
    SoundFile select;

    BrownNoise noise;

    public void setup()
    {
        fullScreen(P3D);
        background(0);
        frameRate(60);
        noCursor();
        textMode(SHAPE);
        // Overlay initialization
        overlay = createGraphics(width, height);
        // Fonts
        headerFont = createFont(FONTNAME, 72);
        bodyFont = createFont(FONTNAME, 24, true);
        // Images
        logo = loadShape("VEKTA.svg");
        // All sounds and music. These must be instantiated in the main file
        // Music
        theme = new SoundFile(this, "main.wav");

        // Sound
        laser = new SoundFile(this, "laser.wav");
        death = new SoundFile(this, "death.wav");
        engine = new SoundFile(this, "engine.wav");
        change = new SoundFile(this, "change.wav");
        select = new SoundFile(this, "select.wav");

        playerWins[0] = 0;
        playerWins[1] = 0;

        menu = new Menu();
    }

    public void draw()
    {
        // Pause menu overlay
        if (paused)
        {
            overlay.beginDraw();
            overlay.background(0, 0);
            // Border box
            overlay.rectMode(CORNER);
            overlay.stroke(0, 255, 0);
            overlay.fill(1);
            overlay.rect(0, 0, width / 4, height - 1);
            // Logo
            overlay.shapeMode(CENTER);
            overlay.shape(logo, width / 8, 100, (width / 4) - 100, ((width / 4) - 100) / 3.392);
            // Options
            for (int i = 0; i < pauseMenu.length; i++)
            {
                drawOption(pauseMenu[i], (height / 2) + (i * 100), i == pauseSelected);
            }
            overlay.textFont(bodyFont);
            overlay.stroke(0);
            overlay.fill(255);
            overlay.textAlign(CENTER);
            overlay.text("X to select", width / 8, (height / 2) + (pauseMenu.length * 100) + 100);
            overlay.endDraw();

            overlay.setLoaded(true);
            redraw();
            noLoop();
        } else
        {
            clearOverlay();
            loop();
        }
        // Menu / game render switch
        if (modePicked)
        {
            if (selectedMode == 2) exit();
            else if (!switchedToGame)
            {
                if (selectedMode == 0) game = new Singleplayer();
                if (selectedMode == 1) game = new Multiplayer();
                theme.stop();
                game.init();
                switchedToGame = true;
            }
        }
        if (modePicked && switchedToGame)
        {
            game.render();
        } else
        {
            menu.render();
        }
        drawOverlay();
    }

    void keyPressed()
    {
        // Toggle pause
        if (key == ESC)
        {
            key = 0;
            paused = !paused;
        }
        // Pause controls 
        else if (paused)
        {
            if (key == 'w')
            {
                change.play();
                pauseSelected = Math.max(pauseSelected - 1, 0);
            }
            if (key == 's')
            {
                change.play();
                pauseSelected = Math.min(pauseSelected + 1, pauseMenu.length - 1);
            }
            if (key == 'x')
            {
                theme.stop();
                select.play();
                switch (pauseSelected)
                {
                    case (0):
                        clearOverlay();
                        paused = false;
                        loop();
                        break;
                    case (1):
                        game.init();
                        paused = false;
                        break;
                    case (2):
                        modePicked = false;
                        switchedToGame = false;
                        paused = false;
                        break;
                    default:
                        break;
                }
            }
            redraw();
        }
        // Send event to game
        else if (modePicked)
        {
            game.keyPressed(key);
        }
        // Send event to menu
        else
        {
            menu.keyPressed(key);
        }
    }

    void keyReleased()
    {
        if (modePicked)
        {
            game.keyReleased(key);
        }
    }

    void clearOverlay()
    {
        if (overlay.isLoaded())
        {
            overlay.clear();
            overlay.beginDraw();
            overlay.background(0, 0);
            overlay.endDraw();
            overlay.setLoaded(false);
        }
    }

    void drawOverlay()
    {
        // Overlay the overlay
        // NOTE: THIS IS VERY SLOW. Use only for menus, not gameplay!
        if (overlay.isLoaded())
        {
            overlay.loadPixels();
            loadPixels();
            for (int i = 0; i < pixels.length; i++)
                if (overlay.pixels[i] != color(0)) pixels[i] = overlay.pixels[i];
            updatePixels();
            overlay.updatePixels();
            //image(overlay, 0, 0);
            //redraw();
        }
    }

    /**
     * Draws an option of name "name" at yPos in the overlay
     */
    private void drawOption(String name, int yPos, boolean selected)
    {
        // Shape ---------------------
        if (selected) overlay.stroke(255);
        else overlay.stroke(0, 255, 0);
        overlay.fill(1);
        overlay.rectMode(CENTER);
        overlay.rect(width / 8, yPos, 200, 50);
        // Text ----------------------
        overlay.textFont(bodyFont);
        overlay.stroke(0);
        overlay.fill(0, 255, 0);
        overlay.textAlign(CENTER, CENTER);
        overlay.text(name, width / 8, yPos - 3);
    }
}