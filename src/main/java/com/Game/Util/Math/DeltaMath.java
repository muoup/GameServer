package com.Game.Util.Math;

public class DeltaMath {
    public static final float pi = 3.1415926538f;

    public static int[] fillArray(int content, int length) {
        int[] array = new int[length];

        for (int i = 0; i < array.length; i++) {
            array[i] = content;
        }

        return array;
    }

    public static boolean between(float c, float a, float b) {
        return c >= a && c <= b;
    }

    public static float roundTo(float in, float to) {
        float dIn = in / to;
        dIn = Math.round(dIn);
        return dIn * to;
    }

    public static float maxmin(float in, float b, float t) {
        return Math.max(Math.min(t, in), b);
    }

    public static float logb(float base, float number) {
        return (float) (Math.log10(number) / Math.log10(base));
    }

    public static float range(float low, float high) {
        return (float) Math.random() * (high - low) + low;
    }

    public static double trueArcSine(float amt) {
        return Math.asin(amt) + Math.ceil((2 * amt - pi) / pi);
    }

    public static boolean randBool() {
        return Math.random() > 0.5;
    }
}
