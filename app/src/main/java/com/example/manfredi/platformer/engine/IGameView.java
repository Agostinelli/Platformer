package com.example.manfredi.platformer.engine;

import com.example.manfredi.platformer.Viewport;
import com.example.manfredi.platformer.gameobjects.GameObject;

import java.util.ArrayList;

/**
 * Created by Manfredi on 23/03/2017.
 */

public interface IGameView {
    public void setGameObjects(ArrayList<GameObject> gameObjects);
    public Viewport createViewport(float metersToShowX, float meterstoShowY, float scaleFactor);
    public void render();
}
