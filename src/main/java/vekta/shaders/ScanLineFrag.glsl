#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

#define PROCESSING_TEXTURE_SHADER

uniform float count;            // Not a true count of the number of scan lines on screen, but proportional
uniform vec2 resolution;        // Resolution of the monitor/game window
uniform float brightnessBoost;  // How much to boost the areas that are in a scan line
uniform float time;             // Current time in the sketch (used to slowly animate the lines)

uniform sampler2D texture;

varying vec4 vertColor;
varying vec4 vertTexCoord;

void main() {
    // Determine whether the current pixel is in a scanline. If it is, this value is 1, otherwise 0
    float line = step(0.5, sin(time + (10000 * count * vertTexCoord.y / resolution.y)));
    // Add the line value multiplied by the brightness boost setting to the pixel's previous value
    gl_FragColor = texture2D(texture, vertTexCoord.xy) + (brightnessBoost * line);
}