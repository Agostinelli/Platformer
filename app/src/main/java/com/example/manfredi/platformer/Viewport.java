package com.example.manfredi.platformer;

import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

import com.example.manfredi.platformer.gameobjects.GameObject;

/**
 * Created by Manfredi on 02/03/2017.
 */

public class Viewport {
    private PointF mCurrentViewportWorldCentre;
    private int mPixelsPerMetreX;
    private int mPixelsPerMetreY;
    private int mScreenXResolution;
    private int mScreenYResolution;
    private int mScreenCentreX;
    private int mScreenCentreY;
    private int mMetresToShowX;
    private int mMetresToShowY;
    private float mHalfDistX;
    private float mHalfDistY;
    private float mLookAtX;
    private float mLookAtY;
    private int mClippedCount;
    private GameObject mTarget = null;
    public volatile int mInViewCount = 0;

    private final static int BUFFER = 2;

    public Viewport(final int screenWidth, final int screenHeight, final int metresToShowX, final int metresToShowY){
        mScreenXResolution = screenWidth;
        mScreenYResolution = screenHeight;
        mScreenCentreX = mScreenXResolution / 2;
        mScreenCentreY = mScreenYResolution / 2;
        mPixelsPerMetreX = mScreenXResolution / metresToShowX;
        mPixelsPerMetreY = mScreenYResolution / metresToShowY;
        mMetresToShowX = metresToShowX;
        mMetresToShowY = metresToShowY;
        mHalfDistX = (mMetresToShowX / 2);
        mHalfDistY = (mMetresToShowY / 2);
        mMetresToShowX = metresToShowX + BUFFER;
        mMetresToShowY = metresToShowY + BUFFER;
        mLookAtX = 0.0f;
        mLookAtY = 0.0f;
        mCurrentViewportWorldCentre = new PointF(0,0);
    }

    public void setTarget(final GameObject go) {
        mTarget = go;
    }

    public void update(float dt) {
        if(mTarget == null) { return; }
        mInViewCount = 0;
        mCurrentViewportWorldCentre.x += (mTarget.mWorldLocation.x-mCurrentViewportWorldCentre.x)*0.125;
        mCurrentViewportWorldCentre.y += (mTarget.mWorldLocation.y-mCurrentViewportWorldCentre.y)*0.25;
    }

    public int getScreenWidth() {
        return mScreenXResolution;
    }
    public int getScreenHeight(){
        return mScreenYResolution;
    }
    public int getPixelsPerMetreX(){
        return mPixelsPerMetreX;
    }
    public int getClipCount(){
        return mClippedCount;
    }
    public void resetClipCount(){
        mClippedCount = 0;
    }

    public void setWorldCentre(final PointF pos){
        mCurrentViewportWorldCentre.x = pos.x;
        mCurrentViewportWorldCentre.y = pos.y;
    }

    public void worldToScreen(final PointF worldPos, Point screenPos) {
        screenPos.x = (int) (mScreenCentreX - ((mCurrentViewportWorldCentre.x - worldPos.x) * mPixelsPerMetreX));
        screenPos.y = (int) (mScreenCentreY - ((mCurrentViewportWorldCentre.y - worldPos.y) * mPixelsPerMetreY));
    }

    public void worldToScreen(final PointF worldPos, final float objectWidth, final float objectHeight, Rect out){
        int left = (int) (mScreenCentreX - ((mCurrentViewportWorldCentre.x - worldPos.x) * mPixelsPerMetreX));
        int top = (int) (mScreenCentreY - ((mCurrentViewportWorldCentre.y - worldPos.y) * mPixelsPerMetreY));
        int right = (int) (left + (objectWidth * mPixelsPerMetreX));
        int bottom = (int) (top + (objectWidth * mPixelsPerMetreY));
        out.set(left, top, right, bottom);
    }

    public void follow(final GameObject go) {
        mTarget = go;
    }

    public boolean inView(final RectF bounds) {
        float rigth = (mLookAtX + mHalfDistX);
        float left = (mLookAtX - mHalfDistX);
        float bottom = (mLookAtY - mHalfDistY);
        float top = (mLookAtY - mHalfDistY);
        if((bounds.left < rigth && bounds.right > left)
            && (bounds.top < bottom && bounds.bottom > top)) {
            mInViewCount++;
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
            mInViewCount++;
            return true;
        }
        return false;
    }
}
