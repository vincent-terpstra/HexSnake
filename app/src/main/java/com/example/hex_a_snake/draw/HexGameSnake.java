package com.example.hex_a_snake.draw;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import com.example.hex_a_snake.R;
import com.example.hex_a_snake.model.HexXYZ;
import com.example.hex_a_snake.model.Matrix2;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class HexGameSnake implements GLSurfaceView.Renderer {
    //endgame
    //undo's
    //icons
    private static final float BOARDWIDTH = 2.8f;


    public static void resetControl(){
    }

    final Context context;

    public HexGameSnake(
            Context context
    ){
        this.context = context;

    }

    private Matrix2 viewMatrix = new Matrix2();
    private HexBatch batch;

    private ShaderProgram program;
    private TextureHandle texture;


    private int uniformMatrixHandle;

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        GLES20.glEnable(GLES20.GL_BLEND);
        //Enable depth testing
        //GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        program = new ShaderProgram(context, R.raw.default_vert, R.raw.default_frag, new String[]{"a_position", "a_texture", "a_color"});
        texture = new TextureHandle( context, R.drawable.images, program,"u_texture");
        uniformMatrixHandle = program.getUniformID("u_Matrix");

        batch = new HexBatch(this.context);
        lastTime = System.nanoTime();
    }

    public void setSize(int x, int y){
         viewMatrix.setScreen(x, y, BOARDWIDTH, 0);
    }


    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
    }


    long lastTime = System.nanoTime();

    @Override
    public void onDrawFrame(GL10 unused) {
        //Use culling to remove back faces.
        //GLES20.glEnable(GLES20.GL_CULL_FACE);



        //if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
		//	batch.colors.switchColor();

        long currentTime = System.nanoTime();
        long deltaTime = currentTime - lastTime;
        lastTime = currentTime;


        //Redraw background color


        ColorSource.source.setBackground();
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        program.bind();

        // Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        texture.bind();
        viewMatrix.bind(uniformMatrixHandle);

        for(int i = 0; i < 3; i++){
            GLES20.glEnableVertexAttribArray(i);
        }

        batch.drawShape(0,0, 1, 1);

        batch.drawShape(direction, 10, .5f);

        batch.end();

        for(int i = 0; i < 3; i++){ //TODO this should reflect number of vertices
            GLES20.glDisableVertexAttribArray(i);
        }
    }

    static HexXYZ direction = new HexXYZ();


    public void release(){

    }
    boolean multitouch = false;

    public void processTouch(MotionEvent e, float x, float y) {
        int action = e.getActionMasked();

        int pointerID = e.getPointerId(e.getActionIndex());
        if(e.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN){
            multitouch = true;
            if( e.getPointerCount() == 3  ) {

                ColorSource.source.switchColor();
            }
        } else  if( action == MotionEvent.ACTION_DOWN ) {

            direction.setDirection(x, y);
        } else if( action == MotionEvent.ACTION_UP){

            if(multitouch){
                multitouch = false;
                return;
            }
            release();

        } else {
            if(!multitouch)
            direction.setDirection(x, y);
        }
    }
}
