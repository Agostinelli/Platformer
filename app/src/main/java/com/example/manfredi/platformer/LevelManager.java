package com.example.manfredi.platformer;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Manfredi on 17/03/2017.
 */

public class LevelManager {
    private static final String TAG = "LevelManager";
    private Bitmap[] mBitmaps;
    ArrayList<GameObject> mGameObjects;
    LevelData mData;
    Player mPlayer;

    LevelManager(Context context, int pixelsPerMeter, String levelName) {
        switch (levelName) {
            default:
                mData = new TestLevel();
                break;
        }
        mGameObjects = new ArrayList<>();
        mBitmaps = new Bitmap[mData.mTileCount];
        loadMapAssets(context, pixelsPerMeter);
    }

    public Bitmap getBitmap(int tileType) {
        return mBitmaps[tileType];
    }

    private void loadMapAssets(Context context, int pixelsPerMeter) {
        int tileType;
        GameObject temp;
        for(int y = 0; y < mData.mHeight; y++) {
            int width = mData.mTiles[y].length;
            for(int x = 0; x < mData.mWidth; x++) {
                tileType = mData.mTiles[y][x];
                if (tileType == LevelData.BACKGROUND) {
                    continue;
                }
                temp = createGameObject(tileType, x, y);
                if (temp != null) {
                    loadBitmap(temp, context, pixelsPerMeter);
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
                    Log.d(TAG, "BAD LevelData - duplicate player definition!" + tileType);
                    break;
                }
                mPlayer = new Player(x, y, tileType);
                o = mPlayer;
                break;
            case 2:
                o = new GroundTile(x, y, tileType);
                break;
            default:
                Log.d(TAG, "NO game object");
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










