package com.example.hex_a_snake.draw;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class QuadBatch {
    static final int VERTEX_PER_SHAPE = 4;
    static final int BYTES_PER_FLOAT = 4;
    static final int BYTES_PER_SHORT = 2;
    final int[] DATA_SIZES;
    final int COORDS_PER_VERTEX;

    private final FloatBuffer vertexBuffer;
    private final ShortBuffer indexBuffer;

    private final int vertexStride;
    protected final float[] vertexArray;

    protected int idx;

    final int vbo;
    final int ibo;

    public QuadBatch(int maxShapes, final int[] DATA_SIZES){

        this.DATA_SIZES = DATA_SIZES;

        int sum = 0;
        for(int i : DATA_SIZES) sum += i;
        this.COORDS_PER_VERTEX = sum;

        vertexArray = new float[COORDS_PER_VERTEX * VERTEX_PER_SHAPE * maxShapes];
        vertexBuffer = ByteBuffer.allocateDirect(vertexArray.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();

        vertexStride = COORDS_PER_VERTEX * 4;
        int[] tmp = new int[1];
        GLES20.glGenBuffers(1, tmp, 0);
        vbo = tmp[0];
        GLES20.glGenBuffers(1, tmp, 0);
        ibo = tmp[0];

        if(vbo <= 0 || ibo <= 0){
            throw new RuntimeException("ERROR creating buffers");
        }

        short[] indexArray = new short[6 * maxShapes];
        short j = 0;
        for(int i = 0; i < indexArray.length; ){
            indexArray[i++] =  j;
            indexArray[i++] = ++j;
            indexArray[i++] = ++j;
            indexArray[i++] = (short) (j-2);
            indexArray[i++] = j;
            indexArray[i++] = ++j;
            ++j;
        }

        indexBuffer = ByteBuffer.allocateDirect(indexArray.length * BYTES_PER_SHORT)
                .order(ByteOrder.nativeOrder())
                .asShortBuffer();
        indexBuffer.put(indexArray);
        indexBuffer.position(0);

        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, ibo);
        GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, indexBuffer.capacity()
                * BYTES_PER_SHORT, indexBuffer, GLES20.GL_STATIC_DRAW);
    }

    public void end(){
        vertexBuffer.position(0);
        vertexBuffer.put(vertexArray)
                .position(0);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vbo);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vertexBuffer.capacity()
                * BYTES_PER_FLOAT, vertexBuffer, GLES20.GL_STATIC_DRAW);

        int offset = 0;
        for(int idx = 0; idx < DATA_SIZES.length; ++idx){
            GLES20.glVertexAttribPointer(
                    idx, DATA_SIZES[idx], GLES20.GL_FLOAT,
                    false, vertexStride, offset * BYTES_PER_FLOAT
            );
            offset += DATA_SIZES[idx];
        }

        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, ibo);

        final int NUM_SHAPES = idx / VERTEX_PER_SHAPE / COORDS_PER_VERTEX;
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, NUM_SHAPES * 6 , GLES20.GL_UNSIGNED_SHORT, 0);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);

        idx = 0;
    }


    public void dispose(){
        final int[] buffersToDelete = new int[] {
          ibo, vbo
        };
        GLES20.glDeleteBuffers(buffersToDelete.length, buffersToDelete, 0);
    }

}
