package com.Game.Util.Math;

public class Util {
    public static <T> T[] cycleArray(T[] array, int index) {
        T[] newArray = array.clone();

        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[(i + index) % array.length];
        }

        return newArray;
    }
}
