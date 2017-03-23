package com.example.manfredi.platformer.gameobjects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;

import com.example.manfredi.platformer.App;
import com.example.manfredi.platformer.Jukebox;
import com.example.manfredi.platformer.R;
import com.example.manfredi.platformer.engine.GameEngine;

/**
 * Created by Manfredi on 05/03/2017.
 */

public class GameObject {
    private static final float HALF = Float.parseFloat(App.getContext().getString(R.string.HALF));
    public static Point screenCoord = new Point();
    public static final int HEIGHT = App.getContext().getResources().getInteger(R.integer.height);
    public static final int WIDTH = App.getContext().getResources().getInteger(R.integer.width);
    public static final float PRECISION = Float.parseFloat(App.getContext().getString(R.string.PRECISION));
    public RectF mBounds = new RectF(0.0f, 0.0f, WIDTH, HEIGHT);
    public PointF mWorldLocation = new PointF(0.0f, 0.0f);
    public float mWidth = HEIGHT;
    public float mHeight = WIDTH;
    public boolean mIsVisible = true;
    public int mType = 0;
    protected GameEngine mEngine;
    protected Matrix mTransform = new Matrix();

    private Jukebox mJukebox;

    public GameObject(final GameEngine engine, final float x, final float y, final float width, final float height, final int type) {
        init(engine, x, y, width, height, type);
    }

    public GameObject(final GameEngine engine, final float x, final float y, final int type) {
        init(engine, x, y, WIDTH, HEIGHT, type);
    }

    private void init(final GameEngine engine, final float x, final float y, final float width, final float height, final int type) {
        mJukebox = new Jukebox(engine.getContext());
        mEngine = engine;
        mType = type;
        mHeight = height;
        mWidth = width;
        mWorldLocation.x = x;
        mWorldLocation.y = y;
        updateBounds();
    }

    public void render(Canvas canvas, Paint paint) {
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
        coinPickUp(that);
        healthDamage(that);
    }

    private void healthDamage(GameObject that) {
        if(this.getType() == 4 || that.getType() == 4) {
            Player player;
            if(this.getType() == 1) {
                player = (Player) this;
            }
            player = (Player) that;
            player.decreaseHealth();
            mJukebox.play(Jukebox.HIT);
        }
    }

    private void coinPickUp(GameObject that) {
        if(this.getType() == 3 || that.getType() == 3) {
            Player player;
            GameObject coin;
            if(this.getType() == 1) {
                player = (Player) this;
                coin = that;
            }
            player = (Player) that;
            coin = this;
            player.coinPickedUp();
            mJukebox.play(Jukebox.PICKUP);
            //GameView.mLevelManager.mGameObjects.remove(coin);
            //Log.d("TEST", "COINS: " + player.getCoins());

        }
    }


    //SAT intersection test. http://www.metanetsoftware.com/technique/tutorialA.html
    //returns true on intersection, and sets the least intersecting axis in overlap
    protected static PointF overlap = new PointF(0,0);
    public static boolean getOverlap(GameObject a, GameObject b, PointF overlap) {
        overlap.set(0f, 0f);
        float centerDeltaX = a.mBounds.centerX() - b.mBounds.centerX();
        float halfWidths = (a.mWidth + b.mWidth) * HALF;

        if (Math.abs(centerDeltaX) > halfWidths) return false; //no overlap on x == no collision

        float centerDeltaY = a.mBounds.centerY() - b.mBounds.centerY();
        float halfHeights = (a.mHeight + b.mHeight) * HALF;

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
        int resId = context.getResources().getIdentifier(bitmapName, context.getString(R.string.drawable), context.getPackageName());
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
        if(bitmap == null) {
            throw new Exception(context.getString(R.string.noBitmaperror) + bitmapName);
        }
        bitmap = Bitmap.createScaledBitmap(bitmap, (int)(mWidth*pixelsPerMeter), (int)(mHeight*pixelsPerMeter), false);
        return bitmap;
    }

    public int getType() {
        return mType;
    }
}
