package com.example.manfredi.platformer.GL;

import android.opengl.GLES20;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by Manfredi on 24/03/2017.
 */

public class GLModel {
    private static final String TAG = "GLModel";
    public static final int SIZE_OF_FLOAT = 4;
    public static final int COORDS_PER_VERTEX = 3;
    public int mVertexCount = 0;
    public final static int STRIDE = COORDS_PER_VERTEX * SIZE_OF_FLOAT;
    public FloatBuffer mVertexBuffer = null;
    public int mDrawMode = GLES20.GL_TRIANGLES;
    float mColor[] = { 1f, 1f, 1f, 1.0f };

    public GLModel(final float[] geometry) {
        init(geometry, mColor, mDrawMode);
    }
    public GLModel(final float[] geometry, final float[] colors, final int primitive) {
        init(geometry, colors, primitive);
    }
    public GLModel(final float[] geometry, final int primitive) {
        init(geometry, mColor, primitive);
    }
    public GLModel(final float[] geometry, final float[] colors) {
        init(geometry, colors, mDrawMode);
    }

    private void init(final float[] geometry, final float[] colors, final int primitive) {
        setColors(colors);
        setVertices(geometry);
        setPrimitive(primitive);
    }

    private void setPrimitive(int primitiveType) {
        if(primitiveType != GLES20.GL_TRIANGLES && primitiveType != GLES20.GL_LINES && primitiveType != GLES20.GL_POINTS) {
            Log.d(TAG, "uknown primitive type " + primitiveType);
            primitiveType = GLES20.GL_TRIANGLES;
        }
        mDrawMode = primitiveType;
    }

    private void setVertices(final float[] geometry) {
        mVertexCount = geometry.length / COORDS_PER_VERTEX;
        mVertexBuffer = ByteBuffer.allocateDirect(geometry.length * SIZE_OF_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        mVertexBuffer.put(geometry);
        mVertexBuffer.position(0);
    }

    public void setColors(final float[] colors) {
        if(colors != null) {
            mColor = colors;
        }
    }

    public void setColors(final float r, final float g, final float b, final float a) {
        mColor[0] = r;
        mColor[1] = g;
        mColor[2] = b;
        mColor[3] = a;
    }
}
