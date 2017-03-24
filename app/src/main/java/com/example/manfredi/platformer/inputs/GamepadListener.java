package com.example.manfredi.platformer.inputs;

import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * Created by Manfredi on 24/03/2017.
 */

public interface GamepadListener {
    boolean dispatchGenericMotionEvent(MotionEvent event);
    boolean dispatchKeyEvent(KeyEvent event);
}
