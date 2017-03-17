package com.example.manfredi.platformer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Manfredi on 01/03/2017.
 */

public class GameView extends SurfaceView implements Runnable {
    private static final String TAG = "GameView";
    private static final int BG_COLOR = Color.rgb(135, 206, 235);//sky blue

    private volatile boolean mIsRunning = false;
    private Thread mGameThread = null;
    private Canvas mCanvas;
    private SurfaceHolder mSurfaceHolder;
    Context mContext = null;
    Paint mPaint = null;

    private boolean mDebugging = true;

    FrameTimer mTimer;
    Viewport mCamera;


    private static final int METERS_TO_SHOW_X = 32; //TODO: refactor to xml
    private static final int METERS_TO_SHOW_Y = 18;
    private static final int STAGE_WIDTH = 1920/3;
    private static final int STAGE_HEIGHT = 1080/3;
    private static final boolean SCALE_CONTENT = true;

    public GameView(final Context context) {
        super(context);
        mContext = context;
        mPaint = new Paint();
        mSurfaceHolder = getHolder();
        mTimer = new FrameTimer();
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        if (SCALE_CONTENT) {
            mSurfaceHolder.setFixedSize(STAGE_WIDTH, STAGE_HEIGHT);
            screenWidth = STAGE_WIDTH;
            screenHeight = STAGE_HEIGHT;
        }
        mCamera = new Viewport(screenWidth, screenHeight, METERS_TO_SHOW_X, METERS_TO_SHOW_Y);
    }

    public void pause() {
        mIsRunning = false;
        try {
            mGameThread.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        mIsRunning = true;
        mGameThread = new Thread(this);
        try {
            mGameThread.start();
        }
        catch(IllegalThreadStateException e) {
            e.printStackTrace();
        }
    }

    public void destroy() {

    }

    private void update(long dt) {
        //mCamera.setWorldCentre();
    }

    private void render() {
        if (!lockAndSetCanvas()) {
            return;
        }
        mCanvas.drawColor(BG_COLOR);
        mPaint.setColor(Color.WHITE);

        if(mDebugging) {
            doDebugDrawing();
        }
        mSurfaceHolder.unlockCanvasAndPost(mCanvas);
    }

    private void doDebugDrawing() {
        mPaint.setTextSize(36);
        mPaint.setTextAlign(Paint.Align.LEFT);
        mPaint.setColor(Color.WHITE);
        mCanvas.drawText("FPS: " + mTimer.getCurrentFPS(), 10, 60, mPaint);
    }

    private boolean lockAndSetCanvas() {
        if(!mSurfaceHolder.getSurface().isValid()) {
            return false;
        }
        mCanvas = mSurfaceHolder.lockCanvas();
        if(mCanvas == null) {
            return false;
        }
        return true;
    }

    @Override
    public void run() {
        mTimer.reset();
        while(mIsRunning) {
            update(mTimer.onEnterFrame());
            render();
            //mTimer.endFrame();
        }
    }

}
