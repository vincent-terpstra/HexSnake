package com.example.hex_a_gone.draw;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.opengl.GLES20;

public class ColorSource {
    public static ColorSource source;

    final float[][] clrs;
    final float[][] shadows;

    int[] clr1 = {38, 42, 44, 235, 234, 240};
    boolean toggle = true;

    private final Context context;

    float[] background;

    public ColorSource(Context ctx){
        this.context = ctx;
        source = this;
        clrs = new float[][]{
         //       {  38,  42,  44 },
                { 165, 165, 200 },
                { 160, 160, 190 },
                { 160, 160, 190 },
                { 155, 152, 179 },
                { 155, 152, 179 },
                { 138, 130, 153 },
                { 138, 130, 153 },
                { 121, 108, 128 },
                {  99, 113, 140 },
                {  96, 121, 135 },
                {  93, 140, 121 },
                {  85, 140,  95 },
                { 181, 160,  85 },
                { 190, 149,  86 },
                { 172, 112,  73 },
                { 145,  67,  51 },
                { 140,  49,  49 }
        };


        for(float[] array : clrs){
            for(int i = 0; i < 3; i++){
                array[i] /= 255f;
            }
        }

        shadows= new float[clrs.length][];

        int idx = 0;
        for(float[] c : clrs){
            shadows[idx++] = new float[]{c[0] * .5f, c[1] * .6f, c[2] * .5f};
        }

        toggle = true;
        try{
            SharedPreferences sharedPreferences = context.getSharedPreferences(
                    "hexSliderSharedPreferences", Context.MODE_PRIVATE);

            toggle = !sharedPreferences.getBoolean("colorToggle", true);
        } catch (Exception ex){
        }


        switchColor();
    }



    @SuppressLint("CommitPrefEdits")
    public void switchColor(){
        toggle = !toggle;
        try {
            SharedPreferences.Editor hexSliderSharedPreferences = context.getSharedPreferences(
                    "hexSliderSharedPreferences", Context.MODE_PRIVATE).edit();

            hexSliderSharedPreferences.putBoolean("colorToggle", toggle);
            hexSliderSharedPreferences.apply();
        } catch (Exception ex){
        }

        int shift = toggle? 0 : 3;

        int r = clr1[shift++], g = clr1[shift++], b = clr1[shift];
        background = new float[]{r/255f, g/255f, b/255f, 1.0f};
    }

    int max(int idx){
        return Math.min(idx, clrs.length -1);
    }

    public float[] getColor(int idx){
        return clrs[max(idx)];
    }

    public float[] getShadow(int idx) {
        return shadows[max(idx)];
    }

    public void setBackground() {
        GLES20.glClearColor(background[0], background[1], background[2], 1.0f);
    }
}
