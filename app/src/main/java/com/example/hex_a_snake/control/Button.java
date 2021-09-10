package com.example.hex_a_snake.control;

import com.example.hex_a_snake.model.PointXY;

public abstract class Button extends PointXY {

    private int touchIdx = -1;
    public int clr = 5;

    public boolean isTouched(){
        return touchIdx != -1;
    }

    public boolean touchDown(float touchX, float touchY, int touch){
        if(inRange(touchX, touchY, .5f)){
            touchIdx = touch;
            clr ++;
            return true;
        }
        return false;
    }

    public boolean touchUp(float touchX, float touchY, int touch){
        if(inRange(touchX, touchY, .52f)){
            if(touchIdx == touch)
                run();
            touchIdx = -1;
            return true;
        }
        if(touchIdx == touch){
            touchIdx = -1;
            return true;
        }

        return false;
    }

    public abstract void run();

    private float range(float touchX, float touchY){
        touchX -= x;
        touchY -= y;

        return  touchX * touchX + touchY * touchY;
    }

    private boolean inRange(float touchX, float touchY, float range){
        return range(touchX, touchY) < range;
    }
}
