package com.example.manfredi.platformer;

import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;

import java.util.Random;

/**
 * Created by Manfredi on 19/03/2017.
 */

public class AnimationManager {
    private static final float SECOND = Float.parseFloat(App.getContext().getString(R.string.SECOND));
    private GameView mEngine = null;
    private AnimationDrawable mAnim = null;
    private Bitmap[] mFrames = null;
    private int mFrameCount = 0;
    private int mDuration = 0;
    private long mCurrentAnimTime = 0;
    private int mCurrentFrame = 0;

    public AnimationManager(GameView engine, int resourceID, float width, float height) {
        mEngine = engine;
        loadAnimationResource(resourceID, width, height);
    }

    private void loadAnimationResource(int resouceID, float width, float height) {
        recycle();
        mAnim = (AnimationDrawable) ContextCompat.getDrawable(mEngine.getContext(), resouceID);
        mFrameCount = mAnim.getNumberOfFrames();
        mDuration = getAnimationDuration(mAnim);
        mFrames = prepareAnimation(mAnim, mEngine.getPixelsPerMeter(), width, height);
        mCurrentAnimTime = 0;
        if(!mAnim.isOneShot()) {
            Random r = new Random();
            mCurrentAnimTime = r.nextInt(mDuration);
        }
        mCurrentFrame = 0;
    }

    public static Bitmap[] prepareAnimation(AnimationDrawable anim, int pixelsPerMeter, float width, float height) {
        int count = anim.getNumberOfFrames();
        Bitmap[] frames = new Bitmap[count];
        for(int i = 0; i < count; i++) {
            frames[i] = Bitmap.createScaledBitmap(((BitmapDrawable) anim.getFrame(i)).getBitmap(),
                    (int)(width*pixelsPerMeter),
                    (int)(height*pixelsPerMeter), true);
        }
        return frames;
    }

    public void recycle() {
        if(mFrames == null) { return; }
        for(int i = 0; i < mFrames.length; i++) {
            mFrames[i].recycle();
        }
        mFrames = null;
    }

    public Bitmap getCurrentBitmap() {
        return mFrames[mCurrentFrame];
    }

    public void update(float dt) {
        long elapsedMillis = (long) (SECOND*dt);
        mCurrentAnimTime += elapsedMillis;
        if(mCurrentAnimTime > mDuration) {
            if(mAnim.isOneShot()) {
                return;
            }
            mCurrentAnimTime = mCurrentAnimTime % mDuration;
        }
        int framDuration = 0;
        for(int i = 0; i < mFrameCount; i++) {
            framDuration += mAnim.getDuration(i);
            if(framDuration > mCurrentAnimTime) {
                mCurrentFrame = i;
                break;
            }
        }
    }

    public static int getAnimationDuration(AnimationDrawable anim) {
        int count = anim.getNumberOfFrames();
        int duration = 0;
        for(int i = 0; i < count; i++) {
            duration += anim.getDuration(i);
        }
        return duration;
    }
}
