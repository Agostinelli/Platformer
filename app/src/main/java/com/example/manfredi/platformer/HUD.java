package com.example.manfredi.platformer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Manfredi on 20/03/2017.
 */

public class HUD {
    private Paint mPaint = null;
    private Canvas mCanvas = null;
    private int mY = App.getContext().getResources().getInteger(R.integer.testPositionY);
    private int mTextSize = App.getContext().getResources().getInteger(R.integer.textSize);
    private int mIndex = App.getContext().getResources().getInteger(R.integer.index);

    public HUD(Paint mPaint, Canvas mCanvas) {
        this.mPaint = mPaint;
        this.mCanvas = mCanvas;
    }

    public void drawText(String text) {
        mPaint.setTextSize(mTextSize);
        mPaint.setTextAlign(Paint.Align.LEFT);
        mPaint.setColor(Color.WHITE);
        mCanvas.drawText(text, mIndex, mY, mPaint);
        mY+=mTextSize;
    }

    public void displayHealth(Player player) {
        drawText(App.getContext().getString(R.string.health) + player.getHealth());
    }
}
