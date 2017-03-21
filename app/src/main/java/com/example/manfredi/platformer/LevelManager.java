package com.example.manfredi.platformer;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Manfredi on 17/03/2017.
 */

public class LevelManager {
    private static final String TAG = App.getContext().getString(R.string.LevelManagerTag);
    private Bitmap[] mBitmaps;
    ArrayList<GameObject> mGameObjects;
    LevelData mData;
    Player mPlayer;
    GameView mEngine = null;

    LevelManager(final GameView engine, String levelName) {
        mEngine = engine;
        switch (levelName) {
            default:
                mData = new TestLevel();
                break;
        }
        mGameObjects = new ArrayList<>();
        mBitmaps = new Bitmap[mData.mTileCount];
        loadMapAssets();
    }

    public Bitmap getBitmap(int tileType) {
        return mBitmaps[tileType];
    }

    private void loadMapAssets() {
        int tileType;
        GameObject temp;
        for(int y = 0; y < mData.mHeight; y++) {
            int width = mData.mTiles[y].length;
            for(int x = 0; x < width; x++) {
                tileType = mData.mTiles[y][x];
                if (tileType == LevelData.BACKGROUND) {
                    continue;
                }
                temp = createGameObject(tileType, x, y);
                if (temp != null) {
                    loadBitmap(temp, mEngine.getContext(), mEngine.getPixelsPerMeter());
                    mGameObjects.add(temp);
                }
            }
        }
    }

    private GameObject createGameObject(final int tileType,final int x,final int y) {
        GameObject o = null;
        switch (tileType) {
            case 1:
                if(mPlayer != null) {
                    Log.d(TAG, App.getContext().getString(R.string.duplicatePlayerError) + tileType);
                    break;
                }
                mPlayer = new Player(mEngine, x, y, tileType);
                o = mPlayer;
                break;
            case 2:
                o = new GameObject(mEngine, x, y, tileType);
                break;
            case 3:
                o = new GameObject(mEngine, x, y, tileType);
                break;
            case 4:
                o = new GameObject(mEngine, x, y, tileType);
                break;
            default:
                Log.d(TAG, App.getContext().getString(R.string.nogameobjecterror));
                break;
        }
        return o;
    }

    private void loadBitmap(GameObject o, Context context, int pixelsPerMeter) {
        if(mBitmaps[o.mType] != null) {
            return;
        }
        try {
            String bitmapName = mData.getBitmapName(o.mType);
            mBitmaps[o.mType] = o.prepareBitmap(context, bitmapName, pixelsPerMeter);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}










