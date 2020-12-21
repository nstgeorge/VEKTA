#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

#define PROCESSING_TEXTURE_SHADER

// Consts
const float PI = 3.1415926535;
const float EPSILON = 0.000011;

// For use by fish-eye shader
uniform float fishLensPower;

// For use by vignette shader
uniform float vigInnerRad;
uniform float vigOuterRad;
uniform float vigOpacity;

// For use by dithering shader
uniform float dithMixScale;
uniform float dithNoiseScale;

// For use by scanline shader
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

vec2 GetFishUV(vec2 pixel)
{
    float theta = atan(pixel.y, pixel.x);
    float radius = length(pixel);
    radius = pow(radius, fishLensPower);
    pixel.x = radius * cos(theta);
    pixel.y = radius * sin(theta);

    return pixel;
}

void main() {
    // Fish-eye calculations
    vec3 fishOut = texture(texture, GetFishUV(gl_FragCoord.xy / resolution.xy)).xyz;

    // Vignette calculations
    vec2 vigCenter = (vertTexCoord.xy - vec2(0.5)) * (resolution.x / resolution.y);
    vec4 vigColor = vec4(1);//vec4(0.8, 0.8, 0.8, 1);
    vigColor.rgb *= smoothstep(vigInnerRad, vigOuterRad, length(vigCenter));
    vigColor *= vertColor;
    vigColor *= vigOpacity;
    vec3 vigOut = fishOut - vigColor.xyz;

    // Dithering calculations
    lowp vec2 dithUV = vertTexCoord.xy / resolution.xy;
    lowp vec3 dithResult = vigOut;
    dithResult = pow(dithResult, vec3(3.25));
    dithResult = ACESFilm(dithResult.xyz);
    vec3 dithOut = mix(vigOut, dithResult, dithMixScale);

    // Scanline calculations
    float line = step(0.5, sin(time + (10000 * count * vertTexCoord.y / resolution.y)));
    vec3 scanLineOut = dithOut + (brightnessBoost * line);

    // Final output
    gl_FragColor = vec4(scanLineOut, 1);
}
