package com.example.manfredi.platformer.inputs;

import java.util.ArrayList;

/**
 * Created by Manfredi on 24/03/2017.
 */

public class CompositeControl extends InputManager {
    private ArrayList<InputManager> mInputs = new ArrayList<>();
    private int mCount = 0;

    public CompositeControl(InputManager... inputs) {
        for(InputManager im : inputs){
            mInputs.add(im);
        }
        mCount = mInputs.size();
    }

    public void setInput(InputManager im) {
        onPause();
        onStop();
        mInputs.clear();
        addInput(im);
    }

    public void addInput(InputManager im){
        mInputs.add(im);
        mCount = mInputs.size();
    }

    @Override
    public void update(float dt) {
        InputManager temp;
        mJump = false;
        mHorizontalFactor = 0.0f;
        mVerticalFactor = 0.0f;
        for(int i = 0; i < mCount; i++){
            temp = mInputs.get(i);
            temp.update(dt);
            mJump = mJump || temp.mJump;
            mHorizontalFactor += temp.mHorizontalFactor;
            mVerticalFactor += temp.mVerticalFactor;
        }
        clampInputs();
    }

    @Override
    public void onStart() {
        for(InputManager im : mInputs){
            im.onStart();
        }
    }

    @Override
    public void onStop() {
        for(InputManager im : mInputs){
            im.onStop();
        }
    }

    @Override
    public void onPause() {
        for(InputManager im : mInputs){
            im.onPause();
        }
    }

    @Override
    public void onResume() {
        for(InputManager im : mInputs){
            im.onResume();
        }
    }
}
