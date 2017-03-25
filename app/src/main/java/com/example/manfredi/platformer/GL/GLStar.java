package com.example.manfredi.platformer.GL;

import android.opengl.GLES20;

/**
 * Created by Manfredi on 25/03/2017.
 */

public class GLStar extends GLGameObject {
    public GLStar(float x, float y) {
        mPos.x = x;
        mPos.y = y;
        float[] vertices = new float[] {
                0,0,0
        };
        mModel = new GLModel(vertices, GLES20.GL_POINTS);
    }

    @Override
    public void update(float dt) {
    }



}
