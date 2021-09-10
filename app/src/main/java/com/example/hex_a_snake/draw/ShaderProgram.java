package com.example.hex_a_snake.draw;

import android.content.Context;
import android.opengl.GLES20;

import com.example.hex_a_snake.FileHelper;

public class ShaderProgram {
    final int programID;

    final int vertexID;
    final int fragmentID;

    public ShaderProgram(Context context, int vertID, int fragID, final String[] attribs){
        vertexID = loadShader(GLES20.GL_VERTEX_SHADER, context, vertID);
        fragmentID = loadShader(GLES20.GL_FRAGMENT_SHADER, context, fragID);

        programID  = GLES20.glCreateProgram();

        GLES20.glAttachShader(programID, vertexID);
        GLES20.glAttachShader(programID, fragmentID);

        int idx = 0;
        for(String s : attribs){
            GLES20.glBindAttribLocation(programID, idx++, s);
        }

        GLES20.glLinkProgram(programID);
    }

    public int getUniformID(String value){
        return GLES20.glGetUniformLocation(programID, value);
    }

    public void bind(){
        GLES20.glUseProgram(programID);
    }


    private int loadShader(int type, Context context, int resourceID) {
        int shader = GLES20.glCreateShader(type);
        String shaderCode = FileHelper.readTextFileFromRawResource(context, resourceID);

        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        final int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
        if (compileStatus[0] == 0) {
            System.out.println(GLES20.glGetShaderInfoLog(shader));
            GLES20.glDeleteShader(shader);
            shader = 0;
        }

        if (shader == 0) {
            throw new RuntimeException("Error creating shader " + type);
        }

        return shader;
    }
}
