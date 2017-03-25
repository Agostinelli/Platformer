package com.example.manfredi.platformer.engine;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.PointF;

import com.example.manfredi.platformer.App;
import com.example.manfredi.platformer.GL.GLGameObject;
import com.example.manfredi.platformer.HUD;
import com.example.manfredi.platformer.LevelManager;
import com.example.manfredi.platformer.R;
import com.example.manfredi.platformer.Viewport;
import com.example.manfredi.platformer.gameobjects.GameObject;
import com.example.manfredi.platformer.inputs.InputManager;
import com.example.manfredi.platformer.inputs.NullInput;

import java.util.ArrayList;

/**
 * Created by Manfredi on 23/03/2017.
 */

public class GameEngine {
    private static final float SCALE_FACTOR = 0.5f;
    private static final int METERS_TO_SHOW_X = App.getContext().getResources().getInteger(R.integer.METERS_TO_SHOW_X);
    private static final int METERS_TO_SHOW_Y = App.getContext().getResources().getInteger(R.integer.METERS_TO_SHOW_Y);
    private HUD mHUD = null;
    private Activity mActivity;
    private RenderThread mRenderThread = null;
    private UpdateThread mUpdateThread = null;
    private ArrayList<GLGameObject> mGameObjects = new ArrayList<>();
    private ArrayList<GLGameObject> mObjectsToAdd = new ArrayList<GameObject>();
    private ArrayList<GLGameObject> mObjectsToRemove = new ArrayList<GameObject>();
    public GameObject mPlayer = null;
    public InputManager mControl = null;
    private LevelManager mLevelManager = null;
    Viewport mCamera = null;
    private IGameView mGameView = null;


    public GameEngine(Activity activity, IGameView gameView) {
        mActivity = activity;
        mControl = new NullInput();
        mGameView = gameView;
        mCamera = mGameView.createViewport(METERS_TO_SHOW_X, METERS_TO_SHOW_Y, SCALE_FACTOR);
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

        synchronized (mGameObjects) {
            GameObject temp;
            while(!mObjectsToRemove.isEmpty()) {
                temp = mObjectsToRemove.remove(0);
                mGameObjects.remove(temp);
            }
            while (!mObjectsToAdd.isEmpty()) {
                temp = mObjectsToAdd.remove(0);
                addGameObjectNow(temp);
            }
        }


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

    public void addGameObject(final GameObject gameObject) {
        if(isRunning()) {
            mObjectsToAdd.add(gameObject);
        }
        else {
            addGameObjectNow(gameObject);
        }
    }

    public void addGameObjectNow(final GameObject gameObject) {
        mObjectsToRemove.add(gameObject);
    }

    public void removeGameObject(final GameObject gameObject) {
        mObjectsToRemove.add(gameObject);
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

    public boolean isRunning() {
        return mUpdateThread != null && mUpdateThread.isGamePaused();
    }

    public boolean isPaused() {
        return mUpdateThread != null && mUpdateThread.isGamePaused();
    }

    /*public void loadLevel(String levelName) {
        mLevelManager = new LevelManager(this, levelName);
        mGameObjects = mLevelManager.mGameObjects;
        mGameView.setGameObjects(mGameObjects);
        mPlayer = mLevelManager.mPlayer;
        mCamera.lookAt(mPlayer.x(), mPlayer.y());
        //mCamera.setWorldCentre(mPlayer.mWorldLocation);
        mCamera.follow(mPlayer);
    }*/

    public void startGame() {
        stopGame();
        if (mControl != null) {
            mControl.onStart();
        }
        mUpdateThread = new UpdateThread(this);
        mUpdateThread.start();

        mRenderThread = new RenderThread(this);
        mRenderThread.start();
        stopGame();
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
