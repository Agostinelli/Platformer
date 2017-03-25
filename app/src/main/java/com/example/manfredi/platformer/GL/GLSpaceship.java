package com.example.manfredi.platformer.GL;

/**
 * Created by Manfredi on 25/03/2017.
 */

public class GLSpaceship extends GLGameObject {

    public GLSpaceship(float x, float y) {
        mPos.x = x;
        mPos.y = y;
        float width = 15;
        float height = 20;
        float halfW = width/2;
        float halfH = height/2;
        float[] vertices = new float[] {
                -halfW, -halfH, 0,
                halfW, -halfH, 0,
                0, halfH, 0
        };
        mModel = new GLModel(vertices, null);
    }

    @Override
    public void update(float dt) {
        mRotation = sineWave(180, 180, mVR);
        float radiusx = GLGameView.mWorldWidth/2;
        float radiusy = GLGameView.mWorldHeight/2;
        mPos.x = sineWave(GLGameView.mWorldWidth/2, radiusx, mVR);
        mPos.y = cosWave(GLGameView.mWorldHeight/2,radiusy,mVR);
        mVR += 0.02f;
    }
}
