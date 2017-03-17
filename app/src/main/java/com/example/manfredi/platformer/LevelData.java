package com.example.manfredi.platformer;

import java.util.HashSet;

/**
 * Created by Manfredi on 05/03/2017.
 */

public class LevelData {
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
}
