package com.example.manfredi.platformer.GL;

import android.graphics.PointF;
import android.opengl.Matrix;

/**
 * Created by Manfredi on 24/03/2017.
 */

public class GLGameObject {
    public static final float depth = 0.0f;
    public static final float[] modelMatrix = new float[4*4];
    public static final float[] viewportModelMatrix = new float[4*4];
    public static final float[] rotateViewportModelMatrix = new float[4*4];

    protected GLModel mModel = null;
    public PointF mPos = new PointF(0f,0f);
    protected float mScale = 1f;
    protected float mRotation = 0;
    protected float mVR = 0f;



    public GLGameObject() {
    }

    public static float sineWave(final float centerX, final float range, final float currentAngle) {
        return centerX + (float) Math.sin(currentAngle) * range;
    }

    public static float cosWave(final float centerY, final float range, final float currentAngle) {
        return centerY + (float) Math.cos(currentAngle) * range;
    }


    public void update(float dt) {

    }

    public void draw(final float[] viewMatrix) {
        final int OFFSET = 0;


        Matrix.setIdentityM(modelMatrix, OFFSET);
        Matrix.translateM(modelMatrix, OFFSET, mPos.x, mPos.y, depth);
        Matrix.multiplyMM(viewportModelMatrix, OFFSET, viewMatrix, OFFSET, modelMatrix, OFFSET);
        Matrix.setRotateM(modelMatrix, OFFSET, mRotation, 0, 0, 1.f);
        Matrix.scaleM(modelMatrix, OFFSET, mScale, mScale, 1f);
        Matrix.multiplyMM(rotateViewportModelMatrix, OFFSET, viewportModelMatrix, OFFSET, modelMatrix, OFFSET);
        GLManager.draw(mModel, rotateViewportModelMatrix);
    }


}
