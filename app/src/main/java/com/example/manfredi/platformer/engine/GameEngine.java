package com.example.manfredi.platformer.engine;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.PointF;

import com.example.manfredi.platformer.HUD;
import com.example.manfredi.platformer.LevelManager;
import com.example.manfredi.platformer.Viewport;
import com.example.manfredi.platformer.gameobjects.GameObject;
import com.example.manfredi.platformer.inputs.InputManager;
import com.example.manfredi.platformer.inputs.NullInput;

import java.util.ArrayList;

/**
 * Created by Manfredi on 23/03/2017.
 */

public class GameEngine {
    private HUD mHUD = null;
    private Activity mActivity;
    private RenderThread mRenderThread = null;
    private UpdateThread mUpdateThread = null;
    private ArrayList<GameObject> mGameObjects = new ArrayList<>();
    public GameObject mPlayer = null;
    public InputManager mControl = null;
    private LevelManager mLevelManager = null;
    Viewport mCamera = null;
    private IGameView mGameView = null;


    public GameEngine(Activity activity, IGameView gameView) {
        mActivity = activity;
        mControl = new NullInput();
        mGameView = gameView;
        mCamera = mGameView.createViewport();
    }


    public void update(float dt) {
        mCamera.update(dt);
        int count = mGameObjects.size();
        for (int i = 0; i < count; i++) {
            mGameObjects.get(i).update(dt);
            //if (mCamera.inView(go.mWorldLocation, go.mWidth, go.mHeight)) {
            //    mActiveEntities.add(go);
            //}
        }
        doCollisionChecks();
    }

    private void doCollisionChecks() {
        int count = mGameObjects.size();
        GameObject a, b;
        for(int i = 0; i < count-1; i++) {
            a = mGameObjects.get(i);
            for(int j = i+1; j < count; j++) {
                b = mGameObjects.get(j);
                if(a.isColliding(b)) {
                    a.onCollision(b);
                    b.onCollision(a);
                }
            }
        }
    }

    public void render() {
        mGameView.render();
    }

    public void setInputManager(InputManager input) {
        mControl = input;
    }
    public int getPixelsPerMeter() { return mCamera.getPixelsPerMetreX(); }
    public Bitmap getBitmap(int tileType) {
        return mLevelManager.getBitmap(tileType);
    }
    public void setScreenCoordinate(final PointF worldLocation, Point screenCoord) {
        mCamera.worldToScreen(worldLocation, screenCoord);
    }
    public Context getContext() {
        return mActivity;
    }

    public String[] getDebugStrings() {
        return new String[]{
                "Frames/s: " + mRenderThread.getAverageFPS(),
                "Ticks/s: " + mUpdateThread.getAverageFPS(),
                "Objects rendered: "  + " / " + mGameObjects.size(),
                mCamera.toString()
        };
    }

    public void loadLevel(String levelName) {
        mLevelManager = new LevelManager(this, levelName);
        mGameObjects = mLevelManager.mGameObjects;
        mGameView.setGameObjects(mGameObjects);
        mPlayer = mLevelManager.mPlayer;
        mCamera.setWorldCentre(mPlayer.mWorldLocation);
        mCamera.setTarget(mPlayer);
    }

    public void startGame() {
        stopGame();
        if (mControl != null) {
            mControl.onStart();
        }
        mUpdateThread = new UpdateThread(this);
        mUpdateThread.start();

        mRenderThread = new RenderThread(this);
        mRenderThread.start();
    }

    public void stopGame() {
        if(mUpdateThread != null) {
            mUpdateThread.stopGame();
        }
        if (mRenderThread != null) {
            mRenderThread.stopGame();
        }
        if (mControl != null) {
            mControl.onStop();
        }
    }

    public void pauseGame() {
        if(mUpdateThread != null) {
            mUpdateThread.pauseGame();
        }
        if(mRenderThread != null) {
            mRenderThread.pauseGame();
        }
        if(mControl != null) {
            mControl.onPause();
        }
    }

    public void resumeGame() {
        if (mUpdateThread != null) {
            mUpdateThread.resumeGame();
        }
        if (mRenderThread != null) {
            mRenderThread.resumeGame();
        }
        if (mControl != null) {
            mControl.onResume();
        }
    }



}
