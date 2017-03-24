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
    private int mScreenWidth;
    private int mScreenHeight;
    private int mScreenCentreX;
    private int mScreenCentreY;
    private float mMetresToShowX;
    private float mMetresToShowY;
    private float mHalfDistX;
    private float mHalfDistY;
    private float mLookAtX;
    private float mLookAtY;
    private int mClippedCount;
    private GameObject mTarget = null;
    public volatile int mInViewCount = 0;

    private final static int BUFFER = 2;

    public Viewport(final int screenWidth, final int screenHeight, final float metresToShowX, final float metresToShowY){
        mScreenWidth = screenWidth;
        mScreenHeight = screenHeight;
        mScreenCentreX = mScreenWidth / 2;
        mScreenCentreY = mScreenHeight / 2;
        //mPixelsPerMetreX = mScreenWidth / metresToShowX;
        //mPixelsPerMetreY = mScreenHeight / metresToShowY;
        //mMetresToShowX = metresToShowX;
        //mMetresToShowY = metresToShowY;
        //mHalfDistX = (mMetresToShowX / 2);
        //mHalfDistY = (mMetresToShowY / 2);
        //mMetresToShowX = metresToShowX + BUFFER;
        //mMetresToShowY = metresToShowY + BUFFER;
        mLookAtX = 0.0f;
        mLookAtY = 0.0f;
        //mCurrentViewportWorldCentre = new PointF(0,0);
        setmetersToShow(metresToShowX, metresToShowY);
    }

    private void setmetersToShow(final float metersToShowX, final float metersToShowY) {
        if(metersToShowX < 1 && metersToShowY < 1) throw new IllegalArgumentException("One of the dimension must be");
        mMetresToShowX = metersToShowX;
        mMetresToShowY = metersToShowY;
        if (metersToShowX > 0 && metersToShowY > 0) {

        }
        else if (metersToShowX > 0) {
            mMetresToShowX = ((float) mScreenWidth / mScreenHeight) * metersToShowY;
        }
        else {
            mMetresToShowY = ((float) mScreenHeight / mScreenWidth) * metersToShowX;
        }
        mHalfDistX = (mMetresToShowX+BUFFER) / 2;
        mHalfDistY = (mMetresToShowY+BUFFER) / 2;
        mPixelsPerMetreX = (int)(mScreenWidth / mMetresToShowX);
        mPixelsPerMetreY = (int) (mScreenHeight / mMetresToShowY);
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
