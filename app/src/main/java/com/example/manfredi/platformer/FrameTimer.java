package com.example.manfredi.platformer;

/**
 * Created by Manfredi on 02/03/2017.
 */

public class FrameTimer {
    private static final long SECOND = 1000;
    private static final float TO_SECONDS = 1000.0f;
    private long mStartFrameTime = 0;
    private long mElapsedTime = 0;
    private long mFrameCount = 0;
    private long mMeasuringStartedTime = 0;


    public FrameTimer() {
        reset();
    }

    public void reset() {
        mMeasuringStartedTime = System.currentTimeMillis();
        mStartFrameTime = 0;
        mElapsedTime = 0;
        mFrameCount = 0;
    }

    public float onEnterFrame() {
        mFrameCount++;
        mElapsedTime = System.currentTimeMillis()-mStartFrameTime;
        mStartFrameTime = System.currentTimeMillis();
        return mElapsedTime / TO_SECONDS;
    }

    public long getCurrentFPS() {
        if(mElapsedTime > 0) {
            return SECOND / mElapsedTime;
        }
        return 0;
    }


}
