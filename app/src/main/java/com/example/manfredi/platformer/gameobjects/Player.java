package com.example.manfredi.platformer.gameobjects;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import com.example.manfredi.platformer.AnimationManager;
import com.example.manfredi.platformer.App;
import com.example.manfredi.platformer.Jukebox;
import com.example.manfredi.platformer.R;
import com.example.manfredi.platformer.engine.GameEngine;

/**
 * Created by Manfredi on 05/03/2017.
 */

public class Player extends DynamicGameObject {
    private static final String TAG = App.getContext().getString(R.string.dynamicGameObjectTag);
    private static final float PLAYER_HEIGHT = Float.parseFloat(App.getContext().getString(R.string.PLAYER_HEIGHT));
    private static final float PLAYER_WIDTH = Float.parseFloat(App.getContext().getString(R.string.PLAYER_WIDTH));
    private static final float PLAYER_RUN_SPEED = Float.parseFloat(App.getContext().getString(R.string.PLAYER_RUN_SPEED));
    private static final float PLAYER_FRICTION = Float.parseFloat(App.getContext().getString(R.string.PLAYER_FRICTION));
    private static final float PLAYER_ACCELERATION_X = Float.parseFloat(App.getContext().getString(R.string.PLAYER_ACCELERATION_X));
    private static final float PLAYER_ACCELERATION_Y = Float.parseFloat(App.getContext().getString(R.string.PLAYER_ACCELERATION_Y));
    private static final float PLAYER_JUMP_HEIGHT = Float.parseFloat(App.getContext().getString(R.string.PLAYER_JUMP_HEIGHT));
    private static final float PLAYER_JUMP_DURATION = Float.parseFloat(App.getContext().getString(R.string.PLAYER_JUMP_DURATION));
    private static final float PLAYER_JUMP_IMPULSE = -(PLAYER_JUMP_HEIGHT/PLAYER_JUMP_DURATION);
    private static final int DAMAGE = App.getContext().getResources().getInteger(R.integer.damage);
    private static final int HEALTH = App.getContext().getResources().getInteger(R.integer.health);

    private static final int LEFT = App.getContext().getResources().getInteger(R.integer.left);
    private static final int RIGHT = App.getContext().getResources().getInteger(R.integer.right);

    private boolean mIsOnGround = false;
    private int mFacing = LEFT;
    private float mJumpTime = Float.parseFloat(App.getContext().getString(R.string.JUMP_TIME));
    private AnimationManager mAnim = null;

    private int mCoins;
    private int mHealth;

    private Jukebox mJukebox;



    public int getHealth() {
        return mHealth;
    }

    public void decreaseHealth() {
        this.mHealth -= DAMAGE;
    }

    public int getCoins() {
        return mCoins;
    }

    public void coinPickedUp() {
        this.mCoins += 1;
    }

    public Player(final GameEngine engine, final float x, final float y, final int type) {
        super(engine, x, y, PLAYER_WIDTH, PLAYER_HEIGHT, type);
        mJukebox = new Jukebox(engine.getContext());
        mCoins = 0;
        mHealth = HEALTH;
        mAcceleration.x = PLAYER_ACCELERATION_X;
        mAcceleration.y = PLAYER_ACCELERATION_Y;
        mFriction = PLAYER_FRICTION;
        mAnim = new AnimationManager(engine, R.drawable.player_anim, PLAYER_WIDTH, PLAYER_HEIGHT);

    }

    @Override
    public void onCollision(final GameObject that) {
        if(!getOverlap(this, that, overlap)) {
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
        mEngine.setScreenCoordinate(mWorldLocation, screenCoord);
        int offset = 0;
        if(mFacing == RIGHT) {
            offset = (int) mWidth * mEngine.getPixelsPerMeter();
        }
        mTransform.postTranslate(screenCoord.x+offset, screenCoord.y);



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
