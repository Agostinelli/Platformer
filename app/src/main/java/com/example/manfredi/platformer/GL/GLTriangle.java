package com.example.manfredi.platformer.GL;

/**
 * Created by Manfredi on 24/03/2017.
 */

public class GLTriangle extends GLModel {
    private static float triangleCoords[] = {   // in counterclockwise order:
            0.0f,  0.5f, 0.0f, // top
            -0.5f, -0.5f, 0.0f, // bottom left
            0.5f, -0.5f, 0.0f  // bottom right
    };
    public GLTriangle(final float[] colors) {
         super(triangleCoords, colors);
    }
}
