package com.example.manfredi.platformer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

/**
 * Created by Manfredi on 05/03/2017.
 */

public class Player extends DynamicGameObject {
    private static final String TAG = "DynamicGameObject";
    private static final float PLAYER_HEIGHT = 2.0f; //TODO move to xml - meters
    private static final float PLAYER_WIDTH = 1.0f;
    private static final float PLAYER_RUN_SPEED = 6.0f;
    private static final float PLAYER_FRICTION = 0.98f;
    private static final float PLAYER_ACCELERATION_X = 0.75f;
    private static final float PLAYER_ACCELARATION_Y = 0.2f;
    private static final float PLAYER_JUMP_HEIGHT = 3f;
    private static final float PLAYER_JUMP_DURATION = 0.180f;
    private static final float PLAYER_JUMP_IMPULSE = -(PLAYER_JUMP_HEIGHT/PLAYER_JUMP_DURATION);
    private static final float MAX_VELOCITY = 6f; // running speed
    private static final float ACCEL_X = 0.125f;
    private static final float ACCEL_Y = 0.15f;
    private static final float JUMP_INPULSE = -8f;

    private static final int LEFT = 1;
    private static final int RIGHT = -1;

    private boolean mIsOnGround = false;
    private int mFacing = LEFT;
    private float mJumpTime = 0.0f;
    private AnimationManager mAnim = null;

    private int mCoins;
    private int mHealth;

    private Jukebox mJukebox;



    public int getHealth() {
        return mHealth;
    }

    public void decreaseHealth() {
        this.mHealth -= 20;
    }

    public int getCoins() {
        return mCoins;
    }

    public void coinPickedUp() {
        this.mCoins += 1;
    }

    Player(final GameView engine, final float x, final float y, final int type) {
        super(engine, x, y, PLAYER_WIDTH, PLAYER_HEIGHT, type);
        mJukebox = new Jukebox(engine.getContext());
        mCoins = 0;
        mHealth = 100;
        mAcceleration.x = PLAYER_ACCELERATION_X;
        mAcceleration.y = PLAYER_ACCELARATION_Y;
        mFriction = PLAYER_FRICTION;
        mAnim = new AnimationManager(engine, R.drawable.player_anim, PLAYER_WIDTH, PLAYER_HEIGHT);

    }

    @Override
    public void onCollision(final GameObject that) {
        if(!GameObject.getOverlap(this, that, overlap)) {
            Log.d(TAG, mEngine.getContext().getString(R.string.OverlapError));
        }
        if (overlap.y != 0) {
            mVelocity.y = 0;
            if(overlap.y < 0) {
                mIsOnGround = true;
            }

        }
        mWorldLocation.offset(overlap.x, overlap.y);
        updateBounds();
    }

    public void render(Canvas canvas, Paint paint) {
        mTransform.reset();
        mTransform.setScale(mFacing, 1.0f);
        mEngine.setScreenCoordinate(mWorldLocation, GameObject.screenCoord);
        int offset = 0;
        if(mFacing == RIGHT) {
            offset = (int) mWidth * mEngine.getPixelsPerMeter();
        }
        mTransform.postTranslate(GameObject.screenCoord.x+offset, GameObject.screenCoord.y);



        canvas.drawBitmap(mAnim.getCurrentBitmap(), mTransform, paint);
    }

    @Override
    public void update(float dt) {
        mAnim.update(dt);

        if(mVelocity.y != 0) {
            mIsOnGround = false;
        }

        if(mEngine.mControl.mHorizontalFactor < 0) {
            mFacing = LEFT;
        }
        else if(mEngine.mControl.mHorizontalFactor > 0) {
            mFacing = RIGHT;
        }

        mTargetSpeed.x = mEngine.mControl.mHorizontalFactor * (PLAYER_RUN_SPEED * dt);

        if(mEngine.mControl.mIsJumping && mJumpTime < PLAYER_JUMP_DURATION) {
            mJukebox.play(Jukebox.JUMP);
            mVelocity.y = (PLAYER_JUMP_IMPULSE * dt);
            mJumpTime += dt;
            mIsOnGround = false;
        }
        else {
            mVelocity.y += mAcceleration.y * (TERMINAL_VELOCITY * dt);
        }
        super.update(dt);
    }
}
