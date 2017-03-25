package com.example.manfredi.platformer.GL;

import android.content.Context;
import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.util.AttributeSet;

import com.example.manfredi.platformer.FrameTimer;
import com.example.manfredi.platformer.Viewport;
import com.example.manfredi.platformer.engine.IGameView;
import com.example.manfredi.platformer.gameobjects.GameObject;

import java.util.ArrayList;
import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


//import com.example.manfredi.platformer.gameobjects.GameObject;

/**
 * Created by Manfredi on 24/03/2017.
 */

public class GLGameView extends GLSurfaceView implements IGameView, GLSurfaceView.Renderer {
    private static final int BG_COLOR = Color.rgb(135, 206, 235);//sky blue
    private static final int METERS_TO_SHOW_X = 0;
    private static final int METERS_TO_SHOW_Y = 180;
    private GLGameObject go = null;

    private ArrayList<GLGameObject> mGameObjects = new ArrayList<>();


    private GLViewport mViewport = null;
    int mScreenWidth = 0;
    int mScreenHeight = 0;
    public static float mWorldWidth = 1280f;
    public static float mWorldHeight = 720f;
    private FrameTimer mTimer = new FrameTimer();
    private Handler handler = new Handler();


    public GLGameView(Context context) {
        super(context);
        init();
    }

    public GLGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setEGLContextClientVersion(2);
        setRenderer(this);

        Random r = new Random();
        mGameObjects.add(go);
        int starCount = 500;
        go = new GLSpaceship(mWorldWidth/2, mWorldHeight/2);
        GLBorder border = new GLBorder(mWorldWidth, mScreenHeight);
        mGameObjects.add(border);
        mGameObjects.add(go);
        for(int i = 0; i < starCount; i++) {
            int x = r.nextInt((int)mWorldWidth);
            int y = r.nextInt((int)mWorldHeight);
            mGameObjects.add(new GLStar(x, y));
        }
    }

    @Override
    public void setGameObjects(ArrayList<GameObject> gameObjects) {

    }

    @Override
    public Viewport createViewport(float metersToShowX, float meterstoShowY, float scaleFactor) {
        return null;
    }



    @Override
    public void onSurfaceCreated(final GL10 unused, final EGLConfig eglConfig) {
        float red = Color.red(BG_COLOR) / 255f;
        float green = Color.green(BG_COLOR) / 255f;
        float blue = Color.blue(BG_COLOR) / 255f;
        float alpha = 1f;
        GLManager.buildProgram();
        GLES20.glClearColor(red, green, blue, alpha);
        GLES20.glLineWidth(10);
        GLES20.glUseProgram(GLManager.mProgram);
        mTimer.reset();
    }

    @Override
    public void onSurfaceChanged(final GL10 unused, final int width, final int height) {
        mViewport = new GLViewport(width, height, METERS_TO_SHOW_X, METERS_TO_SHOW_Y);
        mViewport.follow(go);
        GLES20.glViewport(0,0, width, height);
        /*final int delay = 500;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("Avg fps", "" + mTimer.getAverageFPS());
                handler.postDelayed(this, delay);
            }
        }, delay);*/
        //handler.removeCallbacks();
    }

    public void update(float dt) {
        mViewport.update(dt);
        int count = mGameObjects.size();
        for(int i = 0; i < count; i++) {
            mGameObjects.get(i).update(dt);
        }
    }

    @Override
    public void render() {
        float[] viewMatrix = mViewport.getViewMatrix();
        int count = mGameObjects.size();
        for(int i = 0; i < count; i++) {
            mGameObjects.get(i).draw(viewMatrix);
        }
    }

    @Override
    public void onDrawFrame(final GL10 unused) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        float dt = mTimer.tick();
        update(dt);
        render();
    }
}
