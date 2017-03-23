package com.example.manfredi.platformer;

import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.manfredi.platformer.engine.GameEngine;
import com.example.manfredi.platformer.engine.GameView;
import com.example.manfredi.platformer.inputs.VirtualGamePad;

public class MainActivity extends AppCompatActivity {
    GameView mGameView = null;
    GameEngine mGameEngine = null;
    Jukebox mJukebox = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        hideSystemUI();
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        setContentView(R.layout.activity_main);
        mJukebox = new Jukebox(getBaseContext());
        //mJukebox.playForever(Jukebox.LEVEL);
        mGameView = (GameView) findViewById(R.id.gameView);
        mGameEngine = new GameEngine(this, mGameView);
        mGameEngine.setInputManager(new VirtualGamePad(findViewById(R.id.keypad)));
        mGameEngine.loadLevel("TestLevel");
    }

    @Override
    protected void onPause() {
        mGameEngine.pauseGame();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mGameEngine.resumeGame();
        super.onResume();
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
        super.onStop();
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
}










