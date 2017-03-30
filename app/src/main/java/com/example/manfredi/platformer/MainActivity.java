package com.example.manfredi.platformer;

import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.manfredi.platformer.engine.GameEngine;
import com.example.manfredi.platformer.engine.IGameView;
import com.example.manfredi.platformer.inputs.Accelerometer;
import com.example.manfredi.platformer.inputs.CompositeControl;
import com.example.manfredi.platformer.inputs.Gamepad;
import com.example.manfredi.platformer.inputs.GamepadListener;
import com.example.manfredi.platformer.inputs.VirtualJoystick;

public class MainActivity extends AppCompatActivity implements android.hardware.input.InputManager.InputDeviceListener {
    IGameView mGameView = null;
    GameEngine mGameEngine = null;
    GamepadListener mGamepadListener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        hideSystemUI();
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        setContentView(R.layout.activity_main);
        mGameView = (IGameView) findViewById(R.id.gameView);
        mGameEngine = new GameEngine(this, mGameView);
        CompositeControl control = new CompositeControl(
                new VirtualJoystick(findViewById(R.id.virtual_joystick)),
                new Gamepad(this),
                new Accelerometer(this)
        );
        mGameEngine.setInputManager(control);
    }

    public void setmGamepadListener(GamepadListener listener) {
        mGamepadListener = listener;
    }

    @Override
    public boolean dispatchGenericMotionEvent(final MotionEvent ev) {
        if(mGamepadListener != null) {
            if(mGamepadListener.dispatchGenericMotionEvent(ev)) {
                return true;
            }
        }
        return super.dispatchGenericMotionEvent(ev);
    }

    @Override
    public boolean dispatchKeyEvent(final KeyEvent ev) {
        if(mGamepadListener != null) {
            if (mGamepadListener.dispatchKeyEvent(ev)) {
                return true;
            }
        }
        return super.dispatchKeyEvent(ev);
    }

    @Override
    protected void onPause() {
        mGameEngine.pauseGame();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isGameControllerConnected()) {
            Toast.makeText(this, R.string.gamepadDet, Toast.LENGTH_LONG).show();
        }
        mGameEngine.resumeGame();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGameEngine.startGame();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGameEngine.stopGame();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGameEngine.stopGame();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(!hasFocus) {
            return;
        }
        hideSystemUI();
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
        }
        else {
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LOW_PROFILE
            );
        }
    }

    public boolean isGameControllerConnected() {
        int[] deviceIds = InputDevice.getDeviceIds();
        for (int deviceId : deviceIds) {
            InputDevice dev = InputDevice.getDevice(deviceId);
            int sources = dev.getSources();
            if(((sources & InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD) ||
            ((sources & InputDevice.SOURCE_JOYSTICK) == InputDevice.SOURCE_JOYSTICK)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onInputDeviceAdded(final int deviceId) {
        Toast.makeText(this, R.string.inpuDeAdd, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onInputDeviceRemoved(final int deviceId) {
        Toast.makeText(this, R.string.inDR, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onInputDeviceChanged(final int deviceId) {
        Toast.makeText(this, R.string.inDeCh, Toast.LENGTH_LONG).show();
    }
}










