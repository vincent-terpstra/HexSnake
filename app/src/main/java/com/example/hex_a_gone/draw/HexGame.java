package com.example.hex_a_gone.draw;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import com.example.hex_a_gone.R;
import com.example.hex_a_gone.board.Board;
import com.example.hex_a_gone.board.EffectManager;
import com.example.hex_a_gone.control.Button;
import com.example.hex_a_gone.model.HexXYZ;
import com.example.hex_a_gone.model.Matrix2;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class HexGame implements GLSurfaceView.Renderer {
    //endgame
    //undo's
    //icons
    private static final float BOARDWIDTH = 2.8f;

    private static boolean menu = false;

    public static void resetControl(){
        menu = true;
    }

    final Context context;

    public HexGame(
            Context context
    ){
        this.context = context;
        newGame = new Button() {
            @Override
            public void run() {
                effects.reset();
                history[0] = new Board();
                hIdx = 0;
                maxundo = 0;
                currentBoard().addRandom(4, effects);
            }
        };
        undo = new Button() {
            @Override
            public void run() {
                if(maxundo >0){
                    maxundo --;
                    hIdx = (hIdx + history.length - 1) % history.length;
                }
            }
        };
    }

    private Matrix2 viewMatrix = new Matrix2();
    private HexBatch batch;
    private Dragon dragon;
    //private Board board;
    private Board[] history = new Board[4]; //current board and 3 undo options
    private int hIdx = 0;
    private int maxundo = 0;

    private Board currentBoard(){
        return history[hIdx];
    }


    private EffectManager effects;
    private Button newGame;
    private Button undo;

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
        effects = new EffectManager();

        newGame.run();

        dragon = new Dragon(-3f,4f);
    }

    public void setSize(int x, int y){
         viewMatrix.setScreen(x, y, BOARDWIDTH, 0);

        float locy = 1 / viewMatrix.getMatrix()[3] - .7f;
        float locx = 2.1f;

        newGame.set(locx, -locy);
        undo.set(-locx, -locy);
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

	    effects.update(deltaTime / 1000000000f);

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

        dragon.drawDragon(batch);

        batch.drawScore(newGame.x(), -newGame.y(), currentBoard().score,5);
        //if(currentBoard().checkEndgame())
            batch.drawEndGame();


        if(effects.active()) {
            effects.draw(batch);
        }
        else {
            Board board = currentBoard();
            if (board != null) {
                for (int x = -2; x <= 2; x++) {
                    for (int y = -2; y <= 2; y++) {
                        if (Board.inHex(x, y))
                            batch.drawHexValue(x, y, board.get(x, y));
                    }
                }
            }
        }
        batch.drawIndicator(direction, 5);

        batch.drawButton( newGame, batch.newGame, 5 );
        batch.drawButton(undo, batch.undo, 5 );



        batch.end();

        for(int i = 0; i < 3; i++){ //TODO this should reflect number of vertices
            GLES20.glDisableVertexAttribArray(i);
        }
    }

    static HexXYZ direction = new HexXYZ();


    public void release(){
        if(menu)
            return;
        Board nextBoard = currentBoard().move(direction, effects);
        if(!nextBoard.isEqual(currentBoard())){
            hIdx = (hIdx + 1) % history.length;
            nextBoard.addRandom(4, effects);
            history[hIdx] = nextBoard;
            maxundo = Math.min(maxundo +1, history.length - 1);
        }
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


            menu = false;
         //   System.out.println("XXX: " + newGame + " " + x * BOARDWIDTH + " " + y * BOARDWIDTH );

            if( newGame.touchDown(x * BOARDWIDTH, y * BOARDWIDTH, pointerID) ||
                undo.touchDown(x * BOARDWIDTH, y * BOARDWIDTH, pointerID))
                return;

            direction.setDirection(x, y);
        } else if( action == MotionEvent.ACTION_UP){

            if(multitouch){
                multitouch = false;
                return;
            }

            if(newGame.touchUp(x * BOARDWIDTH, y * BOARDWIDTH, pointerID) ||
                undo.touchUp(x * BOARDWIDTH, y * BOARDWIDTH, pointerID))
                return;

            release();

        } else {
            if(!multitouch)
            direction.setDirection(x, y);
        }
    }
}
