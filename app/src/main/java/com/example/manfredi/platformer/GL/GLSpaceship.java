package com.example.manfredi.platformer.GL;

import com.example.manfredi.platformer.App;
import com.example.manfredi.platformer.R;
import com.example.manfredi.platformer.Utils;
import com.example.manfredi.platformer.engine.GameEngine;

/**
 * Created by Manfredi on 25/03/2017.
 */

public class GLSpaceship extends GLGameObject {
    private static final float TO_RADIANS = ((float)Math.PI/180);
    private GameEngine mEngine = null;
    private float mMaxRotationSpeed = Float.parseFloat(App.getContext().getString(R.string.max_rotation));
    private float mThrust = Float.parseFloat(App.getContext().getString(R.string.thrust));
    private float mVelX = 0f;
    private float mVelY = 0f;
    private float mMaxVelocity = Float.parseFloat(App.getContext().getString(R.string.maxVel));
    private float mFriction = Float.parseFloat(App.getContext().getString(R.string.friction));

    public GLSpaceship(GameEngine engine, float x, float y) {
        mEngine = engine;
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
        mRotation += mEngine.mControl.mHorizontalFactor * (mMaxRotationSpeed * dt);
        float accel = mEngine.mControl.mVerticalFactor * mThrust;
        float angle = (mRotation-90) * TO_RADIANS;
        float ax = (float) Math.cos(angle) * accel;
        float ay = (float) Math.sin(angle) * accel;
        mVelX += ax*dt;
        mVelY += ay*dt;
        mVelX = Utils.clamp(mVelX, -mMaxVelocity, mMaxVelocity);
        mVelY = Utils.clamp(mVelY, -mMaxVelocity, mMaxVelocity);
        mPos.x += mVelX;
        mPos.y = mVelY;
        mVelX *= mFriction;
        mVelY *= mFriction;

    }

}
