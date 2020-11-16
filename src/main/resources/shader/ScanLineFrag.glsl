#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

#define PROCESSING_TEXTURE_SHADER

// For use by vignette shader
uniform float vigInnerRad;
uniform float vigOuterRad;
uniform float vigOpacity;

// For use by dithering shader
uniform float dithMixScale;
uniform float dithNoiseScale;

uniform float count;            // Not a true count of the number of scan lines on screen, but proportional
uniform vec2 resolution;        // Resolution of the monitor/game window
uniform float brightnessBoost;  // How much to boost the areas that are in a scan line
uniform float time;             // Current time in the sketch (used to slowly animate the lines)

uniform sampler2D texture;

varying vec4 vertColor;
varying vec4 vertTexCoord;

lowp vec3 ACESFilm(vec3 x)
{
    x *= 0.6;
    lowp float a = 2.51 * dithNoiseScale;
    lowp float b = 0.03 * dithNoiseScale;
    lowp float c = 2.43 * dithNoiseScale;
    lowp float d = 0.59 * dithNoiseScale;
    lowp float e = 0.14 * dithNoiseScale;
    return clamp((x*(a*x+b))/(x*(c*x+d)+e), 0.0, 1.0);
}

void main() {
    // Vignette calculations
    vec2 vigCenter = (vertTexCoord.xy - vec2(0.5)) * (resolution.x / resolution.y);
    vec4 vigColor = vec4(0.8, 0.8, 0.8, 0.5);
    vigColor.rgb *= 1.0 - smoothstep(vigInnerRad, vigOuterRad, length(vigCenter));
    vigColor *= vertColor;

    // Dithering calc
    lowp vec2 uv = vertTexCoord.xy / resolution.xy;
    lowp vec3 result = vigColor.xyz;
    result = pow(result, vec3(3.25));
    result = ACESFilm(result.xyz);
    vec3 vigDithOut = mix(vigColor.xyz, result, dithMixScale);

    // Determine whether the current pixel is in a scanline. If it is, this value is 1, otherwise 0
    float line = step(0.5, sin(time + (10000 * count * vertTexCoord.y / resolution.y)));

    // Add the line value multiplied by the brightness boost setting to the pixel's previous value
    gl_FragColor = mix(texture2D(texture, vertTexCoord.xy) + (brightnessBoost * line), vec4(vigDithOut, 1), vigOpacity);
}
