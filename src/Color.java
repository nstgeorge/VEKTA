/**
 * Used for storing colors in the format expected by fill() and stroke().
 * https://processing.org/reference/color_datatype.html
 */

public class Color {
    int color; // Used to store the 32 bits of color information: AAAAAAAARRRRRRRRGGGGGGGGBBBBBBBB
    public Color(int r, int g, int b) {

        color = convertRGB((byte)(r % 256), (byte)(g % 256), (byte)(b & 256));
    }

    public Color(int[] rgb) {
        color = convertRGBFromIntArray(rgb);
    }

    public int getIntValue() {
        return color;
    }

    public void setColor(int r, int g, int b) {
        color = convertRGB((byte)(r % 256), (byte)(g % 256), (byte)(b & 256));
    }

    // Conversion functions (I normally don't like bit-twiddling but this is the best way to do it here)

    private int convertRGB(byte r, byte g, byte b) {
        return (byte)(255) << 24 | r << 16 | g << 8 | b;
    }

    private int convertRGBFromIntArray(int[] rgb) {
        return (255) << 24 | (rgb[0] % 256) << 16 | (rgb[1] % 256) << 8 | (rgb[2] % 256);
    }
}
