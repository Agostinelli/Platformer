package com.example.manfredi.platformer.inputs;

import com.example.manfredi.platformer.App;
import com.example.manfredi.platformer.R;

/**
 * Created by Manfredi on 17/03/2017.
 */

public abstract class InputManager {
    public float mVerticalFactor = Float.parseFloat(App.getContext().getString(R.string.VERTICAL_FACTOR));
    public float mHorizontalFactor = Float.parseFloat(App.getContext().getString(R.string.HORIZONTAL_FACTOR));
    public boolean mIsJumping = false;

    public void update(float dt) {}
    public void onStart() {}
    public void onStop() {}
    public void onPause() {}
    public void onResume() {}
}


