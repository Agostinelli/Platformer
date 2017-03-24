package com.example.manfredi.platformer.engine;

import com.example.manfredi.platformer.FrameTimer;

/**
 * Created by Manfredi on 23/03/2017.
 */

public class UpdateThread extends Thread {

    private final GameEngine mEngine;
    private boolean mIsRunning = true;
    private boolean mIsPaused = false;
    private Object mLock = new Object();
    private FrameTimer mTimer = new FrameTimer();


    public UpdateThread(GameEngine engine) {
        mEngine = engine;
    }


    public long getAverageFPS() {
        return mTimer.getAverageFPS();
    }


    @Override
    public synchronized void start() {
        super.start();
        mIsRunning = true;
        mIsPaused = false;
    }

    @Override
    public void run() {
        super.run();
        mTimer.reset();
        while(mIsRunning) {
            while(mIsPaused) {
                waitUntilResumed();
            }
            mEngine.update(mTimer.tick());
        }
    }

    public void stopGame() {
        mIsRunning = false;
        resumeGame();
    }

    public void pauseGame() {
        mIsPaused = true;
    }

    public void resumeGame() {
        mTimer.reset();
        if (mIsPaused == true) {
            mIsPaused = false;
            synchronized (mLock) {
                mLock.notify();
            }
        }
    }

    public boolean isGameRunning() {
        return mIsRunning;
    }

    public boolean isGamePaused() {
        return mIsPaused;
    }

    private void waitUntilResumed() {
        while(mIsPaused) {
            synchronized (mLock) {
                try {
                    mLock.wait();
                }
                catch (InterruptedException e) {

                }
            }
        }
        mTimer.reset();
    }
}
