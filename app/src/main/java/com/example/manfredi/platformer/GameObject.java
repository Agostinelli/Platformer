package com.example.manfredi.platformer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;

/**
 * Created by Manfredi on 05/03/2017.
 */

public abstract class GameObject {
    public PointF mWorldLocation = new PointF(0.0f, 0.0f);
    public float mWidth;
    public float mHeight;
    public boolean mIsVisible = true;
    public int mType;

    public abstract void update(long deltaTime);

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
