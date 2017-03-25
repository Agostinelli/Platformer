package com.example.manfredi.platformer.GL;

import android.graphics.PointF;
import android.graphics.RectF;
import android.opengl.Matrix;

import com.example.manfredi.platformer.App;
import com.example.manfredi.platformer.R;
import com.example.manfredi.platformer.Utils;

/**
 * Created by Manfredi on 25/03/2017.
 */

public class GLViewport extends GLGameObject {
    private static final float EASE_X = Float.parseFloat(App.getContext().getString(R.string.ease_x));
    private static final float EASE_Y = Float.parseFloat(App.getContext().getString(R.string.ease_y));
    private PointF mCurrentViewportWorldCentre;
    float[] mViewMatrix = new float[4*4];
    private int mScreenWidth;
    private int mScreenHeight;
    private float mMetersToShowX;
    private float mMetersToShowY;
    private float mHalfDistX;
    private float mHalfDistY;
    private float mLookAtX;
    private float mLookAtY;
    private int mClippedCount;
    private GLGameObject mTarget = null;
    private PointF mMinPos = new PointF(0f,0f);
    private PointF mMaxPos = new PointF(Float.MAX_VALUE, Float.MAX_VALUE);

    public GLViewport(final int screenWidth, final int screenHeight, final float metresToShowX, final float metresToShowY){
        setViewport(screenWidth, screenHeight, metresToShowX, metresToShowY);
    }

    public void setViewport(final int screenWidth, final int screenHeight, final float metresToShowX, final float metresToShowY){
        mScreenWidth = screenWidth;
        mScreenHeight = screenHeight;
        mPos.x = 0.0f;
        mPos.y = 0.0f;
        setmetersToShow(metresToShowX, metresToShowY);
    }

    public void setBounds(float minX, float minY, float worldWidth, float worldHeight) {
        mMinPos.x = minX+mHalfDistX;
        mMinPos.y = worldHeight-mHalfDistY;
        mMaxPos.x = worldWidth-mHalfDistX;
        mMaxPos.y = minY+mHalfDistY;
        mPos.x = Utils.clamp(mPos.x, mMinPos.x, mMaxPos.x);
        mPos.y = Utils.clamp(mPos.y, mMaxPos.y, mMinPos.y);
    }

    private void setmetersToShow(final float metersToShowX, final float metersToShowY) {
        if(metersToShowX < 1 && metersToShowY < 1) throw new IllegalArgumentException(App.getContext().getString(R.string.illerr));
        mMetersToShowX = metersToShowX;
        mMetersToShowY = metersToShowY;
        if (metersToShowX > 0 && metersToShowY > 0) {

        }
        else if (metersToShowX > 0) {
            mMetersToShowX = ((float) mScreenWidth / mScreenHeight) * metersToShowY;
        }
        else {
            mMetersToShowY = ((float) mScreenHeight / mScreenWidth) * metersToShowX;
        }
        mHalfDistX = mMetersToShowX / 2;
        mHalfDistY = mMetersToShowY / 2;
    }

    public void follow(final GLGameObject go) {
        mTarget = go;
    }

    public final float[] getViewMatrix() {
        return mViewMatrix;
    }

    @Override
    public void draw(final float[] viewMatrix) {

    }

    @Override
    public void update(final float dt) {
        if(mTarget != null) {
            mPos.x += (mTarget.mPos.x - mPos.x) * EASE_X;
            mPos.y += (mTarget.mPos.y - mPos.y) * EASE_Y;

        }
        mPos.x = Utils.clamp(mPos.x, mMaxPos.x, mMaxPos.x);
        mPos.y = Utils.clamp(mPos.y, mMaxPos.y, mMinPos.y);
        final float NEAR = 0f;
        final float FAR = 1f;
        final float LEFT = mPos.x - mHalfDistX;
        final float RIGHT = mPos.x + mHalfDistX;
        final float TOP = mPos.y + mHalfDistY;
        final float BOTTOM = mLookAtY - mHalfDistY;
        synchronized (mViewMatrix) {
            Matrix.orthoM(mViewMatrix, 0, LEFT, RIGHT, BOTTOM, TOP, NEAR, FAR);
        }
    }

    public void lookAt(final PointF pos) {
        mPos.x = pos.x;
        mPos.y = pos.y;
    }

    public void lookAt(final float x, final float y) {
        mPos.x = x;
        mPos.y = y;
    }

    public int getScreenWidth() {
        return mScreenWidth;
    }
    public int getScreenHeight(){
        return mScreenHeight;
    }
    public int getClipCount(){
        return mClippedCount;
    }
    public void resetClipCount(){
        mClippedCount = 0;
    }
    public float getHorizontalView() { return mMetersToShowX; }
    public float getVerticalView() { return mMetersToShowY; }
    public float getAspectRatio() {
        return mScreenWidth / mScreenHeight;
    }

    public void setWorldCentre(final PointF pos){
        mCurrentViewportWorldCentre.x = pos.x;
        mCurrentViewportWorldCentre.y = pos.y;
    }



    public boolean inView(final RectF bounds) {
        float rigth = (mLookAtX + mHalfDistX);
        float left = (mLookAtX - mHalfDistX);
        float bottom = (mLookAtY - mHalfDistY);
        float top = (mLookAtY - mHalfDistY);
        if((bounds.left < rigth && bounds.right > left)
                && (bounds.top < bottom && bounds.bottom > top)) {
            return true;
        }
        return false;
    }


    public boolean inView(final PointF worldPos, final float objectWidth, final float objectHeight) {
        float maxX = (mCurrentViewportWorldCentre.x + mHalfDistX);
        float minX = (mCurrentViewportWorldCentre.x - mHalfDistX)-objectWidth;
        float maxY = (mCurrentViewportWorldCentre.y + mHalfDistY);
        float minY  = (mCurrentViewportWorldCentre.y - mHalfDistY)-objectHeight;
        if((worldPos.x > minX && worldPos.x < maxX)
                || (worldPos.y > minY && worldPos.y < maxY)){
            return true;
        }
        return false;
    }
}
