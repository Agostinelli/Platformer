package com.example.manfredi.platformer;

/**
 * Created by Manfredi on 05/03/2017.
 */

public class Player extends GameObject {
    Player(float x, float y, int type){
        final int HEIGHT = 2; // TODO move to resources - should not defined here in code
        final int WIDTH = 1;
        mType = type;
        mHeight = HEIGHT;
        mWidth = WIDTH;
        mWorldLocation.x = x;
        mWorldLocation.y = y;
    }

    public void update(long dt) {

    }
}
