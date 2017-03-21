package com.example.manfredi.platformer;

import android.graphics.PointF;

/**
 * Created by Manfredi on 18/03/2017.
 */

public class DynamicGameObject extends GameObject {
    private static final String TAG = App.getContext().getString(R.string.dynamicGameObjectTag);
    private static final float MAX_DELTA = Float.parseFloat(App.getContext().getString(R.string.MAX_DELTA));
    private static final float MIN_DELTA = Float.parseFloat(App.getContext().getString(R.string.MIN_DELTA));
    protected static final float TERMINAL_VELOCITY = Float.parseFloat(App.getContext().getString(R.string.TERMINAL_VELOCITY));
    public PointF mVelocity = new PointF(0.0f, 0.0f);
    protected PointF mTargetSpeed = new PointF(0.0f, TERMINAL_VELOCITY);
    protected PointF mAcceleration = new PointF(0.0f, 0.0f);
    public float mFriction = Float.parseFloat(App.getContext().getString(R.string.FRICTION));

    DynamicGameObject(final GameView engine, float x, float y, float width, float height, int type) {
        super(engine, x, y, width, height, type);
    }

    DynamicGameObject(final GameView engine, float x, float y, int type) {
        super(engine, x, y, type);
    }

    @Override
    public void update(float deltaTime) {
        mVelocity.x *= mFriction;
        mVelocity.y *= mFriction;

        mVelocity.x += mAcceleration.x * mTargetSpeed.x;
        if(Math.abs(mVelocity.x) > Math.abs(mTargetSpeed.x)) {
            mVelocity.x = mTargetSpeed.x;
        }

        mVelocity.x = Utils.clamp(mVelocity.x, -MAX_DELTA, MAX_DELTA);
        mVelocity.y = Utils.clamp(mVelocity.y, -MAX_DELTA, MAX_DELTA);
        if(Math.abs(mVelocity.y) < MIN_DELTA) {mVelocity.y = 0;}
        if(Math.abs(mVelocity.x) < MIN_DELTA) {mVelocity.x = 0;}
        mWorldLocation.x += mVelocity.x;
        mWorldLocation.y += mVelocity.y;
        updateBounds();
    }

}
