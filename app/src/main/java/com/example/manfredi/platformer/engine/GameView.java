package com.example.manfredi.platformer.engine;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.manfredi.platformer.App;
import com.example.manfredi.platformer.HUD;
import com.example.manfredi.platformer.Jukebox;
import com.example.manfredi.platformer.R;
import com.example.manfredi.platformer.Viewport;
import com.example.manfredi.platformer.gameobjects.GameObject;

import java.util.ArrayList;

/**
 * Created by Manfredi on 01/03/2017.
 */

public class GameView extends SurfaceView implements IGameView {
    private static final String TAG = App.getContext().getString(R.string.gameViewTag);
    private ArrayList<GameObject> mGameObjects = new ArrayList<>();
    private static final int BG_COLOR = Color.rgb(135, 206, 235);//sky blue

    private Canvas mCanvas = null;
    private SurfaceHolder mSurfaceHolder = null;
    Context mContext = null;
    private Paint mPaint = null;
    private Viewport mCamera = null;


    ArrayList<GameObject> mCoins = null;
    HUD mHUD = null;
    private Jukebox mJukebox;

    private static final int coinType = App.getContext().getResources().getInteger(R.integer.coinType);
    private static final int METERS_TO_SHOW_X = App.getContext().getResources().getInteger(R.integer.METERS_TO_SHOW_X);
    private static final int METERS_TO_SHOW_Y = App.getContext().getResources().getInteger(R.integer.METERS_TO_SHOW_Y);
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

    public void setGameObjects(ArrayList<GameObject> gameObjects) {
        mGameObjects = gameObjects;
    }


    private void init(Context context) {
        mJukebox = new Jukebox(context);
        mCoins = new ArrayList<GameObject>();
        mContext = context;

        mPaint = new Paint();
        mSurfaceHolder = getHolder();
        createViewport();
    }



    public Viewport createViewport() {
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        if (SCALE_CONTENT) {
            mSurfaceHolder.setFixedSize(STAGE_WIDTH, STAGE_HEIGHT);
            screenWidth = STAGE_WIDTH;
            screenHeight = STAGE_HEIGHT;
        }
        mCamera = new Viewport(screenWidth, screenHeight, METERS_TO_SHOW_X, METERS_TO_SHOW_Y);
        return mCamera;
    }



    public void render() {
        if (!lockAndSetCanvas()) {
            return;
        }
        mCanvas.drawColor(BG_COLOR);
        mPaint.setColor(Color.WHITE);
        GameObject temp;
        for(GameObject go : mGameObjects) {
            temp = go;
            if(mCamera.inView(temp.mBounds)) {
                temp.render(mCanvas, mPaint);
            }
            go.render(mCanvas, mPaint);
        }
        // HUD();
        mSurfaceHolder.unlockCanvasAndPost(mCanvas);
    }

    private void HUD() {
        //mHUD = new HUD(mPaint, mCanvas);
        //mHUD.displayHealth(mLevelManager.mPlayer);
        //mHUD.drawText(getContext().getString(R.string.collectibles) + mLevelManager.mPlayer.getCoins() + "/" + mCoins.size());
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
}
