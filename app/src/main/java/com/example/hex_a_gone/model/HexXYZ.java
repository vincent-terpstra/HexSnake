package com.example.hex_a_gone.model;

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

    public void setDirection(float controlX, float controlY){
        //ignore centre
        float dotprod = controlY * controlY + controlX *controlX;

        if(dotprod < .2f) {
            set(0, 0);
            return;
        }
        int dir = controlY > 0 ? -1 : 1;
        if(Math.abs(controlX / controlY) < .5f){
            set(0, -dir,0);
        } else if ( controlY > 0){
            if(controlX > 0){
                set(-1,0,0);
            } else {
                set(0,0,-1);
            }
        } else {
            if(controlX > 0){
                set(0,0,1);
            } else {
                set(1,0,0);
            }
        }
    }
}
