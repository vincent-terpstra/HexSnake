package com.example.hex_a_snake.model;

import android.opengl.GLES20;

/**
 *
 */

public class Matrix2 {
    private final float[] matrix = new float[4];

    public Matrix2(){
        identity();
    }

    public void identity(){
        matrix[1] = matrix[2] = 0;
        matrix[0] = matrix[3] = 1;
    }

    public final float[] getMatrix(){
        return  matrix;
    }

    public final void setScreen(int width, int height, float x, float y){
        identity();
        //return 1920, 1080
        matrix[0] =  1 / x;
        matrix[3] =  1 / x *  width / height;

        return;
        /**
        if(x == 0 && y != 0){
            matrix[3] = -2 / y;
            matrix[0] = (2 / y) * (float)height / (float)width;

        } else if( x != 0 ){
            matrix[0] = -1 / x;
            matrix[3] = (1 / x) * (float)width / (float)height;
        }
         */
    }

    public void bind(int uniformMatrixHandle){
        GLES20.glUniformMatrix2fv(uniformMatrixHandle,1,false, matrix, 0);
    }
}
