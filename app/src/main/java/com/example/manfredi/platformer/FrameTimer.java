package com.example.manfredi.platformer;

/**
 * Created by Manfredi on 02/03/2017.
 */

public class FrameTimer {
    private static final long SECOND = App.getContext().getResources().getInteger(R.integer.second);
    private static final float TO_SECONDS = Float.parseFloat(App.getContext().getString(R.string.SECOND));
    private long mStartFrameTime = 0;
    private long mElapsedTime = 0;
    private int mFrameCount = 0;
    private long mMillisCount = 0;
    private float mAvgFPS = 0;


    public FrameTimer() {
        reset();
    }

    public void reset() {
        mStartFrameTime = 0;
        mElapsedTime = 0;
        mFrameCount = 0;
        mMillisCount = 0;
    }

    public float tick() {
        mFrameCount++;
        mElapsedTime = System.currentTimeMillis()-mStartFrameTime;
        mMillisCount += mElapsedTime;
        mStartFrameTime = System.currentTimeMillis();
        return mElapsedTime / TO_SECONDS;
    }

    public int getAverageFPS() {
        if(mMillisCount > SECOND) {
            mAvgFPS = mFrameCount*SECOND / mMillisCount;
            mFrameCount = 0;
            mMillisCount = 0;
        }
        return (int) mAvgFPS;
    }

    public long getCurrentFPS() {
        if(mElapsedTime > 0) {
            return SECOND / mElapsedTime;
        }
        return 0;
    }


}
