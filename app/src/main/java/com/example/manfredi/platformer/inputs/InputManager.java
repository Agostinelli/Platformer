package com.example.manfredi.platformer.inputs;

import com.example.manfredi.platformer.App;
import com.example.manfredi.platformer.R;

/**
 * Created by Manfredi on 17/03/2017.
 */

public abstract class InputManager {
    public static final float MIN = -1.0f;
    public static final float MAX = 1.0f;
    public float mVerticalFactor = Float.parseFloat(App.getContext().getString(R.string.VERTICAL_FACTOR));
    public float mHorizontalFactor = Float.parseFloat(App.getContext().getString(R.string.HORIZONTAL_FACTOR));
    public boolean mIsJumping = false;
    public boolean mJump = false;

    protected void clampInputs() {
        if(mVerticalFactor < MIN) {
            mVerticalFactor = MIN;
        }
        else if (mVerticalFactor > MAX) {
            mVerticalFactor = MAX;
        }
        if(mHorizontalFactor < MIN) {
            mHorizontalFactor = MIN;
        }
        else if (mHorizontalFactor > MAX) {
            mHorizontalFactor = MAX;
        }
    }

    public void update(float dt) {}
    public void onStart() {}
    public void onStop() {}
    public void onPause() {}
    public void onResume() {}
}


