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
    private int mY = 32;

    public HUD(Paint mPaint, Canvas mCanvas) {
        this.mPaint = mPaint;
        this.mCanvas = mCanvas;
    }

    public void drawText(String text) {
        int texSize = 32;
        mPaint.setTextSize(texSize);
        mPaint.setTextAlign(Paint.Align.LEFT);
        mPaint.setColor(Color.WHITE);
        mCanvas.drawText(text, 10, mY, mPaint);
        mY+=texSize;
    }

    public void displayHealth(Player player) {
        drawText("Health: " + player.getHealth());
    }
}
