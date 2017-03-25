package com.example.manfredi.platformer.GL;

import android.graphics.PointF;
import android.graphics.RectF;
import android.opengl.Matrix;

/**
 * Created by Manfredi on 25/03/2017.
 */

public class GLViewport {
    private static final float EASE_X = 0.125f;
    private static final float EASE_Y = 0.25f;
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
    private PointF mLookAt = new PointF(0f,0f);
    private int mClippedCount;
    private GLGameObject mTarget = null;

    public GLViewport(final int screenWidth, final int screenHeight, final float metresToShowX, final float metresToShowY){
        mScreenWidth = screenWidth;
        mScreenHeight = screenHeight;
        //mScreenCentreX = mScreenWidth / 2;
        //mScreenCentreY = mScreenHeight / 2;
        //mPixelsPerMetreX = mScreenWidth / metresToShowX;
        //mPixelsPerMetreY = mScreenHeight / metresToShowY;
        //mMetersToShowX = metresToShowX;
        //mMetersToShowY = metresToShowY;
        //mHalfDistX = (mMetersToShowX / 2);
        //mHalfDistY = (mMetersToShowY / 2);
        //mMetersToShowX = metresToShowX + BUFFER;
        //mMetersToShowY = metresToShowY + BUFFER;
        mLookAtX = 0.0f;
        mLookAtY = 0.0f;
        //mCurrentViewportWorldCentre = new PointF(0,0);
        setmetersToShow(metresToShowX, metresToShowY);
    }

    private void setmetersToShow(final float metersToShowX, final float metersToShowY) {
        if(metersToShowX < 1 && metersToShowY < 1) throw new IllegalArgumentException("One of the dimension must be");
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

    public void update(final float dt) {
        if(mTarget != null) {
            synchronized (mLookAt) {
                mLookAt.x += (mTarget.mPos.x - mLookAt.x); //* EASE_X;
                mLookAt.y += (mTarget.mPos.y - mLookAt.y); //* EASE_Y;
            }
        }
        final float NEAR = 0f;
        final float FAR = 1f;
        final float LEFT = mLookAt.x - mHalfDistX;
        final float RIGHT = mLookAt.x + mHalfDistX;
        final float TOP = mLookAt.y + mHalfDistY;
        final float BOTTOM = mLookAtY - mHalfDistY;
        Matrix.orthoM(mViewMatrix, 0, LEFT, RIGHT, BOTTOM, TOP, NEAR, FAR);
    }

    public void lookAt(final PointF pos) {
        mLookAtX = pos.x;
        mLookAtY = pos.y;
    }

    public void lookAt(final float x, final float y) {
        mLookAtX = x;
        mLookAtY = y;
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
