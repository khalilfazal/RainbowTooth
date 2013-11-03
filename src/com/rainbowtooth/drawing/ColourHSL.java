package com.rainbowtooth.drawing;

import android.graphics.Color;

/**
 * Adds HSL support to {@link Color}
 * 
 * @author Khalil Fazal
 */
public class ColourHSL extends Color {

    /**
     * Convert HSL components to an ARGB color.
     * {@link "https://en.wikipedia.org/wiki/HLS_color_space#From_HSL"}
     * 
     * - Convert to HSV
     * - Use {@link Color#HSVToColor(float[])}
     * - {@link "https://gist.github.com/xpansive/1337890"}
     * 
     * @param hue is Hue [0 .. 360)
     * @param saturation is Saturation [0 .. 1]
     * @param lightness is Lightness [0 .. 1]
     * @return the color in ARGB form
     */
    public static int HSLToColor(final float hue, final float saturation, final float lightness) {
        final float grayness = saturation * (lightness < 0.5 ? lightness : 1 - lightness);

        return HSVToColor(new float[] {
            hue,
            2 * grayness / (lightness + grayness),
            grayness + lightness
        });
    }
}