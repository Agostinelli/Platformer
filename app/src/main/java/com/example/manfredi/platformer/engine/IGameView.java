package com.example.manfredi.platformer.engine;

import com.example.manfredi.platformer.GL.GLGameObject;

import java.util.ArrayList;

/**
 * Created by Manfredi on 23/03/2017.
 */

public interface IGameView {
    void setGameObjects(ArrayList<GLGameObject> gameObjects);
    void follow(final GLGameObject go);
}
