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

import java.util.ArrayList;

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
    ArrayList<GameObject> mActiveEntities = null;

    private static final int METERS_TO_SHOW_X = 16; //TODO: move to xml
    private static final int METERS_TO_SHOW_Y = 9;
    private static final int STAGE_WIDTH = 1920/3;
    private static final int STAGE_HEIGHT = 1080/3;
    private static final boolean SCALE_CONTENT = true;

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
        mActiveEntities = new ArrayList<GameObject>();
        mContext = context;
        mPaint = new Paint();
        mSurfaceHolder = getHolder();
        mTimer = new FrameTimer();
        mControl = new NullInput();
        createViewport();
        loadLevel("TestLevel");
    }

    public int getPixelsPerMeter() { return mCamera.getPixelsPerMetreX(); }
    public void setInputManager(InputManager input) { mControl = input; }

    public Bitmap getBitmap(int tileType) {
        return mLevelManager.getBitmap(tileType);
    }
    public void setScreenCoordinate(final PointF worldLocation, Point screenCoord) {
        mCamera.worldToScreen(worldLocation, screenCoord);
    }

    public void createViewport() {
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        if (SCALE_CONTENT) {
            mSurfaceHolder.setFixedSize(STAGE_WIDTH, STAGE_HEIGHT);
            screenWidth = STAGE_WIDTH;
            screenHeight = STAGE_HEIGHT;
        }
        mCamera = new Viewport(screenWidth, screenHeight, METERS_TO_SHOW_X, METERS_TO_SHOW_Y);
    }

    private void loadLevel(String levelName) {
        mLevelManager = new LevelManager(this, levelName);
        mCamera.setWorldCentre(mLevelManager.mPlayer.mWorldLocation);
        mCamera.setTarget(mLevelManager.mPlayer);
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

    private void update(float secondsPassed) {
        mActiveEntities.clear();
        mCamera.update(secondsPassed);
        for(GameObject go : mLevelManager.mGameObjects) {
            go.update(secondsPassed);
            if(mCamera.inView(go.mWorldLocation, go.mWidth, go.mHeight)) {
                mActiveEntities.add(go);
            }
        }
        doCollisionChecks();
    }

    private void doCollisionChecks() {
        Player player = mLevelManager.mPlayer;
        int count = mActiveEntities.size();
        GameObject a, b;
        for(int i = 0; i < count-1; i++) {
            a = mActiveEntities.get(i);
            for(int j = i+1; j < count; j++) {
                b = mActiveEntities.get(j);
                if(a.isColliding(b)) {
                    a.onCollision(b);
                    b.onCollision(a);
                }
            }
        }
    }

    private void render() {
        if (!lockAndSetCanvas()) {
            return;
        }
        mCanvas.drawColor(BG_COLOR);
        mPaint.setColor(Color.WHITE);
        for(GameObject go : mActiveEntities) {
            go.render(mCanvas, mPaint);
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
        mCanvas.drawText("Rendered Sprites: " + mActiveEntities.size() + "/" + mLevelManager.mGameObjects.size(), 10, y, mPaint);
        y+=texSize;
        mCanvas.drawText("[" + mControl.mHorizontalFactor + " : " + mControl.mVerticalFactor + "] isJumping: " + mControl.mIsJumping, 10, y, mPaint);
        y+=texSize;
        //mCanvas.drawText("Player: [" + playerPos.x + " , " + playerPos.y + "]", 10, y, mPaint);
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
