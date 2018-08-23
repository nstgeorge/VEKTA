package com.processing.sketch;

class Menu
{
    String[] modes = {"Singleplayer", "Multiplayer"};
    Hyperspace hyperspace;

    public Menu()
    {
        theme.play();
        hyperspace = new Hyperspace(new PVector(width / 2, height / 2), 0.1, 100, color(255));
    }

    void render()
    {
        overlay.beginDraw();
        overlay.background(0, 0);
        overlay.shapeMode(CENTER);
        overlay.shape(logo, width / 2, height / 4, 339.26, 100);
        for (int i = 0; i < modes.length; i++)
        {
            drawOption(modes[i], (height / 2) + (i * 100), i == selectedMode);
        }
        drawOption("Quit", (height / 2) + (modes.length * 100), selectedMode == modes.length);

        overlay.textFont(bodyFont);
        overlay.stroke(0);
        overlay.fill(255);
        overlay.textAlign(CENTER);
        overlay.text("X to select", width / 2, (height / 2) + (modes.length * 100) + 100);

        overlay.textSize(14);
        overlay.text("Created by Nate St. George", width / 2, (height / 2) + (modes.length * 100) + 150);
        overlay.setLoaded(true);
    }

    private void drawOption(String name, int yPos, boolean selected)
    {
        // Shape ---------------------
        if (selected) overlay.stroke(255);
        else overlay.stroke(0, 255, 0);
        overlay.fill(1);
        overlay.rectMode(CENTER);
        overlay.rect(width / 2, yPos, 200, 50);
        // Text ----------------------
        overlay.textFont(bodyFont);
        overlay.stroke(0);
        overlay.fill(0, 255, 0);
        overlay.textAlign(CENTER, CENTER);
        overlay.text(name, width / 2, yPos - 3);
    }

    void keyPressed(char key)
    {
        if (key == 'w')
        {
            change.play();
            selectedMode = Math.max(selectedMode - 1, 0);
            redraw();
        }
        if (key == 's')
        {
            change.play();
            selectedMode = Math.min(selectedMode + 1, modes.length);
            redraw();
        }
        if (key == 'x')
        {
            select.play();
            modePicked = true;
        }
    }
}  