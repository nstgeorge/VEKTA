package vekta;

/**
 * Utility class for displaying status messages to the player
 * Used by TargetingModule and a future impl of an AutopilotModule
 */
public class Status {
    private int xLocation;
    private int yLocation;

    private String prefix;
    private String postfix;
    private String message;
    private int color;
    private boolean show;

    private Vekta v;

    public Status() {
        v = Vekta.getInstance();
        xLocation = 50;
        yLocation = v.height - 150;
        color = v.color(255, 255, 255);

        prefix = postfix = "::";
    }

    public int getColor() { return color; }

    public void setColor(int color) {
        this.color = color;
    }

    public int getX() {
        return xLocation;
    }

    public void setX(int xLocation) {
        this.xLocation = xLocation;
    }

    public int getY() {
        return yLocation;
    }

    public void setY(int yLocation) {
        this.yLocation = yLocation;
    }

    public String getMessage() {
        return prefix + " " + message + " " + postfix;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void clearMessage() {
        message = "";
    }

    public boolean isShown() {
        return show;
    }

    public void show() {
        show = true;
    }

    public void hide() {
        show = false;
    }
}
