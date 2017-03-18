package com.example.manfredi.platformer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
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

    FrameTimer mTimer = null;
    Viewport mCamera = null;
    LevelManager mLevelManager = null;
    InputManager mControl = null;

    private static final int METERS_TO_SHOW_X = 16; //TODO: move to xml
    private static final int METERS_TO_SHOW_Y = 9;
    private static final int STAGE_WIDTH = 1920/3;
    private static final int STAGE_HEIGHT = 1080/3;
    private static final boolean SCALE_CONTENT = true;

    PointF mCameraPos = new PointF(0,0);
    float mScrollSpeed = 2.0f; //meters per second


    public GameView(Context context) {
        super(context);
        init(context);
    }
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
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
        loadLevel("TestLevel");
    }

    public void setInputManager(InputManager input) {
        mControl = input;
    }

    private void loadLevel(String levelName) {
        mLevelManager = new LevelManager(mContext, mCamera.getPixelsPerMetreX(), levelName);

        mCameraPos.x = mLevelManager.mPlayer.mWorldLocation.x;
        mCameraPos.y = mLevelManager.mPlayer.mWorldLocation.y;

        mCamera.setWorldCentre(mCameraPos);

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
        for(GameObject go : mLevelManager.mGameObjects) {
            go.update(dt);
            go.mIsVisible = mCamera.inView(go.mWorldLocation, go.mWidth, go.mHeight);
        }

        float secondsPassed = dt/1000.0f;
        mCameraPos.x += (mControl.mHorizontalFactor*mScrollSpeed)*secondsPassed;
        mCameraPos.y += (mControl.mVerticalFactor*mScrollSpeed)*secondsPassed;

        mCamera.setWorldCentre(mCameraPos);
    }

    private void render() {
        if (!lockAndSetCanvas()) {
            return;
        }
        Point screenCoord = new Point();
        mCanvas.drawColor(BG_COLOR);
        mPaint.setColor(Color.WHITE);

        for(GameObject go : mLevelManager.mGameObjects) {
            if(!go.mIsVisible) {
                //continue;
            }
            mCamera.worldToScreen(go.mWorldLocation, screenCoord);
            Bitmap b = mLevelManager.getBitmap(go.mType);
            mCanvas.drawBitmap(b, screenCoord.x, screenCoord.y, mPaint);
        }

        if(mDebugging) {
            doDebugDrawing();
        }
        mSurfaceHolder.unlockCanvasAndPost(mCanvas);
    }

    private void doDebugDrawing() {
        int y = 60;
        int texSize = 32;
        mPaint.setTextSize(texSize);
        mPaint.setTextAlign(Paint.Align.LEFT);
        mPaint.setColor(Color.WHITE);
        mCanvas.drawText("FPS: " + mTimer.getCurrentFPS(), 10, y, mPaint);
        y+=texSize;
        mCanvas.drawText("Sprites: " + mLevelManager.mGameObjects.size(), 10, y, mPaint);
        y+=texSize;
        mCanvas.drawText("Clipped: " + mCamera.getClipCount(), 10, y, mPaint);
        y+=texSize;

        mCanvas.drawText("[" + mControl.mHorizontalFactor + " : " + mControl.mVerticalFactor + "] isJumping: " + mControl.mIsJumping, 10, y, mPaint);
        mCamera.resetClipCount();
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
