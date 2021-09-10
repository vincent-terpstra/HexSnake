package com.example.hex_a_snake.board;

import android.graphics.Point;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.view.KeyEvent;
import android.view.MotionEvent;

import androidx.annotation.RequiresApi;

import com.example.hex_a_snake.MainActivity;
import com.example.hex_a_snake.draw.ColorSource;
import com.example.hex_a_snake.draw.HexGameSnake;

public class HexGameControl extends GLSurfaceView {
    private final HexGameSnake renderer;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public HexGameControl(MainActivity mainActivity, HexGameSnake renderer, Point size){
        super(mainActivity);

        this.renderer = renderer;

        setEGLContextClientVersion(2);

        setRenderer(renderer);
    }

    @Override public boolean onTouchEvent(MotionEvent e) {



        float x = 2 * e.getX() / this.getRight() - 1f;
        float y = (-2 * e.getY() / this.getBottom() + 1f) / this.getRight() * this.getBottom();

        renderer.processTouch(e, x, y );

        //System.out.print("OnTouchEvent " + x + " : " + y + " " + e.getAction() + " " + e.toString() + " " + e.getActionIndex());
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_SPACE:
            {
                //your Action code
                ColorSource.source.switchColor();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
