package com.example.manfredi.platformer;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Manfredi on 20/03/2017.
 */

public class Jukebox {
    private SoundPool mSoundPool;
    private final int MAX_STREAMS = 5;
    public static int LEVEL;
    public static int JUMP;
    public static int HIT;
    public static int PICKUP;


    public Jukebox(Context context) {
        mSoundPool = createSoundPool();
        loadSounds(context);
    }

    public void play(final int soundID) {
        float letfVolume = 1.0f;
        float rightVolume = 1.0f;
        int priority = 1;
        int loop = 0; // -1 loop forever, 0 once
        float rate = 1.0f;
        if (soundID > 0) {
            mSoundPool.play(soundID, letfVolume, rightVolume, priority, loop, rate);
        }
    }

    public void playForever(final int soundID) {
        float letfVolume = 1.0f;
        float rightVolume = 1.0f;
        int priority = 1;
        int loop = -1; // -1 loop forever, 0 once
        float rate = 1.0f;
        if (soundID > 0) {
            mSoundPool.play(soundID, letfVolume, rightVolume, priority, loop, rate);
        }
    }

    private void loadSounds(Context context) {
        try {
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;
            descriptor = assetManager.openFd("level.ogg");
            LEVEL = mSoundPool.load(descriptor, 0);
            descriptor = assetManager.openFd("jump.ogg");
            JUMP = mSoundPool.load(descriptor, 0);
            descriptor = assetManager.openFd("hit.ogg");
            HIT = mSoundPool.load(descriptor, 0);
            descriptor = assetManager.openFd("pickup.ogg");
            PICKUP = mSoundPool.load(descriptor, 0);
        }
        catch (IOException e) {
            Log.e("TAG", e.getMessage());
        }
    }

    private SoundPool createSoundPool() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return createNewSoundPool();
        }

        return createOldSoundPool();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private SoundPool createNewSoundPool() {
        AudioAttributes attr = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        return new SoundPool.Builder()
                .setAudioAttributes(attr)
                .setMaxStreams(MAX_STREAMS)
                .build();
    }

    @SuppressWarnings("deprecation")
    private SoundPool createOldSoundPool() {
        return new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
    }
}
