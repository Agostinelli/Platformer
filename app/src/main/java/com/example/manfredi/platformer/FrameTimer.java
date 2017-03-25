package com.example.manfredi.platformer;

/**
 * Created by Manfredi on 02/03/2017.
 */

public class FrameTimer {
    private static final String TAG = App.getContext().getString(R.string.FrameTimerTag);
    public static long SECOND_IN_NANOSECONDS = 1000000000;
    public static long MILLISECOND_IN_NANOSECONDS = 1000000;
    public static float NANOSECONDS_TO_MILLISENCONDS = 1.0f / MILLISECOND_IN_NANOSECONDS;
    public static float NANOSECONDS_TO_SECONDS = 1.0f / SECOND_IN_NANOSECONDS;

    private static final long SAMPLE_INTERVAL = (long) (SECOND_IN_NANOSECONDS);
    private long mStartFrameTime = 0;
    private long mElapsedTime = 0;
    private int mFrameCount = 0;
    private long mNanosCount = 0;
    private float mAvgFPS = 0;

    public FrameTimer() {
        reset();
    }

    public void reset() {
        mStartFrameTime = 0;
        mElapsedTime = 0;
        mFrameCount = 0;
        mNanosCount = 0;
    }

    public float tick() {
        mFrameCount++;
        mElapsedTime = System.nanoTime()-mStartFrameTime;
        mNanosCount += mElapsedTime;
        mStartFrameTime = System.nanoTime();
        return mElapsedTime * NANOSECONDS_TO_SECONDS;
    }

    public long getmElapsedNanos() {return mElapsedTime; }
    public long getmElapsedMillis() {
        return (long) (mElapsedTime * NANOSECONDS_TO_MILLISENCONDS);
    }
    public float getElapsedSeconds() {
        return mElapsedTime * NANOSECONDS_TO_SECONDS;
    }

    public long getAverageFPS() {
        if(mNanosCount > SAMPLE_INTERVAL) {
            mAvgFPS = mFrameCount*SECOND_IN_NANOSECONDS / mNanosCount;
            mFrameCount = 0;
            mNanosCount = 0;
        }
        return (long) mAvgFPS;
    }

    public long getCurrentFPS() {
        if (mElapsedTime > 0) {
            return (long) SECOND_IN_NANOSECONDS / mElapsedTime;
        }
        return 0;
    }


}
