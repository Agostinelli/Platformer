package com.example.manfredi.platformer;

/**
 * Created by Manfredi on 17/03/2017.
 */

public abstract class InputManager {
    public float mVerticalFactor = 0.0f;
    public float mHorizontalFactor = 0.0f;
    public boolean mIsJumping = false;

    public void onStart() {}
    public void onStop() {}
    public void onPause() {}
    public void onResume() {}
}


