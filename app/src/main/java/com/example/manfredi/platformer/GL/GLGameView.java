package com.example.manfredi.platformer.GL;

import android.content.Context;
import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.example.manfredi.platformer.App;
import com.example.manfredi.platformer.FrameTimer;
import com.example.manfredi.platformer.R;
import com.example.manfredi.platformer.engine.IGameView;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


//import com.example.manfredi.platformer.gameobjects.GameObject;

/**
 * Created by Manfredi on 24/03/2017.
 */

public class GLGameView extends GLSurfaceView implements IGameView, GLSurfaceView.Renderer {
    private static final int BG_COLOR_R = App.getContext().getResources().getInteger(R.integer.BG_COLOR_R);
    private static final int BG_COLOR_G = App.getContext().getResources().getInteger(R.integer.BG_COLOR_G);
    private static final int BG_COLOR_B = App.getContext().getResources().getInteger(R.integer.BG_COLOR_B);
    private static final int BG_COLOR = Color.rgb(BG_COLOR_R, BG_COLOR_G, BG_COLOR_B);
    private ArrayList<GLGameObject> mGameObjects = new ArrayList<>();
    private final static float MAX_COLOR = Float.parseFloat(App.getContext().getString(R.string.max_color));
    private final static int lineWidth = App.getContext().getResources().getInteger(R.integer.line_width);

    private GLViewport mViewport = null;
    int mScreenWidth = 0;
    int mScreenHeight = 0;
    public static float mWorldWidth = Float.parseFloat(App.getContext().getString(R.string.worldWidth));
    public static float mWorldHeight = Float.parseFloat(App.getContext().getString(R.string.worldHeight));
    private FrameTimer mTimer = new FrameTimer();

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
    }


    @Override
    public void onSurfaceCreated(final GL10 unused, final EGLConfig eglConfig) {
        float red = Color.red(BG_COLOR) / MAX_COLOR;
        float green = Color.green(BG_COLOR) / MAX_COLOR;
        float blue = Color.blue(BG_COLOR) / MAX_COLOR;
        float alpha = 1f;
        GLManager.buildProgram();
        GLES20.glClearColor(red, green, blue, alpha);
        GLES20.glLineWidth(lineWidth);
        GLES20.glUseProgram(GLManager.mProgram);
        mTimer.reset();
    }

    @Override
    public void onSurfaceChanged(final GL10 unused, final int width, final int height) {
        GLES20.glViewport(0,0, width, height);
    }

    private void render() {
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
        mViewport.update(dt); //added this
        render();
    }

    @Override
    public void setGameObjects(ArrayList<GLGameObject> gameObjects) {
        mGameObjects = gameObjects;
        int count = mGameObjects.size();
        for(int i = 0; i < count; i++) {
            if(GLViewport.class.isInstance(mGameObjects.get(i))){
                mViewport = (GLViewport) mGameObjects.get(i);
            }
        }
    }

    @Override
    public void follow(final GLGameObject go) {
        if(mViewport != null) {
            mViewport.follow(go);
        }
    }
}
