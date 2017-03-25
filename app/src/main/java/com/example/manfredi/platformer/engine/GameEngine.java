package com.example.manfredi.platformer.engine;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;

import com.example.manfredi.platformer.App;
import com.example.manfredi.platformer.GL.GLBorder;
import com.example.manfredi.platformer.GL.GLGameObject;
import com.example.manfredi.platformer.GL.GLSpaceship;
import com.example.manfredi.platformer.GL.GLStar;
import com.example.manfredi.platformer.GL.GLViewport;
import com.example.manfredi.platformer.R;
import com.example.manfredi.platformer.inputs.InputManager;
import com.example.manfredi.platformer.inputs.NullInput;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Manfredi on 23/03/2017.
 */

public class GameEngine {
    private static final int METERS_TO_SHOW_X = App.getContext().getResources().getInteger(R.integer.METERS_TO_SHOW_X);
    private static final int METERS_TO_SHOW_Y = App.getContext().getResources().getInteger(R.integer.METERS_TO_SHOW_Y);
    public static final float mWorldWidth = Float.parseFloat(App.getContext().getString(R.string.worldWidth));
    public static final float mWorldHeight = Float.parseFloat(App.getContext().getString(R.string.worldHeight));
    public static final int starCount = App.getContext().getResources().getInteger(R.integer.STAR_COUNT);
    private Activity mActivity;
    private UpdateThread mUpdateThread = null;
    private ArrayList<GLGameObject> mGameObjects = new ArrayList<GLGameObject>();
    private ArrayList<GLGameObject> mObjectsToAdd = new ArrayList<GLGameObject>();
    private ArrayList<GLGameObject> mObjectsToRemove = new ArrayList<GLGameObject>();
    public GLSpaceship mPlayer = null;
    public InputManager mControl = null;
    public GLViewport mCamera = null;
    private IGameView mGameView = null;
    public final static Random RNG = new Random();

    public GameEngine(Activity activity, IGameView gameView) {
        mActivity = activity;
        mControl = new NullInput();
        mGameView = gameView;
        initGame();
    }

    private void initGame() {
        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        int height = Resources.getSystem().getDisplayMetrics().heightPixels;
        mCamera = new GLViewport(width, height, METERS_TO_SHOW_X, METERS_TO_SHOW_Y);
        mCamera.setBounds(0,0,mWorldWidth, mWorldHeight);
        mPlayer = new GLSpaceship(this, mWorldWidth/2, mWorldHeight/2);
        GLBorder border = new GLBorder(mWorldWidth, mWorldHeight);
        mGameObjects.add(border);
        mGameObjects.add(mPlayer);
        mGameObjects.add(mCamera);
        for(int i = 0; i < starCount; i++) {
            int x = RNG.nextInt((int)mWorldWidth);
            int y = RNG.nextInt((int)mWorldHeight);
            mGameObjects.add(new GLStar(x, y));
        }
        mGameView.setGameObjects(mGameObjects);
        mGameView.follow(mPlayer);
    }


    public void update(float dt) {
        mCamera.update(dt);
        int count = mGameObjects.size();
        for (int i = 0; i < count; i++) {
            mGameObjects.get(i).update(dt);
        }
        checkCollisions();

        synchronized (mGameObjects) {
            GLGameObject temp;
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

    private void checkCollisions() {
        return;
    }

    private void addAndRemoveObjects() {
        GLGameObject temp;
        synchronized (mGameObjects) {
            while (!mObjectsToRemove.isEmpty()) {
                temp = mObjectsToRemove.remove(0);
                mGameObjects.remove(temp);
            }
            while(!mObjectsToAdd.isEmpty()) {
                temp = mObjectsToAdd.remove(0);
                addGameObjectNow(temp);
            }
        }
    }

    public void addGameObject(final GLGameObject gameObject) {
        if(isRunning()) {
            mObjectsToAdd.add(gameObject);
        }
        else {
            addGameObjectNow(gameObject);
        }
    }
    public void addGameObjectNow(final GLGameObject gameObject) {
        mObjectsToRemove.add(gameObject);
    }
    public void removeGameObject(final GLGameObject gameObject) {
        mObjectsToRemove.add(gameObject);
    }

    public void setInputManager(InputManager input) {
        mControl = input;
    }
    public Context getContext() {
        return mActivity;
    }


    public boolean isRunning() {
        return mUpdateThread != null && mUpdateThread.isGamePaused();
    }

    public boolean isPaused() {
        return mUpdateThread != null && mUpdateThread.isGamePaused();
    }

    public void onUpdate(final float dt) {
        mControl.update(dt);
        int numObjects = mGameObjects.size();
        for (int i = 0; i < numObjects; i++) {
            mGameObjects.get(i).update(dt);
        }
        checkCollisions();
        addAndRemoveObjects();
    }

    public void startGame() {
        stopGame();
        if (mControl != null) {
            mControl.onStart();
        }
        mUpdateThread = new UpdateThread(this);
        mUpdateThread.start();
    }

    public void stopGame() {
        if(mUpdateThread != null) {
            mUpdateThread.stopGame();
        }
        if (mControl != null) {
            mControl.onStop();
        }
    }

    public void pauseGame() {
        if(mUpdateThread != null) {
            mUpdateThread.pauseGame();
        }
        if(mControl != null) {
            mControl.onPause();
        }
    }

    public void resumeGame() {
        if (mUpdateThread != null) {
            mUpdateThread.resumeGame();
        }
        if (mControl != null) {
            mControl.onResume();
        }
    }



}
