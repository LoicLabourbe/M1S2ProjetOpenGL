package com.example.loic.m1s2projetopengl.Drawable;

import android.opengl.GLES20;

import com.example.loic.m1s2projetopengl.MyGLRenderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Triangle {



    private final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
            "attribute vec4 vPosition;" +
            "void main() {" +
            // the matrix must be included as a modifier of gl_Position
            // Note that the uMVPMatrix factor *must be first* in order
            // for the matrix multiplication product to be correct.
            "  gl_Position = uMVPMatrix * vPosition;" +
            "}";

    private final String fragmentShaderCode=
            "precision mediump float;"+
            "uniform vec4 vColor;"+
            "void main(){"+
                "gl_FragColor = vColor;"+
            "}";


    // Use to access and set the view transformation
    private int mMVPMatrixHandle;
    private int mPositionHandle;
    private int mColorHandle;

    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride =COORDS_PER_VERTEX * 4; //4  bytes per vertex


    private FloatBuffer vertexBuffer;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;

    static float triangleCoords[] = {
             0.0f,  0.5f, 0f,
            -0.5f, -0.5f, 0f,
             0.5f, -0.5f, 0f
    };

    //set color with red, green blue and alpha (opacity) values
    float color []= {0.63f,0.76f,0.22f,1.0f};


    private final int mProgram;

    public Triangle (){

        //initialize vertex byte buffer for shape coordinates
        //number of coordinate values * 4 bytes per float
        ByteBuffer bb = ByteBuffer.allocateDirect(triangleCoords.length*4);

        //use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        //create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(triangleCoords);
        vertexBuffer.position(0);

        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,fragmentShaderCode);

        //create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram();

        //add the vertex shader to program
        GLES20.glAttachShader(mProgram,vertexShader);

        //add the fragment shader to program
        GLES20.glAttachShader(mProgram,fragmentShader);

        //create OpenGL ES program executables
        GLES20.glLinkProgram(mProgram);

    }

    public void draw(float[] mvpMatrix){

        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // Set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);


        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

}
