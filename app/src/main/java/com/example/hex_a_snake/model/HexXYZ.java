package com.example.hex_a_snake.model;

public class HexXYZ {
    private float x, y;

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }

    public void set(float x, float y){
        this.x = x;
        this.y = y;
    }

    public HexXYZ set(float x, float y, float z){
        set(x - z, y - z);
        return this;
    }

    public boolean setDirection(float controlX, float controlY){
        //ignore centre
        float dotprod = controlY * controlY + controlX *controlX;

        if(dotprod < .035f) { // keep moving in the same direction
            System.out.println("DotPROD" + dotprod);
            return false;
        }
        if(controlY != 0 && controlX != 0)
        if(Math.abs(controlX / controlY) < .5f){
            int dir = controlY > 0 ? -1 : 1;
            set(0, -dir,0);
        } else {
            int first = controlX > 0 ? -1 : 1;
            int second = controlX * controlY > 0 ? 0 : first;
            set(first, second);
        }
        /*
        else if ( controlY > 0){
            if(controlX > 0){
                set(-1,0);
            } else {
                set(1,1,-1);
            }
        } else {
            if(controlX > 0){
                set(-1,-1);
            } else {
                set(1,0);
            }
        }
    */

        return true;
    }
}
