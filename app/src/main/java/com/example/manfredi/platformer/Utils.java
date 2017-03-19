package com.example.manfredi.platformer;

/**
 * Created by Manfredi on 18/03/2017.
 */

public class Utils {
    public static float clamp(float val, final float min, final float max) {
        if(val < min) {
            val = min;
        }
        else if(val > max) {
            val = max;
        }
        return val;
    }

    public static int clamp(int val, final int min, final int max) {
        if(val < min) {
            val = min;
        }
        else if (val > max) {
            val = max;
        }
        return val;
    }

    public static int wrap(int val, final int min, final int max) {
        if(val < min) {
            val = max;
        }
        else if (val > max) {
            val = min;
        }
        return val;
    }
}
