package com.example.manfredi.platformer.gameobjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.manfredi.platformer.engine.GameEngine;

/**
 * Created by Manfredi on 23/03/2017.
 */

public class DebugTextGameObject extends GameObject {
    private String[] mDebugStrings = null;

    public DebugTextGameObject(GameEngine engine) {
        super(engine, 0f, 0f, 0f, 0f, 0);
    }

    @Override
    public void render(Canvas canvas, Paint paint) {
        int textSize = (int) (mEngine.getPixelsPerMeter()*0.5f);
        int y = textSize;
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setColor(Color.WHITE);
        mDebugStrings = mEngine.getDebugStrings();
        for(String s : mDebugStrings) {
            canvas.drawText(s, 10, y, paint);
            y += textSize;
        }
    }



    @Override
    public void update(float dt) {
        mWorldLocation = mEngine.mPlayer.mWorldLocation;
        updateBounds();
    }
}
