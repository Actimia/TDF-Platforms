package org.dyndns.tdfpro.platforms;

public class Utils {

    public static float clamp(float c, float min, float max) {
        return c < min ? min : c > max ? max : c;
    }

    /**
     * 0 < f < 1
     * 
     * @param a
     * @param b
     * @param f
     * @return
     */
    public static float lerp(float a, float b, float f) {
        return a * f + b * (1 - f);
    }

    public static float sign(float a) {
        return a > 0 ? (a < 0 ? -1 : 0) : 1;
    }

}
