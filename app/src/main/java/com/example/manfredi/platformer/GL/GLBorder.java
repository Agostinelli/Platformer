package com.example.manfredi.platformer.GL;

import android.opengl.GLES20;

/**
 * Created by Manfredi on 25/03/2017.
 */

public class GLBorder extends GLGameObject {
    public GLBorder(float width, float height) {
        float[] vertices = {
                0, height, 0,
                width, height, 0,
                width, 0, 0,
                width, 0, 0,
                0, 0, 0,
                0, 0, 0,
                0, height, 0
        };
        mModel = new GLModel(vertices, GLES20.GL_LINES);
    }
}
