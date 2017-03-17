package com.example.manfredi.platformer;

import java.util.HashSet;

/**
 * Created by Manfredi on 05/03/2017.
 */

public abstract class LevelData {
    public static final int BACKGROUND = 0;
    int[][] mTiles;
    public int mHeight;
    public int mWidth;
    public int mTileCount;

    protected int countUniqueTiles() {
        int tileType;
        HashSet<Integer> set = new HashSet<>();
        for(int y = 0; y < mHeight; y++) {
            for(int x = 0; x < mWidth; x++) {
                tileType = mTiles[y][x];
                set.add(tileType);
            }
        }
        return set.size();
    }

    public abstract String getBitmapName(int tileType);
}
