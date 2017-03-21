package com.example.manfredi.platformer;

import android.app.Application;
import android.content.Context;

/**
 * Created by Manfredi on 21/03/2017.
 */

public class App extends Application {
    private static Context mContext;

        @Override
        public void onCreate() {
            super.onCreate();
            mContext = this;
        }

        public static Context getContext(){
            return mContext;
        }
    }
