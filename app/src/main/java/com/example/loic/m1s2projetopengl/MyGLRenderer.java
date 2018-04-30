package com.example.loic.m1s2projetopengl;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.example.loic.m1s2projetopengl.Drawable.Triangle;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyGLRenderer implements GLSurfaceView.Renderer{

    private Triangle mTriangle;


    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];


    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {

        //Set the background frame color
        GLES20.glClearColor(0f,0f,1f,1f);

        //initialize a triangle
        mTriangle = new Triangle();


    }

    @Override
    public void onDrawFrame(GL10 unused) {
        //Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        //set the camera position (View Matrix)
        Matrix.setLookAtM(mViewMatrix,0,0,0,-3,0f,0f,0f,0f,1f,0f);

        //Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0,mProjectionMatrix,0,mViewMatrix,0);

        //draw shape
        mTriangle.draw(mMVPMatrix);
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0,0,width,height);

        float ration = (float) width / height;

        //this projection matrix is applied to object coordinates
        //in the onDrawFrame() method
        Matrix.frustumM(mProjectionMatrix,0,-ration,ration,-1,1,3,7);

    }


    public static int loadShader(int type,String shaderCode){

        //create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        //or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        //add the source code to the shader and compile it
        GLES20.glShaderSource(shader,shaderCode);
        GLES20.glCompileShader(shader);

        return shader;

    }

}
