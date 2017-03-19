package com.example.manfredi.platformer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;

/**
 * Created by Manfredi on 05/03/2017.
 */

public class GameObject {
    public static Point screenCoord = new Point();
    public static final int HEIGHT = 1;
    public static final int WIDTH = 1;
    public static final float PRECISION = 100.0f;
    public RectF mBounds = new RectF(0.0f, 0.0f, WIDTH, HEIGHT);
    public PointF mWorldLocation = new PointF(0.0f, 0.0f);
    public float mWidth = HEIGHT;
    public float mHeight = WIDTH;
    public boolean mIsVisible = true;
    public int mType = 0;
    protected GameView mEngine;
    protected Matrix mTransform = new Matrix();

    GameObject(final GameView engine, final float x, final float y, final float width, final float height, final int type) {
        init(engine, x, y, width, height, type);
    }

    GameObject(final GameView engine, final float x, final float y, final int type) {
        init(engine, x, y, WIDTH, HEIGHT, type);
    }

    private void init(final GameView engine, final float x, final float y, final float width, final float height, final int type) {
        mEngine = engine;
        mType = type;
        mHeight = height;
        mWidth = width;
        mWorldLocation.x = x;
        mWorldLocation.y = y;
        updateBounds();
    }

    public void render(Canvas canvas,Paint paint) {
        mTransform.reset();
        mEngine.setScreenCoordinate(mWorldLocation, GameObject.screenCoord);
        mTransform.postTranslate(GameObject.screenCoord.x, GameObject.screenCoord.y);
        canvas.drawBitmap(mEngine.getBitmap(mType), mTransform, paint);
    }

    public void update(float dt) {

    }

    public boolean isColliding(final GameObject that) {
        return RectF.intersects(this.mBounds, that.mBounds);
    }

    public void onCollision(final GameObject that) {

    }

    //SAT intersection test. http://www.metanetsoftware.com/technique/tutorialA.html
//returns true on intersection, and sets the least intersecting axis in overlap
    protected static PointF overlap = new PointF(0,0);
    public static boolean getOverlap(GameObject a, GameObject b, PointF overlap) {
        overlap.set(0f, 0f);
        float centerDeltaX = a.mBounds.centerX() - b.mBounds.centerX();
        float halfWidths = (a.mWidth + b.mWidth) * 0.5f;

        if (Math.abs(centerDeltaX) > halfWidths) return false; //no overlap on x == no collision

        float centerDeltaY = a.mBounds.centerY() - b.mBounds.centerY();
        float halfHeights = (a.mHeight + b.mHeight) * 0.5f;

        if (Math.abs(centerDeltaY) > halfHeights) return false; //no overlap on y == no collision

        float dx = halfWidths - Math.abs(centerDeltaX); //overlap on x
        float dy = halfHeights - Math.abs(centerDeltaY); //overlap on y
        if (dy < dx) {
            overlap.y = (centerDeltaY < 0) ? -dy : dy;
        } else if (dy > dx) {
            overlap.x = (centerDeltaX < 0) ? -dx : dx;
        } else {
            overlap.x = (centerDeltaX < 0) ? -dx : dx;
            overlap.y = (centerDeltaY < 0) ? -dy : dy;
        }
        return true;
    }

    public void updateBounds() {
        mWorldLocation.x = Math.round(mWorldLocation.x * PRECISION) / PRECISION;
        mWorldLocation.y = Math.round(mWorldLocation.y * PRECISION) / PRECISION;
        mBounds.left = mWorldLocation.x;
        mBounds.top = mWorldLocation.y;
        mBounds.right = mWorldLocation.x+mWidth;
        mBounds.bottom = mWorldLocation.y+mHeight;
    }

    public Bitmap prepareBitmap(Context context, String bitmapName, int pixelsPerMeter) throws Exception {
        int resId = context.getResources().getIdentifier(bitmapName, "drawable", context.getPackageName());
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
        if(bitmap == null) {
            throw new Exception("No bitmap named " + bitmapName);
        }
        bitmap = Bitmap.createScaledBitmap(bitmap, (int)(mWidth*pixelsPerMeter), (int)(mHeight*pixelsPerMeter), false);
        return bitmap;
    }
}
